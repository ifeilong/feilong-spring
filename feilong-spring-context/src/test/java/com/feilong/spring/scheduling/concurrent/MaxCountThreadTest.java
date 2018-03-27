package com.feilong.spring.scheduling.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class MaxCountThreadTest extends Thread{

    private static final AtomicInteger count = new AtomicInteger();

    public static void main(String[] args){
        while (true)
            (new MaxCountThreadTest()).start();

    }

    //---------------------------------------------------------------

    @Override
    public void run(){
        System.out.println(count.incrementAndGet());

        while (true){
            try{
                Thread.sleep(Integer.MAX_VALUE);
            }catch (InterruptedException e){
                break;
            }
        }
    }
}