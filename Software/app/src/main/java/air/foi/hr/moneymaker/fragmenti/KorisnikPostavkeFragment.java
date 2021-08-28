package air.foi.hr.moneymaker.fragmenti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.KorisnikPostavkeViewModel;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;

public class KorisnikPostavkeFragment extends Fragment {
    private KorisnikPostavkeViewModel viewModel;
    private Button btnPromjeniLozinku;
    private Button btnPromjeniValutu;
    private Button btnCiljevi;
    private ImageButton btnBack;

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

        btnPromjeniLozinku=view.findViewById(R.id.btnPromjeniLozinku);
        btnPromjeniLozinku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.PROMJENA_LOZINKE,getFragmentManager());
            }
        });

        btnPromjeniValutu=view.findViewById(R.id.btnPromijeniValutu);
        btnPromjeniValutu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.PROMJENA_VALUTE,getFragmentManager());
            }
        });

        btnBack=view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.HOME,getFragmentManager());
            }
        });

        btnCiljevi=view.findViewById(R.id.btnCiljevi);
        btnCiljevi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.CILJEVI,getFragmentManager());
            }
        });

        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(KorisnikPostavkeViewModel.class);
        viewModel.konstruktor(getContext());

    }
}
