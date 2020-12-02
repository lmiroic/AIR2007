package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.core.modul.kategorije.ConcreteCategory;
import air.foi.hr.core.modul.racuni.ConcreteRacun;
import air.foi.hr.core.modul.racuni.RacuniImplementor;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;

public class RacunViewModel extends ViewModel {
    private BottomNavigationView bottomNavigationView;
    private Context context;
    private MyDatabase baza;

    private void OznaciIndex(){
        bottomNavigationView.setSelectedItemId(R.id.racuni);
    }

    public void konstruktor(Context context, BottomNavigationView bottomNavigationView){
        this.context=context;
        this.baza=MyDatabase.getInstance(this.context);
        this.bottomNavigationView = bottomNavigationView;
        this.OznaciIndex();
    }
    public void UpravljanjeNavigacijom(final FragmentManager fragmentManager) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getTitle().toString()) {
                    case "Računi":
                        FragmentSwitcher.ShowFragment(FragmentName.RACUN, fragmentManager);
                        break;
                    case "Kategorije":
                        FragmentSwitcher.ShowFragment(FragmentName.HOME, fragmentManager);
                        break;
                    case "Transakcije":
                        FragmentSwitcher.ShowFragment(FragmentName.TRANSAKCIJA, fragmentManager);
                        break;
                    case "Analiza":
                        FragmentSwitcher.ShowFragment(FragmentName.ANALIZA, fragmentManager);
                        break;


                }
                return true;
            }
        });
    }
        public List<RacuniImplementor> VratiRacunImplementorList(){
            return VratiRacuneIzBaze();
        }

        private List<RacuniImplementor> VratiRacuneIzBaze() {
            List<Racun> racuni=MyDatabase.getInstance(context).getRacunDAO().DohvatiSveRacune();
            ConcreteRacun dodajRacun=new ConcreteRacun("ic_add");
            List<RacuniImplementor>listaZaAdapter=new ArrayList<>();
            for(Racun r:racuni) {
                listaZaAdapter.add(r);
            }
            listaZaAdapter.add(dodajRacun);
            return listaZaAdapter;
        }
    }
