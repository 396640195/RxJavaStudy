package com.xn.rxjava.api;

import android.util.Log;

import com.xn.rxjava.entity.Category;
import com.xn.rxjava.entity.ItemList;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xn068074 on 2018/9/17.
 */

public class RetrofitManager {
   public static final String BASE_URL = "http://baobab.kaiyanapp.com/api/";

   public static Retrofit build(){

      Retrofit retrofit = new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .client(getOkHttpClient())
              .addConverterFactory(GsonConverterFactory.create())
              .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
              .build();

      return retrofit;

   }

   private static OkHttpClient getOkHttpClient(){

      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message->{
         try {
            String text = URLDecoder.decode(message, "utf-8");
            Log.e("OKHttp-----", text);
         } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("OKHttp-----", message);
         }
      });
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      builder.connectTimeout(10, TimeUnit.SECONDS);//连接超时时间
      builder.writeTimeout(10,TimeUnit.SECONDS);//写操作 超时时间
      builder.readTimeout(10,TimeUnit.SECONDS);//读操作超时时间
      builder.addInterceptor(interceptor);

      return builder.build();
   }

   /**
    * 获取热词
    * @return
    */
   public static Observable<List<String>> getHotPhrase(){

         return RetrofitManager
                 .build()
                 .create(ApiService.class)
                 .getHotPhrase()
                 .subscribeOn(Schedulers.newThread())
                 .observeOn(AndroidSchedulers.mainThread());

   }

   /**
    * 根据关键词搜索视频
    */
   public static Observable<ItemList> searchVideoInfo(int num,int start,String query){
      Map<String,String> params = new HashMap<>();
      params.put("num",String.valueOf(num));
      params.put("start",String.valueOf(start));
      params.put("query",query);
      return RetrofitManager
              .build()
              .create(ApiService.class)
              .searchWithKeywords(params)
              //.searchWithKeywords(num,start,query)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread());
   }

   public static Observable<List<Category>> fetchCategory(){

      return  RetrofitManager
              .build()
              .create(ApiService.class)
              .fetchCategory()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread());
   }
}
