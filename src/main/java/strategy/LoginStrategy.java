package strategy;

import importCat.Driver;

public class LoginStrategy extends ImportStrategy {

	private String user;
	private String pwd;
	private boolean autoMode=false;
	private Driver dr;

	public LoginStrategy(String user, String pwd, boolean autoMode) {
		super();
		this.user = user;
		this.pwd = pwd;
		this.autoMode=autoMode;
	}
	
	public LoginStrategy() {}

	@Override
	public void execute() throws Exception {
		
		dr = Driver.createDriver("chromedriver.exe");
		if((getStatus()==ImportStrategy.UNLOGGED) && autoMode){
			dr.login(user,pwd);
			getBaseUrlESetStatus();
			dr.aspetta("allTabsArrow");
			setStatus(ImportStrategy.LOGGED);
		}else if((getStatus()==ImportStrategy.UNLOGGED) && !autoMode){
			dr.vaiAllaPagina("http://test.salesforce.com");
			dr.alertJS("Immetti User e Password...");
			dr.aspetta("allTabsArrow");
			getBaseUrlESetStatus();
			setStatus(ImportStrategy.LOGGED);
		}else if(autoMode && (user.isEmpty() || pwd.isEmpty())){
			throw new Exception("inserire user o password");
		}
	}
	private void getBaseUrlESetStatus(){
		String temp=dr.getUrl();
		temp="https://"+temp.split("/")[2];
		setBaseUrl(temp);
		setStatus(LOGGED);
	}

}
