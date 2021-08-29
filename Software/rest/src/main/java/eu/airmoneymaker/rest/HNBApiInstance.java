package eu.airmoneymaker.rest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HNBApiInstance {
    private static Retrofit INSTANCE;
    private static final String BASE_URL="https://api.hnb.hr/tecajn/";

    public static Retrofit getInstance(){
        if(INSTANCE==null){
            synchronized (HNBApiInstance.class){
                if(INSTANCE==null){
                    INSTANCE=new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(new OkHttpClient())
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
