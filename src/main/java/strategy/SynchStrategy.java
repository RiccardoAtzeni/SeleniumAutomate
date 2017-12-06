package strategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;


import importCat.Driver;
import importCat.wrongPopUpException;

public class SynchStrategy extends ImportStrategy {

	@Override
	public void execute() throws Exception {
		String url = null;
		String url2;
		if(getStatus()==ImportStrategy.UNLOGGED){
			return;
		}
		Driver dr;
		String temp;
		Pattern pattern = Pattern.compile("'(.*?)'");
		Matcher matcher;
		dr = Driver.createDriver("chromedriver.exe");
		dr.click(By.cssSelector("img.allTabsArrow"), "allTabsArrow");
		dr.click(By.linkText("Catalogs"), "Catalogs");
		dr.click(By.linkText("Fastweb Master Catalog"), "Fastweb Master Catalog");
		url2=dr.getUrl();
		matcher=pattern.matcher(dr.getAttribute(By.name("ne__publish_all_items"), "ne__publish_all_items", "onclick"));
		if(matcher.find()){
			url=getBaseUrl()+matcher.group(1);
		}
		dr.vaiAllaPagina(url);
		dr.click(By.name("j_id0:j_id25:preview:j_id33:j_id34"), "j_id0:j_id25:preview:j_id33:j_id34"); //spunta beta
	//	dr.selectPickList(By.name("j_id0:j_id25:preview:j_id38"), "Naviga Mobile 2GB, Version: 1"); //se commentata fa la synch di tutte le regole
		dr.click(By.id("j_id0:j_id25:preview:j_id26:bottom:j_id30"), "j_id0:j_id25:preview:j_id26:bottom:j_id30"); //recreate programm
		if(!(temp=dr.closeAlertAndGetItsText()).equals("The action synchronizes Salesforce data with the engine, are you sure?")){
			throw new wrongPopUpException(temp);
		}
		
		while(!dr.esiste("j_id0:j_id25:preview:j_id26:bottom:synchStatus.start\" style=\"display: none")){ //rotellina
			Thread.sleep(100);
			
		}
		dr.cercaErrori(By.xpath("//tr[@class=\"dataCell\"]"),"dataCell");
		dr.click(By.id("j_id0:j_id25:preview:j_id26:bottom:j_id27"), "j_id0:j_id25:preview:j_id26:bottom:j_id27");
		while(!dr.esiste("j_id0:j_id25:preview:j_id26:bottom:synchStatus.start\" style=\"display: none")){
			Thread.sleep(100);
		}
		dr.cercaErrori(By.xpath("//tr[@class=\"showborder\"]"),"showborder");
		dr.click(By.id("j_id0:j_id25:preview:j_id26:bottom:j_id29"), "j_id0:j_id25:preview:j_id26:bottom:j_id29");
		if(!(temp=dr.closeAlertAndGetItsText()).equals("The action synchronizes Salesforce data with the engine, are you sure?")){
			throw new wrongPopUpException(temp);
		}
		while(!dr.esiste("Starting")){
			Thread.sleep(100);
			
		}
		while(!dr.esiste("Process completed!")){
			Thread.sleep(100);
			
		}
		dr.cercaErrori(By.xpath("//tr[@class=\"showborder\"]"),"showborder");
		dr.vaiAllaPagina(url2);
		matcher=pattern.matcher(dr.getAttribute(By.name("bit2archetypes__manage_archetypes"), "bit2archetypes__manage_archetypes", "onclick"));
		if(matcher.find()){
			url=getBaseUrl()+matcher.group(1);
		}
		dr.vaiAllaPagina(url);
		dr.selectPickList(By.id("rowsFilterOptions"),"<div class=\"hideArea\" id=\"overlay\">",/* "Naviga Mobile 2GB, Version: 1"*/ "All");
		dr.click(By.xpath("//button[@onclick='synchAllRows();return false;']"), "<div class=\"hideArea\" id=\"overlay\">");
		if(!(temp=dr.closeAlertAndGetItsText()).equals("The action synchronizes Salesforce data with the engine, are you sure?")){
			throw new wrongPopUpException(temp);
		}
		dr.aspetta("<div id=\"loadingMsg\">Process completed!</div>");
		dr.vaiAllaPagina(getBaseUrl());
		dr.alertJS("Catalogo importato correttamente");
	}
	
}


