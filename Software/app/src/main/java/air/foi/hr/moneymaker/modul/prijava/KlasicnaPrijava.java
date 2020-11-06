package air.foi.hr.moneymaker.modul.prijava;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import air.foi.hr.moneymaker.session.Sesija;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class KlasicnaPrijava implements IPrijava {
    private String email;
    private String lozinka;
    private Context context;
    public KlasicnaPrijava(String email,String lozinka,Context context) {
        this.email=email;
        this.lozinka=lozinka;
        this.context=context;
    }

    @Override
    public void PrijaviKorisnika(final FragmentManager fragmentManager) {
        Retrofit r= RetrofitInstance.getInstance();
        RestApiImplementor api=r.create(RestApiImplementor.class);
        Call<List<Korisnik>> pozivUnosa = api.DohvatiKorisnikaLogin(RequestBody.create(MediaType.parse("text/plain"), email),RequestBody.create(MediaType.parse("text/plain"), lozinka));
        pozivUnosa.enqueue(new Callback<List<Korisnik>>() {
            @Override
            public void onResponse(Call<List<Korisnik>> call, final Response<List<Korisnik>> response) {
                Korisnik DohvacenKorisnik = response.body().get(0);

                if(response.isSuccessful() && DohvacenKorisnik != null){
                        if (DohvacenKorisnik.getEmail().equals(email) && DohvacenKorisnik.getLozinka().equals(lozinka)) {
                            Log.e("Korisnik", "Uspješna prijava!");
                            UpravljanjeKorisnikomULokalnojBazi(DohvacenKorisnik,fragmentManager);
                        } else {
                            Log.e("Korisnik", "Neuspješna prijava, korisnik ne postoji.");
                        }
                }
            }

            @Override
            public void onFailure(Call<List<Korisnik>> call, Throwable t) {
                Log.e("Korisnik",t.getMessage());
            }
        });
    }
    private void ZapisiKorisnikaULokalnuBazu(Korisnik korisnik){

        MyDatabase.getInstance(context).getKorisnikDAO().UnosKorisnika(korisnik);
    }
    private boolean ProvjeraPostojiLiKorisnik(Korisnik korisnik) throws Exception {
        Korisnik k=MyDatabase.getInstance(context).getKorisnikDAO().DohvatiKorisnikaLogin(korisnik.getEmail(),korisnik.getLozinka());
        if(k!=null){
            return true;
        }
        return false;
    }
    private Korisnik DohvatiKorisnika(Korisnik korisnik) throws Exception {
        Korisnik k=MyDatabase.getInstance(context).getKorisnikDAO().DohvatiKorisnikaLogin(korisnik.getEmail(),korisnik.getLozinka());
        if(k!=null){
            return k;
        }
        throw new Exception("Ne mogu dohvatiti korisnika u lokalnoj bazi!");
    }
    private void UpravljanjeKorisnikomULokalnojBazi(Korisnik korisnik, FragmentManager fragmentManager){
        try {
            if (ProvjeraPostojiLiKorisnik(korisnik)){
                Korisnik k=DohvatiKorisnika(korisnik);
                Sesija.getInstance().setKorisnik(korisnik);
                FragmentSwitcher.ShowFragment(FragmentName.HOME, fragmentManager);
            }
            else{
                ZapisiKorisnikaULokalnuBazu(korisnik);
                Sesija.getInstance().setKorisnik(korisnik);
                FragmentSwitcher.ShowFragment(FragmentName.HOME, fragmentManager);
            }
        }
        catch(Exception exception){
            exception.printStackTrace();
        }
    }

}
