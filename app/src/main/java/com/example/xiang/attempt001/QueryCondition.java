package com.example.xiang.attempt001;

import java.io.Serializable;

/**
 * Created by xiang on 2017-09-10.
 */

public class QueryCondition implements Serializable{
    private String conditionString;
    private Number returncycleint;

    public void setConditionString(String condition){this.conditionString = condition;}
    public void setReturncycleint(Number cycle){this.returncycleint = cycle;}
}
