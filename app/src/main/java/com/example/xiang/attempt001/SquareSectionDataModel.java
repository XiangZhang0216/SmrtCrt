package com.example.xiang.attempt001;

import java.util.ArrayList;
/**
 * Created by xiang on 2017-05-17.
 */

public class SquareSectionDataModel {
    private String squareHeaderTitle;
    private ArrayList<SingleSquareModel> allItemsInSection;
    //Constructors
    public SquareSectionDataModel()
    {

    }
    public SquareSectionDataModel(String headerTitle, ArrayList<SingleSquareModel> allItemsInSection)
    {
        this.squareHeaderTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }
    //Functions
    public String getSquareHeaderTitle()
    {
        return squareHeaderTitle;
    }
    public void setSquareHeaderTitle(String headerTitle)
    {
        this.squareHeaderTitle = headerTitle;
    }
    public ArrayList<SingleSquareModel> getAllItemsInSection()
    {
        return allItemsInSection;
    }
    public void setAllItemsInSection(ArrayList<SingleSquareModel> allItemsInSection)
    {
        this.allItemsInSection = allItemsInSection;
    }
}
