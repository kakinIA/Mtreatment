package com.competition.kakin.mtreatment;

/**
 * Created by kakin on 2016/8/8.
 */
public class PlanContent {
    private int imgId;
    private String planTitle;
    private String period;
    private String introduce;
    public PlanContent(){

    }
    public PlanContent(int imgId, String planTitle, String period, String introduce){
        this.imgId = imgId;
        this.planTitle = planTitle;
        this.period = period;
        this.introduce = introduce;
    }

    public int getImgId() {
        return imgId;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public String getPeriod() {
        return period;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
