package com.feilong.spring.namespace;

import java.util.Date;

public class LogBean{

    private boolean isPrintTiem = false;

    private String  company     = "";

    //---------------------------------------------------------------

    public LogBean(boolean isPrintTime){
        this.isPrintTiem = isPrintTime;
    }

    public void print(String log){
        if (this.isPrintTiem)
            System.out.println(this.company + new Date() + ":" + log);
        else
            System.out.println(this.company + ":" + log);
    }

    public String getCompany(){
        return company;
    }

    public void setCompany(String company){
        this.company = company;
    }

    public boolean isPrintTiem(){
        return isPrintTiem;
    }
}