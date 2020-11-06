package air.foi.hr.moneymaker.fragmenti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.SplashScreenViewModel;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import retrofit2.Call;

public class SplashScreenFragment extends Fragment {

   private View view;
   private SplashScreenViewModel viewModel;

    public SplashScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_splash_screen, container, false);
        InicijalizacijaVarijabli();
        //viewModel.DohvatiSveKorisnike();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                FragmentSwitcher.ShowFragment(FragmentName.PRIJAVA, getFragmentManager());
            }
        }, 3500);

        //test
        //FragmentSwitcher.ShowFragment(FragmentName.KLASICNA_PRIJAVA, getFragmentManager());
        return view;
    }

    private void InicijalizacijaVarijabli() {
        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(SplashScreenViewModel.class);
        viewModel.konstruktor(getContext());
    }
}