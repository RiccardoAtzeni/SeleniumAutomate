package salesforce.cpq.catalog;

import core.Driver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import prototype.BasicProcess;

public class Recreate extends BasicProcess {
    private static final Logger log = Logger.getLogger(Recreate.class);
    long timeout;
    long pause;

    public Recreate(String timeout, String pause){
        try{
            this.timeout=Long.valueOf(timeout.trim());
            this.pause=Long.valueOf(pause.trim());;
        }catch(NumberFormatException ex){
            log.warn("Error occurred: "+ex.getMessage());
            log.warn("Using default timeout(30 minutes) and retry(30 seconds) for Recreate process");
            this.timeout=30;
            this.pause=30;
        }
    }

    @Override
    public boolean fire(Driver driver) {
        driver.click(By.cssSelector("img.allTabsArrow"));
        driver.click(By.linkText("Catalogs"));
        driver.click(By.linkText("Fastweb Master Catalog"));
        driver.setBeforeWin(driver.getWindowHandle());
        driver.click(By.name("ne__publish_all_items"));
        for(String actualWin: driver.getWindowHandles())
            driver.switchToWin(actualWin);
        driver.click(By.xpath("(//input[@type='checkbox'])[1]"));
        driver.click(By.xpath("(//input[@type='checkbox'])[2]"));

        driver.click(By.xpath("//input[@value='Recreate programs']"));
        if(awaitCompleted(timeout,pause,driver,By.xpath("//td[starts-with(span,'Starting')]"),
                By.xpath("//td[starts-with(span,'Process completed')]")))
            return true;
        else return false;
    }


}
