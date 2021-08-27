package air.foi.hr.moneymaker.fragmenti;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Ciljevi;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.modul.ciljevi.OnDialogCiljeviResult;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.CiljeviViewModel;
import air.foi.hr.moneymaker.manager.CustomAdapterCiljevi;
import air.foi.hr.moneymaker.manager.CustomAdapterTransakcije;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import air.foi.hr.moneymaker.modul.ciljevi.CiljeviAddDialog;

public class CiljeviFragment extends Fragment implements View.OnClickListener {

    private CiljeviViewModel viewModel;
    private View view;
    private RecyclerView recyclerView;
    private FloatingActionButton fabDodajCilj;
    private CustomAdapterCiljevi adapter;
    private Button btnNatrag;

    public CiljeviAddDialog ciljeviAddDialog;

    public CiljeviFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_ciljevi,container,false);
        InicijalizacijaVarijabli();
        PostaviRecyclerView();
        brisiCilj(recyclerView,adapter);
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void PostaviRecyclerView(){
        recyclerView=view.findViewById(R.id.recyclerViewCiljeva);
        adapter= new CustomAdapterCiljevi(getContext());
        viewModel.VratiCiljeveKorisnika().observe(this, new Observer<List<Ciljevi>>() {
            @Override
            public void onChanged(List<Ciljevi> ciljevis) {
                adapter.arrayList=ciljevis;
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
            }
        });

    }
    private void InicijalizacijaVarijabli(){
        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(CiljeviViewModel.class);
        viewModel.konstruktor(getContext());
        btnNatrag=view.findViewById(R.id.btnNatragCiljevi);
        btnNatrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.POSTAVKE,getFragmentManager());
            }
        });
        fabDodajCilj=view.findViewById(R.id.fabAddCiljevi);
        fabDodajCilj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOpen();
            }
        });
    }
    private void dialogOpen(){
        CiljeviAddDialog ciljeviAddDialog=new CiljeviAddDialog(getContext(),this);
        ciljeviAddDialog.SetOnDialogCiljeciResult(new OnDialogCiljeviResult() {
            @Override
            public void finish() {

            }
        });
        ciljeviAddDialog.show();
    }


    @Override
    public void onClick(View v) {

    }

    public void brisiCilj(final RecyclerView recyclerView, final CustomAdapterCiljevi customAdapterCiljevi) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Ciljevi cilj = customAdapterCiljevi.getCiljAtPosition(viewHolder.getAdapterPosition());
                customAdapterCiljevi.removeCiljAtPosition(viewHolder.getAdapterPosition());
                MyDatabase.getInstance(getContext()).getCiljeviDAO().IzbrisiCilj(cilj);
                Snackbar.make(recyclerView, "Izbrisan je cilj " + cilj.getNaziv(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}
