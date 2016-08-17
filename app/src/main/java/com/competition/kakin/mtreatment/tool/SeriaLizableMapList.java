package com.competition.kakin.mtreatment.tool;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by kakin on 2016/8/13.
 */
public class SeriaLizableMapList implements Serializable {
    private List<Map<String, Integer>> mapList;

    public List<Map<String, Integer>> getMapList() {
        return mapList;
    }

    public void setMapList(List<Map<String, Integer>> mapList) {
        this.mapList = mapList;
    }
}
