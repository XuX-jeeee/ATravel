package com.example.uitest.object;

import static java.lang.String.format;

import java.util.Date;

public class Listitem {
    private  Date mDate;
    private String date;
    private String time;
    private String content;

    public Listitem(String content) {
        this.date = getDate();
        this.time = getTime();
        this.content = content;
    }
    public Listitem(String date,String time,String content) {
        this.date = getDate();
        this.time = getTime();
        this.content = content;
    }

    public String getDate() {
        setDate(date);
        return format("%tF%n",mDate).toString();
    }

    public void setDate(String date) {
        mDate=new Date();this.date = date;
    }

    public String getTime() {
        setTime(date);
        return format("%tR",mDate).toString();
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
