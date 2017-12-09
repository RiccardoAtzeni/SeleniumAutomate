package salesforce.cpq.catalog;

import core.Driver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import prototype.BasicProcess;

public class Publish extends BasicProcess{
    private static final Logger log = Logger.getLogger(Publish.class);
    long timeout, pause;

    public Publish(String timeout, String pause){
        try{
            this.timeout=Long.valueOf(timeout.trim());
            this.pause=Long.valueOf(pause.trim());
        }catch(Exception ex){
            log.warn("Error occurred: "+ex.getMessage());
            log.warn("Using default timeout(30 minutes) and retry(1 minute) for Publish process");
            this.timeout=360;
            this.pause=30000;
        }
    }

    @Override
    public boolean fire(Driver driver) {
        try {
            driver.click(By.xpath("//input[@value='Publish Catalog']"));
            if (awaitCompleted(timeout, pause, driver,By.xpath("//div[@id='loadingMex' and string-length(text())>0]"),
                    By.xpath("//input[@value='Publish Catalog']")))
                return true;
            else return false;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }
}
