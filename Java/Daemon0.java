package org.solarex.threadtest;
public class Daemon0{
    public static void main(String[] args){
        Thread dt = new Thread(new DaemonThread(), "dt");
        dt.setDaemon(true);
        try{
            dt.start();
            Thread.sleep(5000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("main thread finished");
    }
}
class DaemonThread implements Runnable{
    @Override 
    public void run(){
        while(true){
            processSth();
        }
    }
    
    private void processSth(){
        try{
            System.out.println("Processing daemon thread");
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
