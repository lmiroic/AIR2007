package air.foi.hr.moneymaker.modul.prijava;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.manager.HashiranjeLozinke;
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

    public KlasicnaPrijava(String email, String lozinka, Context context) {
        this.email = email;
        this.lozinka = lozinka;
        this.context = context;
    }

    @Override
    public void PrijaviKorisnika(final FragmentManager fragmentManager) {
        Retrofit r = RetrofitInstance.getInstance();
        RestApiImplementor api = r.create(RestApiImplementor.class);
        Call<List<Korisnik>> pozivUnosa = api.DohvatiKorisnikaLogin(RequestBody.create(MediaType.parse("text/plain"), email), RequestBody.create(MediaType.parse("text/plain"), HashiranjeLozinke.HashirajLozinku(lozinka)));
        pozivUnosa.enqueue(new Callback<List<Korisnik>>() {
            @Override
            public void onResponse(Call<List<Korisnik>> call, final Response<List<Korisnik>> response) {
                try {
                    Korisnik DohvacenKorisnik = response.body().get(0);
                    if (response.isSuccessful() && DohvacenKorisnik != null) {
                        if (DohvacenKorisnik.getEmail().equals(email) && DohvacenKorisnik.getLozinka().equals(HashiranjeLozinke.HashirajLozinku(lozinka))) {
                            Log.e("Korisnik", "Uspješna prijava!");
                            UpravljanjeKorisnikomULokalnojBazi(DohvacenKorisnik, fragmentManager);
                        }
                    }
                } catch (Exception e) {
                    Log.e("Prijava", "Neuspješna prijava!");
                    Toast.makeText(context, "Neuspješna prijava!", Toast.LENGTH_LONG).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<List<Korisnik>> call, Throwable t) {
                Log.e("Korisnik", t.getMessage());
            }
        });
    }

    private void ZapisiKorisnikaULokalnuBazu(Korisnik korisnik) {

        MyDatabase.getInstance(context).getKorisnikDAO().UnosKorisnika(korisnik);
    }

    private boolean ProvjeraPostojiLiKorisnik(Korisnik korisnik) throws Exception {
        Korisnik k = MyDatabase.getInstance(context).getKorisnikDAO().DohvatiKorisnikaLogin(korisnik.getEmail(), korisnik.getLozinka());
        if (k != null) {
            return true;
        }
        return false;
    }

    private Korisnik DohvatiKorisnika(Korisnik korisnik) throws Exception {
        Korisnik k = MyDatabase.getInstance(context).getKorisnikDAO().DohvatiKorisnikaLogin(korisnik.getEmail(), korisnik.getLozinka());
        if (k != null) {
            return k;
        }
        throw new Exception("Ne mogu dohvatiti korisnika u lokalnoj bazi!");
    }

    private void UpravljanjeKorisnikomULokalnojBazi(Korisnik korisnik, FragmentManager fragmentManager) {
        try {
            if (ProvjeraPostojiLiKorisnik(korisnik)) {
                Korisnik k = DohvatiKorisnika(korisnik);
                Sesija.getInstance().setKorisnik(korisnik);
                dohvatiRacune(Sesija.getInstance().getKorisnik().getId());
                dohvatiSveTransakcijeKorisnika(Sesija.getInstance().getKorisnik().getId());
                FragmentSwitcher.ShowFragment(FragmentName.HOME, fragmentManager);
            } else {
                ZapisiKorisnikaULokalnuBazu(korisnik);
                Sesija.getInstance().setKorisnik(korisnik);
                dohvatiRacune(Sesija.getInstance().getKorisnik().getId());
                dohvatiSveTransakcijeKorisnika(Sesija.getInstance().getKorisnik().getId());
                FragmentSwitcher.ShowFragment(FragmentName.HOME, fragmentManager);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
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

    private boolean ProvjeraPostojanostiRacunaUBazi() {
        return MyDatabase.getInstance(context).getRacunDAO().DohvatiSveRacune().size() > 0 ? true : false;
    }

    private boolean ProvjeraPostojanostiTransakcijaUBazi() {
        return MyDatabase.getInstance(context).getTransakcijaDAO().DohvatiSveTransakcije().size() > 0 ? true : false;
    }

}
