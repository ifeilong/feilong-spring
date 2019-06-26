package cn.hurraysoft.HelloWorldTask;

import java.util.Timer;

public class OneOffScheduling{

    public static void main(String[] args){
        Timer timer = new Timer();
        timer.schedule(new HelloWorldTask(), 1000);
    }
}
