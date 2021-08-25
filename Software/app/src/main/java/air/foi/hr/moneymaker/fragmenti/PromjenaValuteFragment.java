package air.foi.hr.moneymaker.fragmenti;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Valuta;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.PromjenaValuteViewModel;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import eu.airmoneymaker.rest.HNBApiImplementor;
import eu.airmoneymaker.rest.HNBApiInstance;
import eu.airmoneymaker.rest.HNBValute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PromjenaValuteFragment extends Fragment {
    private PromjenaValuteViewModel viewModel;
    private Spinner valuta;
    private String odabranaValuta="";
    private Button btnSpremiLozinku2;
    private Button btnOdustani2;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_promjena_valute, container, false);
        InicijalizacijaVarijabli();
        return view;

    }
    private void InicijalizacijaVarijabli() {
        valuta=view.findViewById(R.id.spinnerValuta);
        btnOdustani2=view.findViewById(R.id.btnOdustani2);
        btnOdustani2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.POSTAVKE,getFragmentManager());
            }
        });

        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(PromjenaValuteViewModel.class);
        viewModel.konstruktor(getContext());
        PostaviSpinner();

    }
    private void PostaviSpinner(){
        final List<Valuta> valute = MyDatabase.getInstance(getContext()).getValutaDAO().DohvatiSveValute();
        final List<String> valutaZaSpinner = new ArrayList<>();
        for(Valuta v:valute){
            valutaZaSpinner.add(v.getNaziv());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.spinner_valuta_racuna,valutaZaSpinner);
        valuta.setAdapter(adapter);
        valuta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabranaValuta=valutaZaSpinner.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}

