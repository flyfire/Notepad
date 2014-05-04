package org.solarex.threadtest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerService {
    public static void main(String[] args){
        BlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(10);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        new Thread(producer).start();
        new Thread(consumer).start();
        System.out.println("Producer and Consumer has been started");
    }
}

class Message {
    private String msg;

    public Message(String str){
        this.msg = str;
    }

    public String getMsg(){
        return msg;
    }
}

class Producer implements Runnable {
    private BlockingQueue<Message> queue;

    public Producer(BlockingQueue<Message> q){
        this.queue = q;
    }

    @Override
    public void run(){
        for (int i=0; i<100; i++){
            Message msg = new Message(""+i);
            try{
                Thread.sleep(i);
                queue.put(msg);
                System.out.println("Produced " + msg.getMsg());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Message msg = new Message("exit");
        try{
            queue.put(msg);
        }catch ( InterruptedException ex){
            ex.printStackTrace();
        }
    }
}


class Consumer implements Runnable {
    private BlockingQueue<Message> queue;

    public Consumer(BlockingQueue<Message> q){
        this.queue = q;
    }

    @Override
    public void run(){
        try{
            Message msg;
            while (!(msg = queue.take()).getMsg().equals("exit")){
                Thread.sleep(10);
                System.out.println("Consumed " + msg.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
