package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import air.foi.hr.core.database.MyDatabase;

public class PrijavaViewModel extends ViewModel {
    private Context context;
    private MyDatabase baza;

    public void konstruktor(Context context){
        this.context=context;
        this.baza=MyDatabase.getInstance(this.context);
    }

}
