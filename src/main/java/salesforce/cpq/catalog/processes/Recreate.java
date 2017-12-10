package salesforce.cpq.catalog.processes;

import core.Driver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import prototype.BasicProcess;
import java.util.ArrayList;

public class Recreate extends BasicProcess {
    private static final Logger log = Logger.getLogger(Recreate.class);
    long timeout,pause;

    public Recreate(String timeout, String pause){
        try{
            this.timeout=Long.valueOf(timeout.trim());
            this.pause=Long.valueOf(pause.trim());
        }catch(NumberFormatException ex){
            log.warn("Error occurred: "+ex.getMessage());
            log.warn("Using default timeout(30 minutes) and retry(30 seconds) for Recreate process");
            this.timeout=30;
            this.pause=30;
        }
    }

    @Override
    public boolean fire(Driver driver) {
        driver.click(By.name("ne__publish_all_items"));
        for(String actualWin: driver.getWindowHandles())
            driver.switchToWin(actualWin);
        driver.click(By.xpath("(//input[@type='checkbox'])[1]"));
        driver.click(By.xpath("(//input[@type='checkbox'])[2]"));
        driver.click(By.xpath("//input[@value='Recreate programs']"));
        return awaitCompleted(timeout,pause,driver,  new ArrayList<By>(){{
            add(By.xpath("//td[starts-with(span,'Starting')]"));
            add(By.xpath("//td[starts-with(span,'Process completed')]"));}});
    }

}
