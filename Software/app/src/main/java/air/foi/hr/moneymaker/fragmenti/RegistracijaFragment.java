package air.foi.hr.moneymaker.fragmenti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import air.foi.hr.core.database.dao.KorisnikDAO;
import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.manager.HashiranjeLozinke;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import air.foi.hr.moneymaker.modul.prijava.GoogleSignIn;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RegistracijaFragment extends Fragment {
    public View view;
    private EditText Ime;
    private EditText Prezime;
    private EditText Email;
    private EditText Lozinka;
    private Button btnRegistracija;
    private static GoogleSignInAccount account;
    public RegistracijaFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_registracija, container, false);
        InicijalizacijaVarijabli();
        return view;
    }
    private void RegistrirajKorisnika(String ime, String prezime, String email, String lozinka){
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
                    FragmentSwitcher.ShowFragment(FragmentName.PRIJAVA,getFragmentManager());
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

    private void InicijalizacijaVarijabli(){
        Ime=view.findViewById(R.id.editTextIme);
        Prezime=view.findViewById(R.id.editTextPrezime);
        Email=view.findViewById(R.id.editTextEmail);
        Lozinka=view.findViewById(R.id.editTextLozinka);
        btnRegistracija=view.findViewById(R.id.btnRegistracija);
        btnRegistracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrirajKorisnika(Ime.getText().toString(),Prezime.getText().toString(),Email.getText().toString(),Lozinka.getText().toString());
            }
        });
    }
    private void PrikaziObavijest(String poruka){
        Toast.makeText(getContext(),poruka,Toast.LENGTH_LONG).show();
    }
}