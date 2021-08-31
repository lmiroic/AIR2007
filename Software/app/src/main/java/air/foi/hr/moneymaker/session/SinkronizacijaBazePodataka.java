package air.foi.hr.moneymaker.session;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Ciljevi;
import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SinkronizacijaBazePodataka {
    private Context context;
    public SinkronizacijaBazePodataka(Context context) {
        this.context=context;
    }

    public void sinkroniziraj(String email){
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
                if(korisnikPrijave!=null){
                    Log.e("Sync", korisnikPrijave.toString());
                    MyDatabase.getInstance(context).getRacunDAO().IzbrisiSveRacune();
                    MyDatabase.getInstance(context).getTransakcijaDAO().IzbrisiSveTransakcije();
                    MyDatabase.getInstance(context).getCiljeviDAO().IzbrisiSveCiljeve();
                    UpravljanjeKorisnikomULokalnojBazi(korisnikPrijave);
                }


            }

            @Override
            public void onFailure(Call<List<Korisnik>> call, Throwable t) {

            }
        });
    }
    private void UpravljanjeKorisnikomULokalnojBazi(Korisnik korisnik) {
        try {
            if (ProvjeraPostojiLiKorisnik(korisnik)) {
                Korisnik k = MyDatabase.getInstance(context).getKorisnikDAO().DohvatiKorisnika(korisnik.getId());
                Sesija.getInstance().setKorisnik(korisnik);
                dohvatiRacune(Sesija.getInstance().getKorisnik().getId());
                dohvatiSveTransakcijeKorisnika(Sesija.getInstance().getKorisnik().getId());
                dohvatiCiljeveKorisnika(Sesija.getInstance().getKorisnik().getId());
            } else {
                MyDatabase.getInstance(context).getKorisnikDAO().UnosKorisnika(korisnik);
                Sesija.getInstance().setKorisnik(korisnik);
                dohvatiRacune(Sesija.getInstance().getKorisnik().getId());
                dohvatiSveTransakcijeKorisnika(Sesija.getInstance().getKorisnik().getId());
                dohvatiCiljeveKorisnika(Sesija.getInstance().getKorisnik().getId());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void dohvatiCiljeveKorisnika(int korisnik) {
        if (!ProvjeraPostojanostiCiljevaUBazi()) {
            Retrofit r = RetrofitInstance.getInstance();
            RestApiImplementor api = r.create(RestApiImplementor.class);
            final Call<List<Ciljevi>> pozivUnosa = api.DohvatiKorisnikoveCiljeve(korisnik);
            pozivUnosa.enqueue(new Callback<List<Ciljevi>>() {
                @Override
                public void onResponse(Call<List<Ciljevi>> call, Response<List<Ciljevi>> response) {
                    for (Ciljevi cilj : response.body()) {
                        MyDatabase.getInstance(context).getCiljeviDAO().UnosCilja(cilj);
                    }
                }

                @Override
                public void onFailure(Call<List<Ciljevi>> call, Throwable t) {

                }
            });
        }
    }

    private void dohvatiRacune(int korisnik) {
        if (!ProvjeraPostojanostiRacunaUBazi()) {
            Retrofit r = RetrofitInstance.getInstance();
            RestApiImplementor api = r.create(RestApiImplementor.class);
            final Call<List<Racun>> pozivUnosa = api.DohvatiKorisnikoveRacune(korisnik);
            pozivUnosa.enqueue(new Callback<List<Racun>>() {
                @Override
                public void onResponse(Call<List<Racun>> call, Response<List<Racun>> response) {
                    for (Racun racun : response.body()) {
                        MyDatabase.getInstance(context).getRacunDAO().UnosRacuna(racun);
                    }
                }

                @Override
                public void onFailure(Call<List<Racun>> call, Throwable t) {

                }
            });
        }
    }

    private void dohvatiSveTransakcijeKorisnika(int korisnik) {
        if (!ProvjeraPostojanostiTransakcijaUBazi()) {
            Retrofit r = RetrofitInstance.getInstance();
            RestApiImplementor api = r.create(RestApiImplementor.class);
            final Call<List<Transakcija>> pozivUnosa = api.DohvatiKorisnikoveTransakcije(korisnik);
            pozivUnosa.enqueue(new Callback<List<Transakcija>>() {
                @Override
                public void onResponse(Call<List<Transakcija>> call, Response<List<Transakcija>> response) {
                    for (Transakcija t : response.body()) {
                        MyDatabase.getInstance(context).getTransakcijaDAO().UnosTransakcije(t);
                    }
                }

                @Override
                public void onFailure(Call<List<Transakcija>> call, Throwable t) {

                }
            });
        }
    }

    private boolean ProvjeraPostojiLiKorisnik(Korisnik korisnik) throws Exception {
        Korisnik k = MyDatabase.getInstance(context).getKorisnikDAO().DohvatiKorisnikaLogin(korisnik.getEmail(), korisnik.getLozinka());
        if (k != null) {
            return true;
        }
        return false;
    }

    private boolean ProvjeraPostojanostiRacunaUBazi() {
        return MyDatabase.getInstance(context).getRacunDAO().DohvatiSveRacune().size() > 0 ? true : false;
    }

    private boolean ProvjeraPostojanostiTransakcijaUBazi() {
        return MyDatabase.getInstance(context).getTransakcijaDAO().DohvatiSveTransakcije().size() > 0 ? true : false;
    }

    private boolean ProvjeraPostojanostiCiljevaUBazi() {
        return MyDatabase.getInstance(context).getCiljeviDAO().DohvatiSveCiljeve().size() > 0 ? true : false;
    }

}
