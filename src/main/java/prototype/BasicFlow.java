package prototype;


import core.AutomateException;

public interface BasicFlow {

    void initDriver();
    void start();
    void next() throws AutomateException;
    void end();
}
