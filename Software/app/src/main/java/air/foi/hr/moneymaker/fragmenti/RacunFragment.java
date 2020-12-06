package air.foi.hr.moneymaker.fragmenti;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.modul.racuni.RacuniImplementor;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.AnalizaViewModel;
import air.foi.hr.moneymaker.ViewModel.RacunViewModel;
import air.foi.hr.moneymaker.manager.CustomAdapterHome;
import air.foi.hr.moneymaker.manager.CustomAdapterRacun;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import air.foi.hr.moneymaker.modul.racun.ConcreteRacun;

public class RacunFragment extends Fragment {
    private CustomAdapterRacun adapterRacun;
    private RacunViewModel viewModel;
    private View view;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_racun, container, false);
        InicijalizacijaVarijabli();
        PostaviRecycleView();
        return view;
    }
    private void PostaviRecycleView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_viewRacuni);
        adapterRacun = new CustomAdapterRacun(getContext());
        viewModel.VratiRacunLiveData().observe(this, new Observer<List<Racun>>() {
            @Override
            public void onChanged(List<Racun> racuns) {
                List<RacuniImplementor> liveListaRacuna=new ArrayList<>();
                for (Racun r:racuns){
                    liveListaRacuna.add(r);
                }
                ConcreteRacun dodajRacun=new ConcreteRacun();
                dodajRacun.setIkona("ic_add");
                liveListaRacuna.add(dodajRacun);
                adapterRacun.arrayList=liveListaRacuna;
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(adapterRacun);
            }
        });


    }

    private void InicijalizacijaVarijabli() {
        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(RacunViewModel.class);
        viewModel.konstruktor(getContext(), (BottomNavigationView) view.findViewById(R.id.bottomNav));
        viewModel.UpravljanjeNavigacijom(getFragmentManager());
    }
}