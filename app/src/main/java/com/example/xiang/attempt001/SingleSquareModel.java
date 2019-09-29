package com.example.xiang.attempt001;

/**
 * Created by xiang on 2017-05-17.
 */

public class SingleSquareModel {
    private String squareName;
    private String squareUrl;
    private String squareDescription;
    private String squareImageName;
    //Constructors
    public SingleSquareModel()
    {

    }
    public SingleSquareModel(String name, String url, String imageName)
    {
        this.squareName = name;
        this.squareUrl = url;
        this.squareImageName = imageName;
    }
    //Functions
    public String getSquareUrl()
    {
        return squareUrl;
    }
    public void setSquareurl(String url)
    {
        this.squareUrl = url;
    }
    public String getSquareName()
    {
        return squareName;
    }
    public void setSquareName(String name)
    {
        this.squareName = name;
    }
    public String getSquareDescription()
    {
        return squareDescription;
    }
    public void setSquareDescription(String description)
    {
        this.squareDescription = description;
    }
    public String getSquareImageName(){return squareImageName;}
    public void setSquareImageName(String imageName){this.squareImageName = imageName;}
}
