package eu.airmoneymaker.rest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit INSTANCE;
    private static final String BASE_URL = "https://moneymakerair.tk/api/";

    public static Retrofit getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitInstance.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Retrofit.Builder()
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
