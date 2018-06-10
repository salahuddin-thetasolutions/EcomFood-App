package com.food.ecom.Http;



/**
 * Created by SALAHUDIN` on 3/28/2018.
 */

public class ApiUtils {
    private ApiUtils() {}
    public static APIService getAPIService(String BaseUrl) {
       return RetrofitClient.GetClient(BaseUrl).create(APIService.class);
    }
}
