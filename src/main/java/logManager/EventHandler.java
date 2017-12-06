package logManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class EventHandler implements WebDriverEventListener {
	private BufferedWriter writer;
	private File file;
	private LocalDateTime data;
	public EventHandler() {
		try {
			data=LocalDateTime.now();
			file=new File(System.getProperty("user.dir")+"/LogImportCatalogo"+stampaData()+".txt");
			writer=new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void scriviSuLog(String str){
		try {
			writer.write(stampaData()+" "+ str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String stampaData(){
		data=LocalDateTime.now();
		return data.getDayOfMonth()+"."+data.getMonthValue()+"."+data.getYear()+"--"+data.getHour()+"."+data.getMinute()+"."+data.getSecond()+"."+ (data.getNano()/1000000);
	}
	
	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		try {
			System.out.println(stampaData()+" cambiamento di valore da "+arg0.toString()+" in "+arg1.toString()+"\n");
			writer.write(stampaData()+" cambiamento di valore da "+arg0.toString()+" in "+arg1.toString()+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterClickOn(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		try {
			System.out.println(stampaData()+" click "+arg0.toString()+"\n");
			writer.write(stampaData()+" click "+arg0.toString()+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		// TODO Auto-generated method stub
		try {
			System.out.println(stampaData()+" trovato "+" con il metodo "+arg0.toString()+"\n");
			writer.write(stampaData()+" trovato con il metodo "+arg0.toString() +"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterNavigateBack(WebDriver arg0) {
		// TODO Auto-generated method stub
		try {
			System.out.println(stampaData()+" navigo indietro a "+arg0.toString()+"\n");
			writer.write(stampaData()+" navigo indietro a "+arg0.toString()+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterNavigateForward(WebDriver arg0) {
		// TODO Auto-generated method stub
		try {
			System.out.println(stampaData()+" navigo avanti a "+arg0.toString()+"\n");
			writer.write(stampaData()+" navigo avanti a "+arg0.toString()+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterNavigateRefresh(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateTo(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		try {
			System.out.println(stampaData()+" navigo a "+arg0.toString()+"\n");
			writer.write(stampaData()+" navigo a "+arg0.toString()+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterScript(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		try {
			System.out.println(stampaData()+" eseguo lo script "+arg0+"\n");
			writer.write(stampaData()+" eseguo lo script "+arg0+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeClickOn(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateBack(WebDriver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateForward(WebDriver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateRefresh(WebDriver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateTo(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeScript(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onException(Throwable arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		try {
			if(!(arg0 instanceof NoSuchWindowException)){				
				System.out.println(stampaData()+" Eccezione: "+arg0.getMessage()+"\n");
				writer.write(stampaData()+" Eccezione: "+arg0.getMessage()+"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void closeStream(){
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
