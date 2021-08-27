package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;
import java.util.Objects;

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

public class PrijavaViewModel extends ViewModel {
    private Context context;
    private MyDatabase baza;
    private static GoogleSignInAccount account;

    public void konstruktor(Context context){
        this.context=context;
        this.baza=MyDatabase.getInstance(this.context);
    }
    public void RegistracijaKorisnikaPutemGoogleAccounta(GoogleSignInAccount account, final FragmentManager fragmentManager){
        if(account.getEmail().toString()!=""&&account.getDisplayName().toString()!=""&&account.getFamilyName().toString()!=""&&account.getId().toString()!=""){
            String google_ID=account.getId();
            String email=account.getEmail();
            String ImeIPrezime=account.getDisplayName();
            String[] parts = ImeIPrezime.split("\\s");
            String Ime = parts[0];
            String Prezime = parts[1];
            String Lozinka="";
            if (!ProvjeraPostojanostiKorisnikaLokalno(account)) {
                Retrofit r = RetrofitInstance.getInstance();
                RestApiImplementor api = r.create(RestApiImplementor.class);
                Call<Void> pozivUnosa = api.UnesiKorisnika(RequestBody.create(MediaType.parse("text/plain"), Ime), RequestBody.create(MediaType.parse("text/plain"), Prezime), RequestBody.create(MediaType.parse("text/plain"), google_ID), RequestBody.create(MediaType.parse("text/plain"), email), RequestBody.create(MediaType.parse("text/plain"), Lozinka));
                pozivUnosa.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e("Korisnik", "Uspjesna registracija");
                        FragmentSwitcher.ShowFragment(FragmentName.PRIJAVA, fragmentManager);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Korisnik", "Neuspjesna registracija");
                    }
                });
            }
        }
    }
    private void ZapisiKorisnikaULokalnuBazu(Korisnik korisnik){

        MyDatabase.getInstance(context).getKorisnikDAO().UnosKorisnika(korisnik);
    }
    private boolean ProvjeraPostojanostiKorisnikaLokalno(GoogleSignInAccount account){
        List<Korisnik>Korisnici=MyDatabase.getInstance(context).getKorisnikDAO().DohvatiSveKorisnike();
        for (Korisnik k:Korisnici){
            if (k.getGoogle_ID().equals(account.getId().toString().trim())){
                return true;
            }
        }
        return false;
    }
    public void ProvjeraPostojanostiKorisnika(final GoogleSignInAccount account, final FragmentManager fragmentManager){
        Retrofit r = RetrofitInstance.getInstance();
        final RestApiImplementor api = r.create(RestApiImplementor.class);
        final Call<List<Korisnik>> DohvacanjeKorisnika = api.DohvatiSveKorisnike();
        DohvacanjeKorisnika.enqueue(new Callback<List<Korisnik>>() {
            @Override
            public void onResponse(Call<List<Korisnik>> call, Response<List<Korisnik>> response) {
                boolean status=false;
                for (Korisnik k:response.body()){
                    if (k!=null && k.getGoogle_ID()!=null && k.getGoogle_ID().trim().equals(account.getId().toString().trim())){
                        status=true;
                        break;
                    }
                }
                if(!status){
                    RegistracijaKorisnikaPutemGoogleAccounta(account,fragmentManager);
                }
                else if (ProvjeraPostojanostiKorisnikaLokalno(account)){
                    Sesija.getInstance().setKorisnik(MyDatabase.getInstance(context).getKorisnikDAO().DohvatiKorisnikaPoGoogleID(account.getId()));
                    FragmentSwitcher.ShowFragment(FragmentName.HOME,fragmentManager);
                }
                else{
                    Call<List<Korisnik>>korisnik=api.DohvatiSveKorisnike();
                    korisnik.enqueue(new Callback<List<Korisnik>>() {
                        @Override
                        public void onResponse(Call<List<Korisnik>> call, Response<List<Korisnik>> response) {
                            for(Korisnik k:response.body()){
                                if(k!=null && k.getGoogle_ID()!=null && k.getGoogle_ID().equals(account.getId().toString().trim())){
                                    ZapisiKorisnikaULokalnuBazu(k);
                                    Sesija.getInstance().setKorisnik(MyDatabase.getInstance(context).getKorisnikDAO().DohvatiKorisnikaPoGoogleID(account.getId()));
                                    FragmentSwitcher.ShowFragment(FragmentName.HOME,fragmentManager);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Korisnik>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Korisnik>> call, Throwable t) {

            }
        });
    }
}
