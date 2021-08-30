package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;
import java.util.Objects;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Ciljevi;
import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import air.foi.hr.moneymaker.modul.prijava.KlasicnaPrijava;
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

    public void konstruktor(Context context) {
        this.context = context;
        this.baza = MyDatabase.getInstance(this.context);
    }

    public void RegistracijaKorisnikaPutemGoogleAccounta(GoogleSignInAccount account, final FragmentManager fragmentManager) {
        if (account.getEmail().toString() != "" && account.getDisplayName().toString() != "" && account.getFamilyName().toString() != "" && account.getId().toString() != "") {
            String google_ID = account.getId();
            String email = account.getEmail();
            String ImeIPrezime = account.getDisplayName();
            String[] parts = ImeIPrezime.split("\\s");
            String Ime = parts[0];
            String Prezime = parts[1];
            String Lozinka = "";
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

    private void ZapisiKorisnikaULokalnuBazu(Korisnik korisnik) {

        MyDatabase.getInstance(context).getKorisnikDAO().UnosKorisnika(korisnik);
    }

    private boolean ProvjeraPostojanostiKorisnikaLokalno(GoogleSignInAccount account) {
        List<Korisnik> Korisnici = MyDatabase.getInstance(context).getKorisnikDAO().DohvatiSveKorisnike();
        for (Korisnik k : Korisnici) {
            if (k.getGoogle_ID().equals(account.getId().toString().trim())) {
                return true;
            }
        }
        return false;
    }

    public void ProvjeraPostojanostiKorisnika(final GoogleSignInAccount account, final FragmentManager fragmentManager) {
        Retrofit r = RetrofitInstance.getInstance();
        final RestApiImplementor api = r.create(RestApiImplementor.class);
        final Call<List<Korisnik>> DohvacanjeKorisnika = api.DohvatiSveKorisnike();
        DohvacanjeKorisnika.enqueue(new Callback<List<Korisnik>>() {
            @Override
            public void onResponse(Call<List<Korisnik>> call, Response<List<Korisnik>> response) {
                boolean status = false;
                for (Korisnik k : response.body()) {
                    if (k != null && k.getGoogle_ID() != null && k.getGoogle_ID().trim().equals(account.getId().toString().trim())) {
                        status = true;
                        break;
                    }
                }
                if (!status) {
                    RegistracijaKorisnikaPutemGoogleAccounta(account, fragmentManager);
                } else if (ProvjeraPostojanostiKorisnikaLokalno(account)) {
                    Sesija.getInstance().setKorisnik(MyDatabase.getInstance(context).getKorisnikDAO().DohvatiKorisnikaPoGoogleID(account.getId()));
                    dohvatiRacune(Sesija.getInstance().getKorisnik().getId());
                    dohvatiSveTransakcijeKorisnika(Sesija.getInstance().getKorisnik().getId());
                    dohvatiCiljeveKorisnika(Sesija.getInstance().getKorisnik().getId());
                    FragmentSwitcher.ShowFragment(FragmentName.HOME, fragmentManager);
                } else {
                    Call<List<Korisnik>> korisnik = api.DohvatiSveKorisnike();
                    korisnik.enqueue(new Callback<List<Korisnik>>() {
                        @Override
                        public void onResponse(Call<List<Korisnik>> call, Response<List<Korisnik>> response) {
                            for (Korisnik k : response.body()) {
                                if (k != null && k.getGoogle_ID() != null && k.getGoogle_ID().equals(account.getId().toString().trim())) {
                                    ZapisiKorisnikaULokalnuBazu(k);
                                    Sesija.getInstance().setKorisnik(MyDatabase.getInstance(context).getKorisnikDAO().DohvatiKorisnikaPoGoogleID(account.getId()));
                                    dohvatiRacune(Sesija.getInstance().getKorisnik().getId());
                                    dohvatiSveTransakcijeKorisnika(Sesija.getInstance().getKorisnik().getId());
                                    dohvatiCiljeveKorisnika(Sesija.getInstance().getKorisnik().getId());
                                    FragmentSwitcher.ShowFragment(FragmentName.HOME, fragmentManager);
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

    private void dohvatiRacune(int korisnik_id) {
        if (!ProvjeraPostojanostiRacunaUBazi()) {
            Retrofit r = RetrofitInstance.getInstance();
            RestApiImplementor api = r.create(RestApiImplementor.class);
            int korisnikId = Sesija.getInstance().getKorisnik().getId();
            final Call<List<Racun>> pozivUnosa = api.DohvatiKorisnikoveRacune(korisnikId);
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
                    if (response.body() != null) {
                        for (Transakcija t : response.body()) {
                            MyDatabase.getInstance(context).getTransakcijaDAO().UnosTransakcije(t);
                        }
                    } else
                        Log.e("Transakcija", "Nemaa");
                }

                @Override
                public void onFailure(Call<List<Transakcija>> call, Throwable t) {

                }
            });
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
