package air.foi.hr.moneymaker.fragmenti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.HomeScreenViewModel;
import air.foi.hr.moneymaker.manager.CustomAdapterHome;
import air.foi.hr.moneymaker.modul.kategorije.ConcreteCategory;


public class HomeFragment extends Fragment {
    private View view;
    private HomeScreenViewModel viewModel;
    RecyclerView recyclerView;
    private CustomAdapterHome adapter;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home, container, false);
        InicijalizacijaVarijabli();
        PostaviRecycleView();
        return view;
    }

    private void PostaviRecycleView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new CustomAdapterHome(getContext());
        viewModel.getKategorijeTransakcije().observe(this, new Observer<List<KategorijaTransakcije>>() {
            @Override
            public void onChanged(List<KategorijaTransakcije> kategorijaTransakcijes) {
                adapter.SetKategorije(kategorijaTransakcijes);
                GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void InicijalizacijaVarijabli() {
        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(HomeScreenViewModel.class);
        viewModel.konstruktor(getContext(), (BottomNavigationView) view.findViewById(R.id.bottomNav));
        viewModel.UpravljanjeNavigacijom(getFragmentManager());


    }


}