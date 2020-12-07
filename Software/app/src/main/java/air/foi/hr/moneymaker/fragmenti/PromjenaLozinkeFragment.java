package air.foi.hr.moneymaker.fragmenti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.PromjenaLozinkeViewModel;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;

public class PromjenaLozinkeFragment extends Fragment {
    private PromjenaLozinkeViewModel viewModel;
    private Button btnSpremiLozinku;
    private Button btnOdustani;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_promjeni_lozinku, container, false);
        InicijalizacijaVarijabli();
        return view;

    }
    private void InicijalizacijaVarijabli() {

        btnOdustani=view.findViewById(R.id.btnOdustani);
        btnOdustani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.POSTAVKE,getFragmentManager());
            }
        });

        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(PromjenaLozinkeViewModel.class);
        viewModel.konstruktor(getContext());

    }
}
