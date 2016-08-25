package com.competition.kakin.mtreatment.DbForMsg;

/**
 * Created by kakin on 2016/8/23.
 */
public class Record {
    private String illDetail;
    private String year;
    private String month;
    private String day;
    public Record(){

    }
    public Record(String illDetail, String year, String month, String day){
        this.illDetail = illDetail;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getIllDetail() {
        return illDetail;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public void setIllDetail(String illDetail) {
        this.illDetail = illDetail;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
