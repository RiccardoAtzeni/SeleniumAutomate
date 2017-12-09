package salesforce.cpq.catalog;

import core.AutomateException;
import core.Driver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import prototype.BasicProcess;

public class Synch extends BasicProcess {
    private static final Logger log = Logger.getLogger(Synch.class);
    long timeout;
    long pause;

    public Synch(String timeout, String pause){
        try{
            this.timeout=Long.valueOf(timeout.trim());
            this.pause=Long.valueOf(pause.trim());
        }catch(Exception ex){
            log.warn("Error occurred: "+ex.getMessage());
            log.warn("Using default timeout(6 hours) and retry(10 minutes) for Synch process");
            this.timeout=360;
            this.pause=30000;
        }
    }

    @Override
    public boolean fire(Driver driver) {
        try {
            driver.click(By.xpath("//input[@value='Synch all rules']"));
            if (awaitCompleted(timeout, pause, driver, By.xpath("//td[starts-with(span,'Starting')]"),
                    By.xpath("//td[starts-with(span,'Process completed')]")))
                return true;
            else return false;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }

    public boolean fireArchetypes(Driver driver) {
        driver.switchToWin(driver.getBeforeWin());
        if(driver.getCurrentUrl().contains("PublishAllItems")){
            log.error("Switch failed. Actual url: "+driver.getCurrentUrl()+" is not the correct one");
            return false;
        }
        driver.click(By.name("bit2archetypes__manage_archetypes"));
        for(String actualWin: driver.getWindowHandles())
            driver.switchToWin(actualWin);
        new Select(driver.getElement(By.id("rowsFilterOptions"),true)).selectByVisibleText("All");
        WebElement synch=null;

        do{
            if(synch!=null)
                log.warn("The button is Disabled");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                return false;
            }
            synch = driver.getElement(By.xpath("//button[@onclick='synchAllRows();return false;']"),false);
        }while(synch==null || synch.getAttribute("disabled")!=null);
        synch.click();

        if(awaitCompleted(timeout,pause,driver,null,
                By.xpath("//div[div='Process completed!']")))
            return true;
        else return false;
    }

}
