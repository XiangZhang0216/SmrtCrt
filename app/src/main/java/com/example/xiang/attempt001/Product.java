package com.example.xiang.attempt001;

import java.io.Serializable;

/**
 * Created by xiang on 2017-07-25.
 */

public class Product implements Serializable,Comparable<Product> {

    private String _id;
    private String owner;
    private String mainImg;
    private String ageGroup;
    private String brand;
    private String condition;
    private String gender;
    private String gtin;
    private String availability;
    private String link;
    private String mpn;
    private String productType;
    private String salePriceEffect;
    private String shipping;
    private String shippingWeight;
    private String itemName;
    private Number price;
    private Number salePrice;
    private String summary;
    private String description;
    private String QRCode;
    private String category;
    private String color;

    public void setItemName(String itemName){this.itemName = itemName;}
    public String getItemName(){return itemName;}
    public void setId(String id){this._id = id;}
    public String getId(){return _id;}
    public String getOwner(){return owner;}
    public String getMainImg(){return mainImg;}
    public String getAgeGroup(){return ageGroup;}
    public String getBrand(){return brand;}
    public String getCondition(){return condition;}
    public String getGender(){return gender;}
    public String getGtin(){return gtin;}
    public String getAvailability(){return availability;}
    public String getLink(){return link;}
    public String getMpn(){return mpn;}
    public String getProductType(){return productType;}
    public String getSalePriceEffect(){return salePriceEffect;}
    public String getShipping(){return shipping;}
    public String getShippingWeight(){return shippingWeight;}
    public String get_id(){return _id;}
    public Number getPrice(){return price;}
    public Number getSalePrice(){return salePrice;}
    public String getSummary(){return summary;}
    public String getDescription(){return description;}
    public String getQRCode(){return QRCode;}
    public String getCategory(){return category;}
    public String getColor(){return color;}
    public String getImageLink(){
     if(getMainImg()!=null)
     {
      if (!getMainImg().contains("default.png"))
      {return getMainImg();}
      return "https://image.shutterstock.com/z/stock-vector-no-image-available-sign-internet-web-icon-to-indicate-the-absence-of-image-until-it-will-be-261719003.jpg";
     }
     return "https://image.shutterstock.com/z/stock-vector-no-image-available-sign-internet-web-icon-to-indicate-the-absence-of-image-until-it-will-be-261719003.jpg";
    }
 @Override
 public int compareTo(Product anotherProduct) {
  return anotherProduct.getId().compareTo(this._id);
 }
}

