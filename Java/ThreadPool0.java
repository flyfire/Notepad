package org.solarex.threadtest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool0{
    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for ( int i=0; i<10; i++){
            Runnable worker = new WorkerThread(""+i);
            executor.execute(worker);
        }
        executor.shutdown();
        while(!executor.isTerminated()){
        }
        System.out.println("Finished all threads");
    }
}

class WorkerThread implements Runnable{
    private String command;
    public WorkerThread(String s){
        this.command = s ;
    }

    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName() + " started, command = " + command);
        try{
            Thread.sleep(5000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.command;
    }
}
