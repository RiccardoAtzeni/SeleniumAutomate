package core;

import java.io.File;
import java.io.IOException;

import java.util.Set;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Driver {
	private static final Logger log = Logger.getLogger(Driver.class);

	private static Driver driver;
	private static boolean exist=false;
	private ChromeDriverService service;
	private WebDriver webDriver;
	private WebDriverWait webwait;
	private JavascriptExecutor js;
	private EventFiringWebDriver eventDriver;

	private Driver(String pathChromeDriver) throws IOException{
		service = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(pathChromeDriver))
				.usingAnyFreePort()
				.build();
		service.start();
		log.info("Starting ChromeDriverService to "+service.getUrl());
		DesiredCapabilities chromecap = DesiredCapabilities.chrome();
		chromecap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
		webDriver =new RemoteWebDriver(service.getUrl(), chromecap);
		eventDriver = new EventFiringWebDriver(webDriver);
		js= (JavascriptExecutor) webDriver;
	}

	
	public static Driver createDriver(String pathChromeDriver) throws IOException{
		if(!exist){
			driver =new Driver(pathChromeDriver);
			exist=true;
		}
		return driver;
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public void setWait(long timeout){
		if(webwait==null)
			log.debug("Setting element timeout: "+timeout+" seconds");
		else
			log.warn("Override element timeout: "+timeout);
		webwait = new WebDriverWait(webDriver,timeout);
	}

	public void click(By by){
		WebElement element = webwait.until(ExpectedConditions.elementToBeClickable(by));
		log.debug("Clicking on "+element.getTagName()+": "+element.getText()+"Found "+by);
			try{
				element.click();
			}catch(UnhandledAlertException ex){
				acceptAlert();
			}
	}


	public WebElement getElement(By by){
		WebElement element=null;
		try{
			element = webwait.until(ExpectedConditions.elementToBeClickable(by));
			log.debug("Retrieving the element "+element.getTagName()+": "+element.getText()+". Found "+by);
		}catch(Exception ex){
			if(ex instanceof NoSuchElementException){
				log.warn("No element found for following criteria: "+by.toString());
				return null;
			}
			else if(ex instanceof UnhandledAlertException){
				acceptAlert();
			}
			else{
				log.error("getElement() throwed the following exception: "+ex.getClass()+"\n"+ex.getMessage());
				throw ex;
			}

		}finally {
			if(element!=null)
				return element;
			else
				return null;
		}
	}

	public String getWindowHandle(){return eventDriver.getWindowHandle();}
	public Set<String> getWindowHandles(){return eventDriver.getWindowHandles();}

	public void switchToWin(String win){
		log.debug("Switching to window: "+win);
		eventDriver.switchTo().window(win);
	}

	public void acceptAlert(){
		Alert alert = eventDriver.switchTo().alert();
		log.warn("Alert open '"+alert.getText()+"'");
		alert.accept();
	}

	public void sendKeys(By by, String key){
		log.debug("Filling the element found "+by+" -> "+key);
		eventDriver.findElement(by).sendKeys(key);
	}

	public void navigateTo(String url){
		log.debug("Navigate to: "+url);
		eventDriver.get(url);
	}

	public void finish(){
		eventDriver.quit();
		service.stop();
	}

}
