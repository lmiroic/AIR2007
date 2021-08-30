package air.foi.hr.moneymaker.session;

import android.util.Log;

import java.util.List;

import air.foi.hr.core.entiteti.Korisnik;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SinkronizacijaBazePodataka {

    public static void sinkroniziraj(String email){
        Retrofit r = RetrofitInstance.getInstance();
        RestApiImplementor rest = r.create(RestApiImplementor.class);
        rest.DohvatiSveKorisnike().enqueue(new Callback<List<Korisnik>>() {
            @Override
            public void onResponse(Call<List<Korisnik>> call, Response<List<Korisnik>> response) {
                final List<Korisnik> korisnici = response.body();
                Korisnik korisnikPrijave = null;
                for(Korisnik k : korisnici){
                    if(k.getEmail().equalsIgnoreCase(email)) {
                        korisnikPrijave = k;
                        break;
                    }
                }
                if(korisnikPrijave!=null)
                    Log.e("Sync", korisnikPrijave.toString());
            }

            @Override
            public void onFailure(Call<List<Korisnik>> call, Throwable t) {

            }
        });
    }

}
