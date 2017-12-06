package strategy;

import org.openqa.selenium.By;

import importCat.Driver;
import importCat.wrongPopUpException;

public class ConcreteImport extends ImportStrategy {

	private final static String REQUESTID="Bit2WinImport?requestId=";
	private final static String STARTFROM="&startFrom=0";
	@Override
	public void execute() throws Exception {
		String url="";
		String url2;
		if((getStatus()==ImportStrategy.UNLOGGED)){
			return;
		}
		Driver dr;
		String temp;

		dr = Driver.createDriver("chromedriver.exe");
		dr.alertJS("Navigo nella pagina di import, attendi il prossimo alert...");
		dr.click(By.cssSelector("img.allTabsArrow"), "allTabsArrow");
		dr.click(By.linkText("Bit2Win"),"Bit2Win");
		dr.click(By.linkText("Deploy"),"Deploy");
		dr.clickByHref(By.linkText("Import Object"),"Import Object");
		dr.click(By.id("loadFilesButton"),"loadFilesButton");
		dr.alertJS("Trascina gli zip nella finestra di caricamento, poi il processo sarà automatizzato. Al Termine verrà visualizzato l alert di completa installazione");
		dr.aspetta("CatalogItem__");
		dr.click(By.xpath("(//button[@onclick='hideModal();return false;'])[2]"),"style=\"display: inline-block;\">Close</button>");
		if(dr.esiste("id=\"checkDataMapID\" onclick=\"checkDataMap();return false;\" disabled=\"\"")){
			url=dr.getUrl();
			url=url.substring(0, url.lastIndexOf("/")+1);
			url2=dr.getAttribute(By.linkText("Go To Request"), "Go To Request", "onclick");
			url2=url2.split("'")[1].substring(1);
			dr.vaiAllaPagina(url+REQUESTID+url2+STARTFROM);
		}
		dr.click(By.id("checkDataMapID"), "checkDataMapID");
		dr.notAspetta("id=\"startImportButton\" onclick=\"hideModalDataMap();startImport();return false;\" style=\"margin-right: 2%; display: none;");
		dr.click(By.id("startImportButton"),"startImportButton");
		if(!(temp=dr.closeAlertAndGetItsText()).equals("Are you sure?")){
			throw new wrongPopUpException(temp);
		}
		while(!dr.isAlertPresent()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!(temp=dr.closeAlertAndGetItsText()).equals("To complete the import, start the synchronization")){
			throw new wrongPopUpException(temp);
		}
		dr.vaiAllaPagina(getBaseUrl());

	}

}
