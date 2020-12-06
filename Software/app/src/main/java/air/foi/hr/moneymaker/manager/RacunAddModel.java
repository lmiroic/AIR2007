package air.foi.hr.moneymaker.manager;

import android.content.Context;

public class RacunAddModel {
    String ikonaRacuna;

    public int getIkonaRacuna(Context context) {
        return context.getResources().getIdentifier(ikonaRacuna,"drawable",context.getPackageName());
    }
    public String getRawIkonaRacuna(){
        return this.ikonaRacuna;
    }
    public void setIkonaRacuna(String ikonaRacuna) {
        this.ikonaRacuna = ikonaRacuna;
    }
}
