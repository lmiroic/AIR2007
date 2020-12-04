package air.foi.hr.moneymaker.fragmenti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.KorisnikPostavkeViewModel;

public class KorisnikPostavkeFragment extends Fragment {
    private KorisnikPostavkeViewModel viewModel;


    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_korisnik_postavke, container, false);
        InicijalizacijaVarijabli();
        return view;

    }
    private void InicijalizacijaVarijabli() {
        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(KorisnikPostavkeViewModel.class);
        viewModel.konstruktor(getContext());

    }
}
