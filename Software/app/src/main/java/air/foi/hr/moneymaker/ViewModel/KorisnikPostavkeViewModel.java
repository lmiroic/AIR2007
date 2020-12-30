package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import air.foi.hr.moneymaker.session.Sesija;
import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;

public class KorisnikPostavkeViewModel extends ViewModel {
    private Context context;
    private MyDatabase baza;


    public void konstruktor(Context context){
        this.context=context;
        this.baza=MyDatabase.getInstance(this.context);
    }

}
