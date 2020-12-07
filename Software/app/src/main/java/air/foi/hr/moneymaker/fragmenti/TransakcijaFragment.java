package air.foi.hr.moneymaker.fragmenti;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.modul.transakcije.OnDialogTransactionResult;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.RacunViewModel;
import air.foi.hr.moneymaker.ViewModel.TransakcijeViewModel;
import air.foi.hr.moneymaker.manager.CustomAdapterHome;
import air.foi.hr.moneymaker.manager.CustomAdapterTransakcije;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import air.foi.hr.moneymaker.modul.transakcije.TransactionPrihodDialog;
import air.foi.hr.moneymaker.modul.transakcije.TransactionPrijenosDialog;
import air.foi.hr.moneymaker.modul.transakcije.TransactionTrosakDialog;


public class TransakcijaFragment extends Fragment {

    private TransakcijeViewModel viewModel;
    FloatingActionButton fabDodaj, fabPrihod, fabTrosak, fabPrijenos;
    Animation fabOpen, fabClose, fabClock, fablAntiClock;
    boolean isOpen=false;

    private View view;
    RecyclerView recyclerView;
    private CustomAdapterTransakcije adapterTransakcije;

    public TransakcijaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_transakcija, container, false);
        InicijalizacijaVarijabli();
        PostaviFloatingButtone();
        PostaviRecycleView();


        return view;


    }

    private void PostaviRecycleView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewTransakcije);
        viewModel.VratiTransakcije().observe(this, new Observer<List<Transakcija>>() {
            @Override
            public void onChanged(List<Transakcija> transakcijas) {
                adapterTransakcije=new CustomAdapterTransakcije(getContext(),transakcijas);
                LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapterTransakcije);
            }
        });
    }

    private void InicijalizacijaVarijabli() {
        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(TransakcijeViewModel.class);
        viewModel.konstruktor(getContext(), (BottomNavigationView) view.findViewById(R.id.bottomNav));
        viewModel.UpravljanjeNavigacijom(getFragmentManager());
        fabDodaj=view.findViewById(R.id.btnDodajTransakciju);
        fabPrihod=view.findViewById(R.id.floatingActionPrihod);
        fabTrosak=view.findViewById(R.id.floatingActionTrosak);
        fabPrijenos=view.findViewById(R.id.floatingActionPrijenos);

        fabOpen= AnimationUtils.loadAnimation(getContext(),R.anim.rotate_open);
        fabClose=AnimationUtils.loadAnimation(getContext(),R.anim.rotate_close);
        fabClock=AnimationUtils.loadAnimation(getContext(),R.anim.clockwise);
        fablAntiClock=AnimationUtils.loadAnimation(getContext(),R.anim.anticlockwise);


    }
    private void PostaviFloatingButtone(){
        fabDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    fabPrijenos.startAnimation(fabClose);
                    fabTrosak.startAnimation(fabClose);
                    fabPrihod.startAnimation(fabClose);
                    fabDodaj.startAnimation(fabClock);

                    fabPrijenos.setClickable(false);
                    fabTrosak.setClickable(false);
                    fabPrihod.setClickable(false);

                    isOpen=false;
                }
                else{
                    fabPrijenos.startAnimation(fabOpen);
                    fabTrosak.startAnimation(fabOpen);
                    fabPrihod.startAnimation(fabOpen);
                    fabDodaj.startAnimation(fablAntiClock);

                    fabPrijenos.setClickable(true);
                    fabTrosak.setClickable(true);
                    fabPrihod.setClickable(true);

                    isOpen=true;
                }
            }
        });
        fabPrihod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionPrihodDialog transactionPrihodDialog=new TransactionPrihodDialog(getContext());
                transactionPrihodDialog.SetOnDialogTransactionResult(new OnDialogTransactionResult() {
                    @Override
                    public void finish() {

                    }
                });

                transactionPrihodDialog.show();

            }
        });

        fabTrosak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionTrosakDialog transactionTrosakDialog=new TransactionTrosakDialog(getContext());
                transactionTrosakDialog.SetOnDialogTransactionResult(new OnDialogTransactionResult() {
                    @Override
                    public void finish() {

                    }
                });
                transactionTrosakDialog.show();
            }
        });
        fabPrijenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionPrijenosDialog transactionPrijenosDialog=new TransactionPrijenosDialog(getContext());
                transactionPrijenosDialog.SetOnDialogTransactionResult(new OnDialogTransactionResult() {
                    @Override
                    public void finish() {

                    }
                });
                transactionPrijenosDialog.show();
            }
        });

    }

}