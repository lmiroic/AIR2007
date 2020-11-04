package air.foi.hr.moneymaker.fragmenti;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import java.util.List;

import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.MainActivity;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.SecondFragment;
import air.foi.hr.moneymaker.ViewModel.PrijavaViewModel;
import air.foi.hr.moneymaker.ViewModel.SplashScreenViewModel;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PrijavaFragment extends Fragment {

    EditText email, lozinka;
    Button default_login;

    private View view;
    private PrijavaViewModel viewModel;

    public PrijavaFragment() {
    }
    Button signin;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_prijava, container, false);
        InicijalizacijaVarijabli();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
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

        signin=view.findViewById(R.id.sign_in_button);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.sign_in_button:
                        signIn();
                        break;

                }
            }
        });
        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(PrijavaViewModel.class);
        viewModel.konstruktor(getContext());
    }

    private void signIn() {
        Intent i= mGoogleSignInClient.getSignInIntent();
        startActivityForResult(i,0);
        
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 0) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            FragmentSwitcher.ShowFragment(FragmentName.SPLASH_SCREEN, getFragmentManager());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

        }
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
         */
        Call<List<Korisnik>>pozivUnosa = api.DohvatiSveKorisnike();
        pozivUnosa.enqueue(new Callback<List<Korisnik>>() {
            @Override
            public void onResponse(Call<List<Korisnik>> call, final Response<List<Korisnik>> response) {
                List<Korisnik> loginResponse = response.body();

                if(response.isSuccessful() && loginResponse != null){
                    for (int i = 0; i< loginResponse.size(); i++){
                        Korisnik korisnik = loginResponse.get(i);

                        if(korisnik.getEmail().equals(emailS) && korisnik.getLozinka().equals(lozinkaS)){
                            Log.e("Korisnik","getId -- > " + korisnik.getId());
                            Log.e("Korisnik","getIme -- > " + korisnik.getIme());
                            Log.e("Korisnik","getPrezime -- > " + korisnik.getPrezime());
                            Log.e("Korisnik","getEmail -- > " + korisnik.getEmail());
                            //Log.e("Korisnik","getLozinka -- > " + korisnik.getLozinka());
                            Log.e("Korisnik","getGoogle_ID -- > " + korisnik.getGoogle_ID());
                            Log.e("Korisnik", "Uspješna prijava!");
                            break;

                        }
                        else {
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