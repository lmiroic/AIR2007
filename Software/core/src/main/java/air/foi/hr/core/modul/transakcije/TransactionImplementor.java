package air.foi.hr.core.modul.transakcije;
import android.content.Context;

import air.foi.hr.core.entiteti.Transakcija;

public interface TransactionImplementor {
    public String getImeRacuna(Context context);
    public int getIkonaTransakcije(Context context);
    public float getIznosTransakcije();
    public void executeAction(Context context, Transakcija transakcija);
}
