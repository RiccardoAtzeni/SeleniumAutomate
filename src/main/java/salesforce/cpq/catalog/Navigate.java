package salesforce.cpq.catalog;

import core.Driver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

public class Navigate {

    private static final Logger log = Logger.getLogger(Navigate.class);
    private static Boolean isUrlWrong=false;

    public static Boolean navigateToCatalog(Driver driver,String user, String domin){

        if(!driver.getCurrentUrl().endsWith("home.jsp")) {
            log.info("not correct url: "+driver.getCurrentUrl());
            String hpUrl = getOrgHomepage(user,domin);
            if(hpUrl==null || isUrlWrong)
                return false;
            else{
                log.info("Retry!");
                driver.navigateTo(hpUrl);
                isUrlWrong=true;
                navigateToCatalog(driver,user,domin);
            }
        }
        log.info("Correct URL!");
        isUrlWrong=false;
        driver.click(By.cssSelector("img.allTabsArrow"));
        driver.click(By.linkText("Catalogs"));
        driver.click(By.linkText("Fastweb Master Catalog"));
        driver.setBeforeWin(driver.getWindowHandle());
        return true;
    }

    private static String getOrgHomepage(String user,String domin){
        String instance=null;
        String elements[] = user.split(".");
        String org = elements[elements.length-1];
        if(domin.equals("fastweb01")){
            if(org.startsWith("cpqdevpro"))
                instance="cs88";
            else if(org.startsWith("qa"))
                instance="cs81";
        }

        if(instance==null){
            log.warn("Unknown organization: "+org);
            log.error("Impossible navigate to the home page");
            return null;
        }

        return "https://"+domin+"--"+org+instance+".my.salesforce.com/home/home.jsp";
    }
}
