package air.foi.hr.moneymaker.fragmenti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.SplashScreenViewModel;

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
        return view;
    }

    private void InicijalizacijaVarijabli() {
        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(SplashScreenViewModel.class);
        viewModel.konstruktor(getContext());
    }
}