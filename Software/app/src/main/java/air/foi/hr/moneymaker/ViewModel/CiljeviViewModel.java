package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Ciljevi;
import air.foi.hr.moneymaker.session.Sesija;

public class CiljeviViewModel extends ViewModel {
    private Context context;
    private MyDatabase baza;
    private LiveData<List<Ciljevi>> sviCiljeviKorisnika;


    public void konstruktor(Context context){
        this.context=context;
        this.baza=MyDatabase.getInstance(this.context);
    }
    public LiveData<List<Ciljevi>> VratiCiljeveKorisnika(){
        sviCiljeviKorisnika=MyDatabase.getInstance(context).getCiljeviDAO().DohvatiSveCiljeveKorisnikaLIVE(Sesija.getInstance().getKorisnik().getId());
        return sviCiljeviKorisnika;
    }
}
