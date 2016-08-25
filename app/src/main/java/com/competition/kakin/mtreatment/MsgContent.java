package com.competition.kakin.mtreatment;

import java.util.Map;

/**
 * Created by kakin on 2016/8/21.
 */
public class MsgContent {
    private String doctorName;
    private String msgDetail;
    private Map<String, Integer> msgTime;
    public MsgContent(){

    }
    public MsgContent(String doctorName, String msgDetail, Map<String, Integer> msgTime){
        this.doctorName = doctorName;
        this.msgDetail = msgDetail;
        this.msgTime = msgTime;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getMsgDetail() {
        return msgDetail;
    }

    public Map<String, Integer> getMsgTime() {
        return msgTime;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setMsgDetail(String msgDetail) {
        this.msgDetail = msgDetail;
    }

    public void setMsgTime(Map<String, Integer> msgTime) {
        this.msgTime = msgTime;
    }
}
