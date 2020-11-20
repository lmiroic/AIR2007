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
import air.foi.hr.moneymaker.modul.prijava.AbstractionPrijava;
import air.foi.hr.moneymaker.modul.prijava.IPrijava;
import air.foi.hr.moneymaker.modul.prijava.KlasicnaPrijava;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PrijavaFragment extends Fragment {

    private View view;
    private PrijavaViewModel viewModel;
    private AbstractionPrijava abstractionPrijava;
    private Button btnKlasicnaPrijava;
    private Button btnRegistrirajSe;
    private EditText etEmail;
    private EditText etLozinka;


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
        signin=view.findViewById(R.id.sign_in_button);
        etEmail=view.findViewById(R.id.editTextTextEmailAddress);
        etLozinka=view.findViewById(R.id.editTextTextPassword);
        btnKlasicnaPrijava=view.findViewById(R.id.btnLogin);
        btnRegistrirajSe = view.findViewById(R.id.btnRegistrirajSe);
        final Fragment fragmentZaProslijedivanje=this;
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.sign_in_button:
                        abstractionPrijava=new AbstractionPrijava(new air.foi.hr.moneymaker.modul.prijava.GoogleSignIn(mGoogleSignInClient.getSignInIntent(),fragmentZaProslijedivanje,getContext()));
                        abstractionPrijava.PrijaviKorisnika(getFragmentManager());
                        break;
                }
            }
        });
        btnKlasicnaPrijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abstractionPrijava=new AbstractionPrijava(new KlasicnaPrijava(etEmail.getText().toString(),etLozinka.getText().toString(),getContext()));
                abstractionPrijava.PrijaviKorisnika(getFragmentManager());
            }
        });
        btnRegistrirajSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.REGISTRACIJA,getFragmentManager());
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
            FragmentSwitcher.ShowFragment(FragmentName.HOME, getFragmentManager());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

        }
    }
}