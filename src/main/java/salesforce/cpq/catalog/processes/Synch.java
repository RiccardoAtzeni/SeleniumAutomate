package salesforce.cpq.catalog.processes;

import core.Driver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import prototype.BasicProcess;
import java.util.ArrayList;

public class Synch extends BasicProcess {
    private static final Logger log = Logger.getLogger(Synch.class);
    long timeout,pause;

    public Synch(String timeout, String pause){
        try{
            this.timeout=Long.valueOf(timeout.trim());
            this.pause=Long.valueOf(pause.trim());
        }catch(Exception ex){
            log.warn("Error occurred: "+ex.getMessage());
            log.warn("Using default timeout(4 hours) and retry(15 minutes) for Synch process");
            this.timeout=240;
            this.pause=900000;
        }
    }

    @Override
    public boolean fire(Driver driver) {
        try {
            driver.click(By.xpath("//input[@value='Synch all rules']"));
            return awaitCompleted(timeout, pause, driver, new ArrayList<By>(){{
                add(By.xpath("//td[starts-with(span,'Starting')]"));
                add(By.xpath("//td[starts-with(span,'Process completed')]"));}});
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
        log.info("URL: "+driver.getCurrentUrl());
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


        return awaitCompleted(timeout,pause,driver,new ArrayList<By>(){{
            add(null);
            add(By.xpath("//div[div='Process completed!']"));}});

    }

}
