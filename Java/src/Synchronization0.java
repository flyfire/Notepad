package org.solarex.threadtest;
public class Synchronization0{
    public static void main(String[] args){ 
        ProcessingThread pt = new ProcessingThread();
        Thread t1 = new Thread(pt, "t1");
        Thread t2 = new Thread(pt, "t2");
        try{
            t1.start();
            t2.start();

            t1.join();
            t2.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("pt.count = " + pt.getCount());
    }
}

class ProcessingThread implements Runnable{
    private int count;

    @Override
    public void run(){
        int i = 0;
        for(i=1; i<5; i++)
        {
            processSth(i);
            System.out.println(Thread.currentThread().getId() + " increasing count befor = " + count);
            count++;
        }
    }

    public int getCount(){
        return this.count;
    }

    private void processSth(int i){
        try{
            Thread.sleep(i * 100);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
