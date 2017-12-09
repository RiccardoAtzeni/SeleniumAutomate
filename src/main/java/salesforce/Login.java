package salesforce;

import core.AutomateException;
import core.Driver;
import org.apache.log4j.Logger;
import org.apache.xpath.operations.Bool;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import prototype.BasicProcess;

public class Login extends BasicProcess{
    private static final String SF_URL = "http://test.salesforce.com";
    private static Logger log = Logger.getLogger(Login.class);
    private String user;
    private String pwd;
    private int maxRetry;
    private int retry;
    private long timeout;

    public Login(String user, String pwd, String retry, String timeout){
        this.user=user;
        this.pwd=pwd;
        this.retry=0;
        try{
            maxRetry = Integer.valueOf(retry)>0? Integer.valueOf(retry) : 0;
            this.timeout = Long.valueOf(timeout);
        }catch(NumberFormatException ex){
            maxRetry=0;
            this.timeout=0L;
        }
    }

    @Override
    public boolean fire(Driver driver) {

        try{
            driver.navigateTo(SF_URL);
            if(!driver.getCurrentUrl().contains("salesforce"))
                throw new AutomateException("Navigate to SF login page failed. Current url: "+driver.getCurrentUrl()+"\n"+
                    "Make sure you have any proxy disabled (automatic Internet provider rendering web page, too)");
            driver.sendKeys(By.id("username"),user);
            driver.sendKeys(By.id("password"),pwd);
            driver.getElement(By.id("Login"),true).submit();
            String header = driver.getElement(By.id("header"),true).getText();
            if(header.equalsIgnoreCase("Verify Your Identity")){
                log.error("For security reason you need to fill your verification code in the Salesforce login page");
                log.error("Please make that and fire the app again");
                return false;
            }
            else if(header.equalsIgnoreCase("TLS Upgrade Required")){
                log.debug("TLS Upgrade Required. Submit continue");
                driver.click(By.id("thePage:inputForm:continue"));
            }
        }catch(Exception ex){
            if(ex instanceof TimeoutException || ex instanceof AutomateException){
                log.error(ex.getMessage());
                if(maxRetry>0 && retry<=maxRetry){
                    try {
                        driver.setWait((timeout)*((++retry)+1));
                        log.warn("Login Timeout. Retry "+retry+"/"+maxRetry+"...");
                        fire(driver);

                    }catch(NumberFormatException exc){
                        log.error("Impossible to retry login");
                    }
                }
                else return false;
            }
            return false;
        }
        return true;
    }

}
