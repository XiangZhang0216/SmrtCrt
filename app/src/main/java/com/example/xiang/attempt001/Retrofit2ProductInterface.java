package com.example.xiang.attempt001;

/**
 * Created by xiang on 2017-07-26.
 */

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface Retrofit2ProductInterface {
    @GET("products/{category}")
    Observable<Product[]> getProductByCategory(@Path("category")  String category);

    @GET("products/{itemName}")
    Observable<Product[]>assortedProducts(@Path("itemName")String itenname);
    //For query of individual item, use indie = true, categorized = false, field = _id or QRCode, value.
    //For query of array of assorted items, use indie = false, categorized = false, field = "", value = ""
    //For query of array of categorized items, use indie = false, categorized = true, field = "category", value

}
