package com.xn.rxjava.api;


import com.xn.rxjava.entity.Category;
import com.xn.rxjava.entity.ItemList;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by xn068074 on 2018/9/17.
 */

public interface ApiService {

    @GET("v3/queries/hot")
    Observable<List<String>>getHotPhrase();

    @GET("v1/search")
    Observable<ItemList> searchWithKeywords(@Query("num") int num, @Query("start") int start,@Query("query") String query);

    @GET("v1/search")
    Observable<ItemList> searchWithKeywords(@QueryMap Map<String,String> params);


    @GET("v4/categories")
    Observable<List<Category>> fetchCategory();
}
