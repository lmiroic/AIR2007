package air.foi.hr.moneymaker.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Valuta;
import air.foi.hr.moneymaker.R;
import eu.airmoneymaker.rest.HNBApiImplementor;
import eu.airmoneymaker.rest.HNBApiInstance;
import eu.airmoneymaker.rest.HNBValute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PromjenaValuteViewModel extends ViewModel {
    private Context context;
    private MyDatabase baza;

    public void konstruktor(Context context){
        this.context=context;
        this.baza=MyDatabase.getInstance(this.context);
    }

}


