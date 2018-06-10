package com.food.ecom.Http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SALAHUDIN` on 3/28/2018.
 */

public class RetrofitClient {

    public static Retrofit oRetrofit=null;

    public static Retrofit GetClient(String BaseUrl){
        if(oRetrofit==null){
            oRetrofit=new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();
            //oCountryServices=retrofit.create(CountryServices.class);
        }
        return oRetrofit;
    }
}
