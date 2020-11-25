package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.core.modul.kategorije.ConcreteCategory;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeScreenViewModel extends ViewModel {
    private BottomNavigationView bottomNavigationView;
    private Context context;
    private MyDatabase baza;

    private void OznaciIndex(){
        bottomNavigationView.setSelectedItemId(R.id.kategorije);
    }

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
    public List<CategoryImplementor> VratiCategoryImplementorList(){
        return VratiKategorijeIzBaze();
    }

    private List<CategoryImplementor> VratiKategorijeIzBaze() {
        List<KategorijaTransakcije> kategorijaTransakcije=MyDatabase.getInstance(context).getKategorijaTransakcijeDAO().DohvatiSveKategorijeTransakcije();
        ConcreteCategory dodajTransakciju=new ConcreteCategory("Add",R.drawable.ic_add);
        List<CategoryImplementor>listaZaAdapter=new ArrayList<>();
        for(KategorijaTransakcije kt: kategorijaTransakcije)
            listaZaAdapter.add(kt);
        listaZaAdapter.add(dodajTransakciju);
        return listaZaAdapter;
    }
}
