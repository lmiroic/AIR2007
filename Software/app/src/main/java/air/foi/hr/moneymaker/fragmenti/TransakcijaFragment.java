package air.foi.hr.moneymaker.fragmenti;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.modul.transakcije.OnDialogTransactionResult;
import air.foi.hr.core.modul.transakcije.TransactionImplementor;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.TransakcijeViewModel;
import air.foi.hr.moneymaker.manager.CustomAdapterTransakcije;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import air.foi.hr.moneymaker.modul.transakcije.TransactionPrihodDialog;
import air.foi.hr.moneymaker.modul.transakcije.TransactionPrijenosDialog;
import air.foi.hr.moneymaker.modul.transakcije.TransactionTrosakDialog;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class TransakcijaFragment extends Fragment implements View.OnClickListener {

    private TransakcijeViewModel viewModel;
    FloatingActionButton fabDodaj, fabPrihod, fabTrosak, fabPrijenos;
    Animation fabOpen, fabClose, fabClock, fablAntiClock;
    boolean isOpen = false;
    public Uri slika;
    public TransactionTrosakDialog transactionTrosakDialog;
    public TransactionPrihodDialog transactionPrihodDialog;
    public TransactionPrijenosDialog transactionPrijenosDialog;
    public ImageButton btnPostavke;


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
        view = inflater.inflate(R.layout.fragment_transakcija, container, false);
        InicijalizacijaVarijabli();
        PostaviFloatingButtone();
        PostaviRecycleView();
        brisiTransakciju(recyclerView,adapterTransakcije);
        return view;
    }

    private void PostaviRecycleView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewTransakcije);
        adapterTransakcije = new CustomAdapterTransakcije(getContext());
        viewModel.VratiTransakcije().observe(this, new Observer<List<Transakcija>>() {
            @Override
            public void onChanged(List<Transakcija> transakcijas) {
                List<TransactionImplementor> transactionImplementors=new ArrayList<>();
                for(Transakcija t:transakcijas){
                    transactionImplementors.add((TransactionImplementor) t);
                }
                adapterTransakcije.arrayList=transactionImplementors;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapterTransakcije);
            }
        });
    }

    private void InicijalizacijaVarijabli() {
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(TransakcijeViewModel.class);
        viewModel.konstruktor(getContext(), (BottomNavigationView) view.findViewById(R.id.bottomNav));
        viewModel.UpravljanjeNavigacijom(getFragmentManager());
        fabDodaj = view.findViewById(R.id.btnDodajTransakciju);
        fabPrihod = view.findViewById(R.id.floatingActionPrihod);
        fabTrosak = view.findViewById(R.id.floatingActionTrosak);
        fabPrijenos = view.findViewById(R.id.floatingActionPrijenos);
        btnPostavke=view.findViewById(R.id.imgBtnPostavke);
        btnPostavke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.POSTAVKE,getFragmentManager());
            }
        });

        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close);
        fabClock = AnimationUtils.loadAnimation(getContext(), R.anim.clockwise);
        fablAntiClock = AnimationUtils.loadAnimation(getContext(), R.anim.anticlockwise);
        fabTrosak.setOnClickListener(this);
        fabPrihod.setOnClickListener(this);
        fabPrijenos.setOnClickListener(this);

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            slika = data.getData();
            Toast.makeText(getContext(), slika.getPath(), Toast.LENGTH_SHORT).show();
            transactionTrosakDialog.imageViewTrosak.setImageURI(slika);
             }
            if (requestCode == 6) {
            slika = data.getData();
            Toast.makeText(getContext(), slika.getPath(), Toast.LENGTH_SHORT).show();
            transactionPrihodDialog.imageViewPrihod.setImageURI(slika);
             }
        if (requestCode == 7) {
            slika = data.getData();
            Toast.makeText(getContext(), slika.getPath(), Toast.LENGTH_SHORT).show();
            transactionPrijenosDialog.imageViewPrijenos.setImageURI(slika);
        }

    }

    private void PostaviFloatingButtone() {
        fabDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    fabPrijenos.startAnimation(fabClose);
                    fabTrosak.startAnimation(fabClose);
                    fabPrihod.startAnimation(fabClose);
                    fabDodaj.startAnimation(fabClock);

                    fabPrijenos.setClickable(false);
                    fabTrosak.setClickable(false);
                    fabPrihod.setClickable(false);

                    isOpen = false;
                } else {
                    fabPrijenos.startAnimation(fabOpen);
                    fabTrosak.startAnimation(fabOpen);
                    fabPrihod.startAnimation(fabOpen);
                    fabDodaj.startAnimation(fablAntiClock);

                    fabPrijenos.setClickable(true);
                    fabTrosak.setClickable(true);
                    fabPrihod.setClickable(true);

                    isOpen = true;
                }
            }
        });

    }

    private void trosak() {
        TransactionTrosakDialog transactionTrosakDialog = new TransactionTrosakDialog(getContext(), this);
        transactionTrosakDialog.SetOnDialogTransactionResult(new OnDialogTransactionResult() {
            @Override
            public void finish() {

            }
        });
        transactionTrosakDialog.show();
    }
    private void prihod(){
        TransactionPrihodDialog transactionPrihodDialog=new TransactionPrihodDialog(getContext(),this);
        transactionPrihodDialog.SetOnDialogTransationResult(new OnDialogTransactionResult() {
            @Override
            public void finish() {

            }
        });
        transactionPrihodDialog.show();
    }
    private void prijenos(){
        TransactionPrijenosDialog transactionPrijenosDialog=new TransactionPrijenosDialog(getContext(), this);
        transactionPrijenosDialog.SetOnDialogTransactionResult(new OnDialogTransactionResult() {
            @Override
            public void finish() {

            }
        });
        transactionPrijenosDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionTrosak:
                trosak();
                break;

            case R.id.floatingActionPrihod:
                prihod();
                break;

            case R.id.floatingActionPrijenos:
                prijenos();
                break;
        }

    }
    public void brisiTransakciju(final RecyclerView recyclerView, final CustomAdapterTransakcije customAdapterTransakcije) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Transakcija transakcija1 = customAdapterTransakcije.getTransactionAtPosition(viewHolder.getAdapterPosition());
                customAdapterTransakcije.removeTransactionAtPosition(viewHolder.getAdapterPosition());
                MyDatabase.getInstance(getContext()).getTransakcijaDAO().IzbrisiTransakciju(transakcija1);
                Retrofit retrofit = RetrofitInstance.getInstance();
                RestApiImplementor api = retrofit.create(RestApiImplementor.class);
                Call<Void> pozivUnosa = api.ObrisiTransakciju(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transakcija1.getId())));
                pozivUnosa.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e("Transakcija", "Izbrisana transakcija");
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                Snackbar.make(recyclerView, "Izbrisana je transakcija " + transakcija1.getOpis(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}