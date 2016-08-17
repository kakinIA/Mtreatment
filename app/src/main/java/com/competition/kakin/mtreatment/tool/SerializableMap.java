package com.competition.kakin.mtreatment.tool;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by kakin on 2016/8/10.
 */
public class SerializableMap implements Serializable {
    private Map<String, Integer> timeMap;
    public Map<String, Integer> getTimeMap(){
        return timeMap;
    }
    public void setMap(Map<String, Integer> map) {
        this.timeMap = map;
    }
}
