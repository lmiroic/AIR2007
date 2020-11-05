package air.foi.hr.moneymaker.fragmenti;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.KlasicnaPrijavaViewModel;
import air.foi.hr.moneymaker.ViewModel.SplashScreenViewModel;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class KlasicnaPrijavaFragment extends Fragment {

    EditText email, lozinka;
    Button default_login;

    private View view;
    private KlasicnaPrijavaViewModel viewModel;

    public KlasicnaPrijavaFragment(){
        //empty
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_prijava, container, false);
        InicijalizacijaVarijabli();

        return view;
    }

    private void InicijalizacijaVarijabli() {

        email=(EditText) view.findViewById(R.id.editTextTextEmailAddress);
        lozinka=(EditText) view.findViewById(R.id.editTextTextPassword);
        default_login=(Button) view.findViewById(R.id.btnLogin);
        default_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnLogin:
                        login();
                        break;
                }
            }
        });

        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(KlasicnaPrijavaViewModel.class);
        viewModel.konstruktor(getContext());
    }

    //default login
    public void login(){
        Retrofit r= RetrofitInstance.getInstance();
        RestApiImplementor api=r.create(RestApiImplementor.class);

        final String emailS = email.getText().toString();
        final String lozinkaS = lozinka.getText().toString();

        if (emailS.equals("")){
            email.setError("Unesite email");
        }
        if (emailS.equals("")){
            lozinka.setError("Unesite lozinku");
        }
        /*
        TODO:
          kriptiranje lozinke,
          poželjno lokalno kriptiranje prije slanja na rest a u bazi se sprema kriptirana
          rest odrađiva autentikaciju
          dohvat samo jednog korisnika a ne svih preko emaila i unesene lozinke
          (osposobit DohvatiKorisnikaLogin() )
          za sad ostaviti ovo ispod, uspješna prijava se može provjeriti u logu
          set intent

         */
        Call<List<Korisnik>> pozivUnosa = api.DohvatiKorisnikaLogin(emailS,lozinkaS);
        pozivUnosa.enqueue(new Callback<List<Korisnik>>() {
            @Override
            public void onResponse(Call<List<Korisnik>> call, final Response<List<Korisnik>> response) {
                List<Korisnik> loginResponse = response.body();

                if(response.isSuccessful() && loginResponse != null){

                        for (int i=0; i<loginResponse.size(); i++) {
                            Korisnik korisnik = loginResponse.get(i);

                            if (korisnik.getEmail().equals(emailS) && korisnik.getLozinka().equals(lozinkaS)) {
                                Log.e("Korisnik", "getId -- > " + korisnik.getId());
                                Log.e("Korisnik", "getIme -- > " + korisnik.getIme());
                                Log.e("Korisnik", "getPrezime -- > " + korisnik.getPrezime());
                                Log.e("Korisnik", "getEmail -- > " + korisnik.getEmail());
                                Log.e("Korisnik","getLozinka -- > " + korisnik.getLozinka());
                                Log.e("Korisnik", "getGoogle_ID -- > " + korisnik.getGoogle_ID());
                                Log.e("Korisnik", "Uspješna prijava!");
                            } else {
                                Log.e("Korisnik", "Neuspješna prijava, korisnik ne postoji.");
                            }
                        }

                    //prebacit se na drugi fragment
                    FragmentSwitcher.ShowFragment(FragmentName.SPLASH_SCREEN, getFragmentManager());
                }
            }

            @Override
            public void onFailure(Call<List<Korisnik>> call, Throwable t) {
                Log.e("Korisnik",t.getMessage());
            }
        });
    }
}
