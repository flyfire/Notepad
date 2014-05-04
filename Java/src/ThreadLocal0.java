package org.solarex.threadtest;
import java.util.Random;
import java.text.SimpleDateFormat;
public class ThreadLocal0 implements Runnable{

    private static final ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue(){
            return new SimpleDateFormat("yyyyMMdd HHmm");
        }
    };

    public static void main(String[] args) throws InterruptedException {
        ThreadLocal0 obj = new ThreadLocal0();
        for(int i=0; i<10; i++){
            Thread t = new Thread(obj, ""+i);
            Thread.sleep(new Random().nextInt(1000));
            t.start();
        }
    }

    @Override
    public void run(){
        System.out.println("id = " + Thread.currentThread().getId() + " default formatter = " + formatter.get().toPattern());
        try{
            Thread.sleep(new Random().nextInt(1000));
        }catch (Exception e){
            e.printStackTrace();
        }
        formatter.set(new SimpleDateFormat());
        System.out.println("id = " + Thread.currentThread().getId() + " modified formatter = " + formatter.get().toPattern());

    }
}
