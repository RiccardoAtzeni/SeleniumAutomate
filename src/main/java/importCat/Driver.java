package importCat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Select;

import logManager.EventHandler;

public class Driver {

	private static Driver driverClass;
	private static boolean exist=false;
	private ChromeDriverService service;
	private WebDriver driver;
	private JavascriptExecutor js;
	private EventFiringWebDriver eventDriver;
	private EventHandler handler;

	public WebDriver getDriver() {
		return driver;
	}
	private boolean acceptNextAlert=true;

	public void setAcceptNextAlert(boolean acceptNextAlert) {
		this.acceptNextAlert = acceptNextAlert;
	}
	
	private Driver(String pathChromeDriver) throws IOException{
		service = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(pathChromeDriver))
				.usingAnyFreePort()
				.build();
		service.start();
		driver=new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
		eventDriver = new EventFiringWebDriver(driver);
		handler = new EventHandler();
		eventDriver.register(handler);
		js= (JavascriptExecutor) driver;
	}
	
	public static Driver createDriver(String pathChromeDriver) throws IOException{
		if(!exist){
			driverClass=new Driver(pathChromeDriver);
			exist=true;
		}
		return driverClass;
	}
	public void arresta(){
		exist=false;
		eventDriver.close();
		driver.quit();
		arrestaService();
		
	}
	public void arrestaService(){
		exist=false;
		handler.closeStream();
		service.stop();
	}
	public void aspetta(String testo){
		while(!eventDriver.getPageSource().contains(testo)){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void notAspetta(String testo){
		while(eventDriver.getPageSource().contains(testo)){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void login(String user, String pwd){
		eventDriver.get("http://test.salesforce.com");
		WebElement searchBox = eventDriver.findElement(By.name("username"));
		searchBox.sendKeys(user);
		WebElement searchBox1 = eventDriver.findElement(By.id("password"));
		searchBox1.sendKeys(pwd);
		eventDriver.findElement(By.id("Login")).submit();
	}
	public void click(By by, String testo){
		aspetta(testo);
		eventDriver.findElement(by).click();
	}
	public void clickByHref(By by, String testo){
		aspetta(testo);
		eventDriver.get(eventDriver.findElement(by).getAttribute("href"));
	}
	public boolean esiste(String testo){
		if(eventDriver.getPageSource().contains(testo)){
			return true;
		}
		return false;
	}
	public String getUrl(){
		return eventDriver.getCurrentUrl();
	}
	public void vaiAllaPagina(String url){
		eventDriver.get(url);
	}
	public String getAttribute(By by, String testo, String attrib){
		aspetta(testo);
		return eventDriver.findElement(by).getAttribute(attrib);
	}
	public String closeAlertAndGetItsText() {
		try {
			if(!isAlertPresent()){
				return null;
			}
			Alert alert = eventDriver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
	public boolean isAlertPresent() 
	{ 
		try 
		{ 
			eventDriver.switchTo().alert(); 
			return true; 
		}   // try 
		catch (NoAlertPresentException Ex) 
		{ 
			return false; 
		}   // catch 
	}
	public void selectPickList(By by, String testo){
		new Select(eventDriver.findElement(by)).selectByVisibleText(testo);
	}
	public void selectPickList(By by, String aspetta, String testo){
		aspetta(aspetta);
		new Select(eventDriver.findElement(by)).selectByVisibleText(testo);
	}
	public void alertJS(String testo){
		js.executeScript("alert('"+testo+"')");
		timeOut();
	}
	public void timeOut(){
		while(isAlertPresent()){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void cercaErrori(By by, String testo){
		List<WebElement> tr=driver.findElements(by);
		List<WebElement> td=driver.findElements(By.xpath("//td[@class=\""+testo+"\"]"));
		int righe=tr.size()-1;
		int campi=td.size();
		int colonne=campi/righe;
		String temp;
		for(int i=0;i<campi;i=i+colonne){
			temp="";
			for(int j=0;j<colonne;j++){				
				temp+=td.get(i+j).getText()+" ";
			}
			handler.scriviSuLog("[Errore di import]:"+temp+"\n");
		}
		
	}
}
