package org.solarex.threadtest;
public class DeadLock0{
    public static void main(String[] args){
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        Thread t1 = new Thread(new SyncThread(obj1, obj2), "t1");
        Thread t2 = new Thread(new SyncThread(obj2, obj3), "t2");
        Thread t3 = new Thread(new SyncThread(obj3, obj1), "t3");

        try{
            t1.start();
            Thread.sleep(1000);
            t2.start();
            Thread.sleep(1000);
            t3.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class SyncThread implements Runnable{
    private Object obj1;
    private Object obj2;

    public SyncThread(Object o1, Object o2){
        this.obj1 = o1;
        this.obj2 = o2;
    }

    @Override
    public void run(){
        String name = Thread.currentThread().getName();
        System.out.println(name + " acquiring " + obj1);
        synchronized(obj1){
            System.out.println(name + " acquired " + obj1);
            work();
            System.out.println(name + " acquiring " + obj2);
            synchronized(obj2){
                System.out.println(name + "acquired " + obj2);
                work();
            }
            System.out.println(name + " released " + obj2);
        }
        System.out.println(name + " released " + obj1);
        System.out.println(name + " finished execution");
    }

    private void work(){
        try{
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
