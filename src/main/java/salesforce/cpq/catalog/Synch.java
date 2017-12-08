package salesforce.cpq.catalog;

import core.Driver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import prototype.BasicProcess;

public class Synch extends BasicProcess {
    private static final Logger log = Logger.getLogger(Synch.class);
    long timeout;
    long pause;

    public Synch(String timeout, String pause){
        try{
            this.timeout=Long.valueOf(timeout.trim());
            this.pause=Long.valueOf(pause.trim());
            log.info("Inside synch: "+this.timeout+", "+this.pause);
        }catch(NumberFormatException ex){
            log.warn("Error occurred: "+ex.getMessage());
            log.warn("Using default timeout(6 hours) and retry(10 minutes) for Synch process");
            this.timeout=360;
            this.pause=30000;
        }
    }

    @Override
    public boolean fire(Driver driver) {
        try {
            driver.getElement(By.xpath("//td[starts-with(span,'Starting')]"));
            if (awaitCompleted(timeout, pause, driver, By.xpath("//input[@value='Synch all rules']"),
                    By.xpath("//td[starts-with(span,'Process completed')]")))
                return true;
            else return false;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }

}
