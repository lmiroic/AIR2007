package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import air.foi.hr.core.database.MyDatabase;

import air.foi.hr.core.manager.HashiranjeLozinke;
import air.foi.hr.moneymaker.session.Sesija;

public class PromjenaLozinkeViewModel extends ViewModel {
    private Context context;
    private MyDatabase baza;


    public void konstruktor(Context context){
        this.context=context;
        this.baza=MyDatabase.getInstance(this.context);
    }
}
