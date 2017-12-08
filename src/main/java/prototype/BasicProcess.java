package prototype;

import core.Driver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

public abstract class BasicProcess {
    private static final Logger log = Logger.getLogger(BasicProcess.class);

    public abstract boolean fire(Driver driver);

    public boolean awaitCompleted(long timeout, long pause, Driver driver, By action, By condition){
        int retry=0;
        long start = System.nanoTime();
        long elapsed = TimeUnit.MINUTES.convert((System.nanoTime()-start), TimeUnit.NANOSECONDS);
        WebElement trigger;
        do{
            trigger = driver.getElement(action);
        }while(trigger==null);
        trigger.click();

        while(driver.getElement(condition)==null && elapsed<timeout){
            try {
                Thread.sleep(pause);
                log.info("Elapsed time: "+elapsed+" minutes");
                elapsed = TimeUnit.MINUTES.convert((System.nanoTime()-start), TimeUnit.NANOSECONDS);
                log.warn("Element searched "+condition+" not found.\nRetry n: "+(++retry));
            } catch (InterruptedException e) {
                log.error("Thread sleep throwed the following: "+e.getMessage());
                return false;
            }
        }
        if(elapsed>timeout){
            log.error("Timeout reached");
            return false;
        }
        else{
            log.debug("Process completed!");
            return true;
        }
    }
}
