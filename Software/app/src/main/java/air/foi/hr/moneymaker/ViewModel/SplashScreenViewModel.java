package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Korisnik;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashScreenViewModel extends ViewModel {
    private Context context;
    private MyDatabase baza;

    public void konstruktor(Context context){
        this.context=context;
        this.baza=MyDatabase.getInstance(this.context);
    }
    public void DohvatiSveKorisnike(){
        Retrofit r= RetrofitInstance.getInstance();
        RestApiImplementor api=r.create(RestApiImplementor.class);
        Call<Void>pozivUnosa=api.UnesiKorisnika("Antonio","Hip","1231313131","aHip@gmail.com","aHip");
        pozivUnosa.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.e("Korisnik","Korisnik unesen");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Korisnik",t.getMessage());
            }
        });




    }

}
