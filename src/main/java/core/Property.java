package core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {
    private static Property config;
    private final static String CONFIGFILE="properties/config.properties";
    //private final static String CONFIGFILE="./config.properties";
    private static Properties properties;

    private Property(){}

    public static Property getInstance(){
        if(config==null)
            config= new Property();
        return config;
    }
    private static void init(){
        InputStream input = null;
        try {
            input = new FileInputStream(CONFIGFILE);
            properties=new Properties();
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(input!=null){
                try{
                    input.close();
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        }

    }

    public String getProperty(String key){
        if(properties==null)
            init();
        return properties.getProperty(key);
    }
}
