package importCat;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;


public class Test {
	private final static String REQUESTID="Bit2WinImport?requestId=";
	private final static String STARTFROM="&startFrom=0";


	public static void main(String[] args) throws Exception {
		Driver dr;
		String baseUrl;
		String url="";
		String url2;
		Pattern pattern = Pattern.compile("'(.*?)'");
		Matcher matcher;
		try {
			dr = Driver.createDriver("src/main/resources/chromedriver.exe");
			
			dr.login("r75-reply@fastweb.it.cpqbitwin4","11le04lo75ne!");
			baseUrl=dr.getUrl();
			baseUrl=baseUrl.substring(0,baseUrl.lastIndexOf("/"));
			baseUrl=baseUrl.substring(0,baseUrl.lastIndexOf("/"));
		//	System.out.println(baseUrl);
			dr.click(By.cssSelector("img.allTabsArrow"), "allTabsArrow");
			/*dr.click(By.linkText("Bit2Win"),"Bit2Win");
			dr.click(By.linkText("Deploy"),"Deploy");
			dr.clickByHref(By.linkText("Import Object"),"Import Object");
			dr.click(By.id("loadFilesButton"),"loadFilesButton");
			dr.aspetta("CatalogItem__");
			dr.click(By.xpath("(//button[@onclick='hideModal();return false;'])[2]"),"style=\"display: inline-block;\">Close</button>");
			if(dr.esiste("id=\"checkDataMapID\" onclick=\"checkDataMap();return false;\" disabled=\"\"")){
				url=dr.getUrl();
				url=url.substring(0, url.lastIndexOf("/")+1);
				//dr.clickByHref(By.linkText("Go To Request"), "Go To Request");
				url2=dr.getAttribute(By.linkText("Go To Request"), "Go To Request", "onclick");
				url2=url2.split("'")[1].substring(1);
				dr.vaiAllaPagina(url+REQUESTID+url2+STARTFROM);
			}
			dr.click(By.id("checkDataMapID"), "checkDataMapID");
			dr.notAspetta("id=\"startImportButton\" onclick=\"hideModalDataMap();startImport();return false;\" style=\"margin-right: 2%; display: none;");
			dr.click(By.id("startImportButton"),"startImportButton");
			dr.closeAlertAndGetItsText();
			while(!dr.isAlertPresent()){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String temp;
			if(!(temp=dr.closeAlertAndGetItsText()).equals("To complete the import, start the synchronization")){
				throw new Exception("Il testo dell'allert non corrisponde a quello atteso: "+temp);
			}
			dr.vaiAllaPagina(baseUrl);
			dr.click(By.cssSelector("img.allTabsArrow"), "allTabsArrow");*/
			dr.click(By.linkText("Catalogs"), "Catalogs");
			dr.click(By.linkText("Fastweb Master Catalog"), "Fastweb Master Catalog");
			url2=dr.getUrl();
			matcher=pattern.matcher(dr.getAttribute(By.name("ne__publish_all_items"), "ne__publish_all_items", "onclick"));
			if(matcher.find()){
				url=baseUrl+matcher.group(1);
			}
			System.out.println(url);
			dr.vaiAllaPagina(url);
			dr.click(By.name("j_id0:j_id25:preview:j_id33:j_id34"), "j_id0:j_id25:preview:j_id33:j_id34");
			dr.selectPickList(By.name("j_id0:j_id25:preview:j_id38"), "Naviga Mobile 2GB, Version: 1");
			dr.click(By.id("j_id0:j_id25:preview:j_id26:bottom:j_id30"), "j_id0:j_id25:preview:j_id26:bottom:j_id30"); //recreate program
			dr.closeAlertAndGetItsText();
			System.out.println(dr.esiste("j_id0:j_id25:preview:j_id26:bottom:synchStatus.start\" style=\"display: none"));
			while(!dr.esiste("j_id0:j_id25:preview:j_id26:bottom:synchStatus.start\" style=\"display: none")){
				Thread.sleep(5);
				System.out.println(dr.esiste("j_id0:j_id25:preview:j_id26:bottom:synchStatus.start\" style=\"display: none"));
			}
			//Thread.sleep(1000);
			
			dr.click(By.id("j_id0:j_id25:preview:j_id26:bottom:j_id27"), "j_id0:j_id25:preview:j_id26:bottom:j_id27");
		//	dr.closeAlertAndGetItsText();
			while(!dr.esiste("j_id0:j_id25:preview:j_id26:bottom:synchStatus.start\" style=\"display: none")){
				Thread.sleep(5);
			}
			dr.click(By.id("j_id0:j_id25:preview:j_id26:bottom:j_id29"), "j_id0:j_id25:preview:j_id26:bottom:j_id29");
			dr.closeAlertAndGetItsText();
			while(!dr.esiste("Starting")){
				Thread.sleep(50);
				System.out.println("ok");
			}
			while(!dr.esiste("Process completed!")){
				Thread.sleep(50);
				System.out.println("ok1");
			}
			dr.vaiAllaPagina(url2);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	
}
