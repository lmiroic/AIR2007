package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import air.foi.hr.core.database.MyDatabase;
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

public class PromjenaLozinkeViewModel extends ViewModel {
    private Context context;
    private MyDatabase baza;


    public void konstruktor(Context context) {
        this.context = context;
        this.baza = MyDatabase.getInstance(this.context);
    }

    public void PromjeniLozinku(int id, String novaLozinka, String novaLozinkaPonovljena, String staraLozinka, final FragmentManager fragmentManager) {
        if (novaLozinka != "" && novaLozinkaPonovljena != "" && staraLozinka != "") {
            if (!novaLozinka.equals(novaLozinkaPonovljena)) {
                PrikaziObavijest("Nova lozinka se ne podudara sa ponovljenom!");
            } else {
                String lokalnaStaraLozinka = Sesija.getInstance().getKorisnik().getLozinka();
                String hashStareLozinke;
                final String hashLozinka;
                try {
                    hashStareLozinke = HashiranjeLozinke.HashirajLozinku(staraLozinka);
                    hashLozinka = HashiranjeLozinke.HashirajLozinku(novaLozinka);
                } catch (Exception e) {
                    return;
                }
                if (lokalnaStaraLozinka.equals(hashStareLozinke)) {
                    Retrofit r = RetrofitInstance.getInstance();
                    RestApiImplementor api = r.create(RestApiImplementor.class);
                    Call<Void> pozivUnosa = api.AzurirajKorisnika(id, RequestBody.create(MediaType.parse("text/plain"), "lozinka"), RequestBody.create(MediaType.parse("text/plain"), hashLozinka));
                    pozivUnosa.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.e("Korisnik", "Uspješna promjena lozinke");
                            PrikaziObavijest("Uspješna promjena lozinke!");
                            Sesija.getInstance().getKorisnik().setLozinka(hashLozinka);
                            FragmentSwitcher.ShowFragment(FragmentName.POSTAVKE, fragmentManager);
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Korisnik", "Neuspješna promjena lozinke");
                        }
                    });
                } else {
                    PrikaziObavijest("Unesena stara lozinka nije ispravna!");
                }
            }
        } else {
            PrikaziObavijest("Niste unijeli sve parametre!");
        }
    }

    private void PrikaziObavijest(String poruka) {
        Toast.makeText(context, poruka, Toast.LENGTH_LONG).show();
    }
}
