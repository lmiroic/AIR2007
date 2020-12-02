package air.foi.hr.core.modul.racuni;

import android.content.Context;

public interface RacuniImplementor {
    public String getImeRacuna();
    public int getIkonaRacuna(Context context);
    public float getStanjeRacuna();
    public void executeAction();
}
