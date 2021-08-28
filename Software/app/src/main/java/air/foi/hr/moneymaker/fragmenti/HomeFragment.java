package air.foi.hr.moneymaker.fragmenti;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.moneymaker.MainActivity;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.HomeScreenViewModel;
import air.foi.hr.moneymaker.manager.CustomAdapterHome;
import air.foi.hr.moneymaker.modul.kategorije.ConcreteCategory;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.HomeScreenViewModel;
import air.foi.hr.moneymaker.manager.CustomAdapterHome;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import air.foi.hr.moneymaker.session.Sesija;


public class HomeFragment extends Fragment {
    private LiveData<List<KategorijaTransakcije>>sveKategorije;
    private View view;
    private HomeScreenViewModel viewModel;
    RecyclerView recyclerView;
    private CustomAdapterHome adapter;
    private ImageButton btnPostavke;

    SimpleDateFormat formaterDate = new SimpleDateFormat("yyyy-MM-dd");
    Calendar datumPonavljajucegTroska = Calendar.getInstance();

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
        ProvjeraPonavljajucihTroskova();
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
        btnPostavke=view.findViewById(R.id.btnPostavke);
        btnPostavke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.POSTAVKE,getFragmentManager());
            }
        });

        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(HomeScreenViewModel.class);
        viewModel.konstruktor(getContext(), (BottomNavigationView) view.findViewById(R.id.bottomNav));
        viewModel.UpravljanjeNavigacijom(getFragmentManager());
    }
    private void ProvjeraPonavljajucihTroskova(){
        List<Transakcija>svetransakcije=MyDatabase.getInstance(getContext()).getTransakcijaDAO().DohvatiTransakcijePonavljajucegTroska(true);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        for (Transakcija t:svetransakcije){
            if(t.getIntervalPonavljanja().equals(date)&&t.isPlacenTrosak()==false){
                AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Obavijest o ponavljajućem trošku!");
                builder.setMessage("Imate ponavljajuci trosak u iznosu od"+ t.getIznos() +" za "+t.getOpis());
                builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Plati", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        t.setPlacenTrosak(true);
                        t.setPonavljajuciTrosak(false);
                        MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(t);
                        Racun racunTerecenja=MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(t.getRacunTerecenja());
                        if(t.getIznos()<=racunTerecenja.getPocetno_stanje()){
                            Transakcija ponavljajuciTrosak= new Transakcija();
                            Date datumPonavljanjaTroska = null;
                            try {
                                datumPonavljanjaTroska = formaterDate.parse(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            datumPonavljajucegTroska.setTime(datumPonavljanjaTroska);
                            datumPonavljajucegTroska.add(Calendar.MONTH, 1);
                            ponavljajuciTrosak.setIntervalPonavljanja(formaterDate.format(datumPonavljajucegTroska.getTime()));
                            ponavljajuciTrosak.setOpis(t.getOpis());
                            ponavljajuciTrosak.setDatum(date);
                            ponavljajuciTrosak.setKategorijaTransakcije(t.getKategorijaTransakcije());
                            ponavljajuciTrosak.setPonavljajuciTrosak(t.isPonavljajuciTrosak());
                            ponavljajuciTrosak.setTipTransakcije(t.getTipTransakcije());
                            ponavljajuciTrosak.setKorisnik(t.getKorisnik());
                            ponavljajuciTrosak.setIznos(t.getIznos());
                            ponavljajuciTrosak.setOpis(t.getOpis());
                            ponavljajuciTrosak.setMemo(t.getMemo());
                            ponavljajuciTrosak.setPlacenTrosak(false);
                            ponavljajuciTrosak.setRacunTerecenja(t.getRacunTerecenja());
                            MyDatabase.getInstance(getContext()).getTransakcijaDAO().UnosTransakcije(ponavljajuciTrosak);
                        }
                        else
                            Toast.makeText(getContext(),"Nemate dovoljno sredstva na računu za plaćanje troškova!",Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();
            }
        }

    }



}