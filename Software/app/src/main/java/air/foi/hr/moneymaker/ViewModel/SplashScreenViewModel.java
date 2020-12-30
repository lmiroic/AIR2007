package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Valuta;
import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.core.modul.racuni.RacuniImplementor;
import air.foi.hr.moneymaker.session.Sesija;
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
    private boolean ProvjeraPostojanostKategorijaUBazi(){
        return MyDatabase.getInstance(context).getKategorijaTransakcijeDAO().DohvatiSveKategorijeTransakcije().size()>0?true:false;
    }
    public void NapuniBazu(){
        if(!ProvjeraPostojanostKategorijaUBazi()){
            Retrofit r= RetrofitInstance.getInstance();
            RestApiImplementor api=r.create(RestApiImplementor.class);
            final Call<List<KategorijaTransakcije>> pozivUnosa = api.DohvatiSveKategorijeTransakcije();
            pozivUnosa.enqueue(new Callback<List<KategorijaTransakcije>>() {
                @Override
                public void onResponse(Call<List<KategorijaTransakcije>> call, Response<List<KategorijaTransakcije>> response) {
                    for(KategorijaTransakcije kt: response.body()){
                        MyDatabase.getInstance(context).getKategorijaTransakcijeDAO().UnosKategorijeTransakcije(kt);
                    }
                }

                @Override
                public void onFailure(Call<List<KategorijaTransakcije>> call, Throwable t) {
                }
            });
        }
        if(!ProvjeraPostojanostiRacunaUBazi()){
            Retrofit r= RetrofitInstance.getInstance();
            RestApiImplementor api=r.create(RestApiImplementor.class);
            final Call<List<Racun>> pozivUnosa = api.DohvatiSveRacune();
            pozivUnosa.enqueue(new Callback<List<Racun>>() {
                @Override
                public void onResponse(Call<List<Racun>> call, Response<List<Racun>> response) {
                    for(Racun racun: response.body()){
                        MyDatabase.getInstance(context).getRacunDAO().UnosRacuna(racun);
                    }
                }

                @Override
                public void onFailure(Call<List<Racun>> call, Throwable t) {

                }
            });
        }
        if(!ProvjeraPostojanostiValuteUBazi()){
            Valuta valuta = new Valuta();
            valuta.setNaziv("HRK");
            valuta.setTecaj(Float.parseFloat("1.00"));
            Valuta valuta1 = new Valuta();
            valuta1.setNaziv("EUR");
            valuta1.setTecaj(Float.parseFloat("7.50"));
            Valuta valuta2 = new Valuta();
            valuta2.setNaziv("USD");
            valuta2.setTecaj(Float.parseFloat("6.12"));
            MyDatabase.getInstance(context).getValutaDAO().UnosValute(valuta,valuta1,valuta2);

        }
    }

    private boolean ProvjeraPostojanostiRacunaUBazi() {
        return MyDatabase.getInstance(context).getRacunDAO().DohvatiSveRacune().size()>0?true:false;
    }
    private boolean ProvjeraPostojanostiValuteUBazi(){
        return MyDatabase.getInstance(context).getValutaDAO().DohvatiSveValute().size()>0?true:false;
    }
}
