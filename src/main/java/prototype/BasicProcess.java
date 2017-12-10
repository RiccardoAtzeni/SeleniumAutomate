package prototype;

import core.Driver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class BasicProcess {
    private static final Logger log = Logger.getLogger(BasicProcess.class);

    public abstract boolean fire(Driver driver);

    public boolean awaitCompleted(long timeout, long pause, Driver driver, List<By> locators) {
        int retry;
        long init, elapsed;
        if(locators==null || locators.size()!=2){
            log.error("awaitCompleted() expects exactly 2 condtions parametrs. List<By> received: "+locators);
            return false;
        }

        for (int i=0; i<2; i++) {
            long freeze = i==0? 5000 : pause;
            retry=0;
            init = System.nanoTime();
            elapsed = TimeUnit.MINUTES.convert((System.nanoTime() - init), TimeUnit.NANOSECONDS);
            By condition = locators.get(i);
            if (condition != null) {
                log.info("CONDITION EVALUETED: "+condition);
                WebElement item = driver.getElement(condition, false);
                while (item == null && elapsed < timeout) {
                    log.warn("RETRY n." + (++retry));
                    elapsed = TimeUnit.MINUTES.convert((System.nanoTime() - init), TimeUnit.NANOSECONDS);
                    log.info("Elapsed time: " + elapsed + " minutes since the begging of the retrieving process");
                    try {
                        Thread.sleep(freeze);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                        return false;
                    }
                    item = driver.getElement(condition, false);
                }
                if (elapsed >= timeout) {
                    log.error("Timeout reached: impossible to retry the element searched " + condition);
                    return false;
                } else {
                    if(i==1){
                        log.debug("Process completed!\n");
                        return true;
                    }
                    else
                        log.debug("Check item retrieved "+condition+". Proceed to check the end condition for the process\n");
                }
            }
        }
        return false;
    }
}
