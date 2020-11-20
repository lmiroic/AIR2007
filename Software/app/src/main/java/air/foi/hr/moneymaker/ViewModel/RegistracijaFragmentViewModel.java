package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.manager.HashiranjeLozinke;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistracijaFragmentViewModel extends ViewModel {
    private Context context;
    private MyDatabase baza;


    public void konstruktor(Context context){
        this.context=context;
        this.baza=MyDatabase.getInstance(this.context);
    }

    public void RegistrirajKorisnika(String ime, String prezime, String email, String lozinka, final FragmentManager fragmentManager){
        if(ime!=""&&prezime!=""&&email!=""&&lozinka!=""){
            String google_ID="";
            String hashLozinka;
            try {
                hashLozinka= HashiranjeLozinke.HashirajLozinku(lozinka);
            }
            catch (Exception e){
                return;
            }
            Retrofit r= RetrofitInstance.getInstance();
            RestApiImplementor api=r.create(RestApiImplementor.class);
            Call<Void> pozivUnosa = api.UnesiKorisnika(RequestBody.create(MediaType.parse("text/plain"), ime),RequestBody.create(MediaType.parse("text/plain"), prezime),RequestBody.create(MediaType.parse("text/plain"), google_ID),RequestBody.create(MediaType.parse("text/plain"), email),RequestBody.create(MediaType.parse("text/plain"), hashLozinka));
            pozivUnosa.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.e("Korisnik","Uspjesna registracija");
                    PrikaziObavijest("Uspješna registracija!");
                    FragmentSwitcher.ShowFragment(FragmentName.PRIJAVA,fragmentManager);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("Korisnik","Neuspjesna registracija");
                }
            });
        }
        else{
            PrikaziObavijest("Niste unijeli sve parametre!");
        }

    }

    private void PrikaziObavijest(String poruka){
        Toast.makeText(context,poruka,Toast.LENGTH_LONG).show();
    }
}
