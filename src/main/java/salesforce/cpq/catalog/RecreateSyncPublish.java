package salesforce.cpq.catalog;

import core.Driver;
import core.Property;
import org.apache.log4j.Logger;
import prototype.BasicFlow;
import salesforce.Login;

public class RecreateSyncPublish implements BasicFlow{

    private static Property config;
    private static Logger log = Logger.getLogger(RecreateSyncPublish.class);
    public Driver driver;
    private String status;

    public RecreateSyncPublish(){
        initDriver();
    }


    public void setStatus(String status) {
        if(status.contains("KO"))
            log.error("RecreateSyncPublish Error Status: "+status);
        else
            log.info("RecreateSyncPublish Status: "+status);
        this.status = status;
    }

    @Override
    public void initDriver() {
        log.info("Retrieving configuration property");
        config = Property.getInstance();
        try {
            driver = Driver.createDriver(config.getProperty("chrome.driver"));
            driver.setWait(Long.parseLong(config.getProperty("find.element.timeout")));
        } catch (Exception e) {
               log.error(e.getMessage());
        }
        setStatus("STARTED");
        start();
    }

    @Override
    public void start() {
        log.debug("Starting login process");
        Login login = new Login(config.getProperty("user"),config.getProperty("pwd"),config.getProperty("login.retry"),
                config.getProperty("find.element.timeout"));
        if(login.fire(driver)){
            setStatus("LOGIN_OK");
            next();
        }
        else{
            setStatus("LOGIN_KO");
            end();
        }
    }

    @Override
    public void next() {
        Synch synch = null;
        switch (status){
            case "LOGIN_OK" :
                 Recreate recreate = new Recreate(config.getProperty("recreate.process.timeout"),config.getProperty("recreate.retry"));
                 if(recreate.fire(driver)){
                     setStatus("RECREATE_OK");
                     next();
                 }
                 else{
                     setStatus("RECREATE_KO");
                     end();
                 }
                 break;
            case "RECREATE_OK" :
                synch = new Synch(config.getProperty("synch.process.timeout"),config.getProperty("synch.retry"));
                if(synch.fire(driver)){
                    setStatus("SYNC_CAT_RULES_OK");
                    next();
                }
                else{
                    setStatus("SYNC_CAT_RULES_KO");
                    end();
                }
                break;
            case "SYNC_CAT_RULES_OK" :
                Publish pub = new Publish(config.getProperty("publish.process.timeout"),config.getProperty("publish.retry"));
                if(pub.fire(driver)){
                    setStatus("PUBLISH_OK");
                    next();
                }
                else
                {
                    setStatus("PUBLISH_KO");
                    end();
                }
                break;
            case "PUBLISH_OK" :
                    synch= new Synch(config.getProperty("synch.archetype.timeout"),config.getProperty("synch.archetype.retry"));
                    if(synch.fireArchetypes(driver))
                        setStatus("COMPLETED");
                    else
                        setStatus("SYNC_ARCH_KO");
                    end();
                    break;
        }
    }

    @Override
    public void end() {
        driver.finish();
    }
}
