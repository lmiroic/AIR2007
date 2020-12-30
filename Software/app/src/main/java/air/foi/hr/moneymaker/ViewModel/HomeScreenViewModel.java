package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.moneymaker.MainActivity;
import air.foi.hr.moneymaker.modul.kategorije.ConcreteCategory;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;

public class HomeScreenViewModel extends ViewModel {
    private BottomNavigationView bottomNavigationView;
    private Context context;
    private MyDatabase baza;
    public List<CategoryImplementor>listaZaAdapter=new ArrayList<>();
    private void OznaciIndex(){
        bottomNavigationView.setSelectedItemId(R.id.kategorije);
    }

    public LiveData<List<KategorijaTransakcije>>sveKategorije;
    public void konstruktor(Context context, BottomNavigationView bottomNavigationView){
        this.context=context;
        this.baza=MyDatabase.getInstance(this.context);
        this.bottomNavigationView = bottomNavigationView;
        this.OznaciIndex();
    }
    public void UpravljanjeNavigacijom(final FragmentManager fragmentManager){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getTitle().toString()){
                    case "Računi":
                        FragmentSwitcher.ShowFragment(FragmentName.RACUN,fragmentManager);
                        break;
                    case "Kategorije":
                        FragmentSwitcher.ShowFragment(FragmentName.HOME,fragmentManager);
                        break;
                    case "Transakcije":
                        FragmentSwitcher.ShowFragment(FragmentName.TRANSAKCIJA,fragmentManager);
                        break;
                    case "Analiza":
                        FragmentSwitcher.ShowFragment(FragmentName.ANALIZA,fragmentManager);
                        break;

                }
                return true;
            }
        });
    }
    public void VratiCategoryImplementorList(){
        VratiKategorijeIzBaze();
    }
    public LiveData<List<KategorijaTransakcije>>getKategorijeTransakcije(){
        sveKategorije=MyDatabase.getInstance(context).getKategorijaTransakcijeDAO().DohvatiSveKategorijeTransakcijeLIVE();
        return sveKategorije;
    }
    private void VratiKategorijeIzBaze() {



    }
}
