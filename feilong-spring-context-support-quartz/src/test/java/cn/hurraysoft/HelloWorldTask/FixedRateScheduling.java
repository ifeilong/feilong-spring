package cn.hurraysoft.HelloWorldTask;

import java.util.Timer;

public class FixedRateScheduling{

    //可以调度HelloWorldTask在程序开始后延迟1 s运行，接着每3s运行一次。
    public static void main(String[] args){
        Timer t = new Timer();
        // t.schedule(new HelloWorldTask(), 1000, 3000);
        t.scheduleAtFixedRate(new HelloWorldTask(), 1000, 3000);
    }
}
