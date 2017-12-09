package prototype;

import core.Driver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

public abstract class BasicProcess {
    private static final Logger log = Logger.getLogger(BasicProcess.class);

    public abstract boolean fire(Driver driver);

    public boolean awaitCompleted(long timeout, long pause, Driver driver, By start, By end) {
        int retry=0;
        long init = System.nanoTime();
        long elapsed = TimeUnit.MINUTES.convert((System.nanoTime()-init), TimeUnit.NANOSECONDS);

        if(start!=null){
            WebElement condition=driver.getElement(start,false);
            while(condition==null && elapsed<timeout){
                log.warn("Element searched "+start+" NOT FOUND.\n\nRETRY n."+(++retry));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    return false;
                }
                elapsed = TimeUnit.MINUTES.convert((System.nanoTime()-init), TimeUnit.NANOSECONDS);
                log.info("***Elapsed time: "+elapsed+" minutes since the begging of the retrieving process***");
                condition = driver.getElement(start,false);
            }

            if(condition==null){
                log.error("Timeout reached: impossible to retry the element searched "+start);
                return false;
            }
            retry=0;
            elapsed=0L;
        }

        while(driver.getElement(end,false)==null && elapsed<timeout){
            try {
                log.warn("Element searched "+end+" NOT FOUND.\n\nRETRY n."+(++retry));
                elapsed = TimeUnit.MINUTES.convert((System.nanoTime()-init), TimeUnit.NANOSECONDS);
                log.info("***Elapsed time: "+elapsed+" minutes since the begging of the retrieving process***");
                Thread.sleep(pause);
            } catch (InterruptedException e) {
                log.error(e);
                return false;
            }
        }
        if(elapsed>timeout){
            log.error("Timeout reached: impossible to retry the element searched "+end);
            return false;
        }
        else{
            log.debug("Process completed!");
            return true;
        }
    }
}
