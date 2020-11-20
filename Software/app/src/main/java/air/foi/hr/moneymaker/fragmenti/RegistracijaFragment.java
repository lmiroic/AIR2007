package air.foi.hr.moneymaker.fragmenti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import air.foi.hr.moneymaker.ViewModel.RegistracijaFragmentViewModel;
import air.foi.hr.moneymaker.ViewModel.SplashScreenViewModel;
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
    private RegistracijaFragmentViewModel viewModel;
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



    private void InicijalizacijaVarijabli(){
        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(RegistracijaFragmentViewModel.class);
        viewModel.konstruktor(getContext());
        Ime=view.findViewById(R.id.editTextIme);
        Prezime=view.findViewById(R.id.editTextPrezime);
        Email=view.findViewById(R.id.editTextEmail);
        Lozinka=view.findViewById(R.id.editTextLozinka);
        btnRegistracija=view.findViewById(R.id.btnRegistracija);
        btnRegistracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.RegistrirajKorisnika(Ime.getText().toString(),Prezime.getText().toString(),Email.getText().toString(),Lozinka.getText().toString(),getFragmentManager());
            }
        });
    }
}