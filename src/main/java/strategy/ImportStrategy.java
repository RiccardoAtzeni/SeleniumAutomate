package strategy;

public abstract class ImportStrategy {

	protected final static boolean LOGGED=true;
	protected final static boolean UNLOGGED=false;
	private static boolean status=UNLOGGED;
	private static String baseUrl;
	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		ImportStrategy.baseUrl = baseUrl;
	}

	public boolean getStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		ImportStrategy.status = status;
	}
	

	public abstract void execute() throws Exception;
}
