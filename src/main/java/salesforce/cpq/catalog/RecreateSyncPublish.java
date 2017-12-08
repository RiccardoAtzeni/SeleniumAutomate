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
                Synch synch = new Synch(config.getProperty("synch.process.timeout"),config.getProperty("synch.retry"));
                if(synch.fire(driver)){
                    setStatus("SYNC_CAT_RULES_OK");
                    end();
                }
                else{
                    setStatus("SYNC_CAT_RULES_KO");
                    end();
                }
                break;
        }
    }

    @Override
    public void end() {
        //driver.finish();
    }
}
