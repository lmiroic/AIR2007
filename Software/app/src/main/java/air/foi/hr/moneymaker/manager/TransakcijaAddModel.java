package air.foi.hr.moneymaker.manager;

import android.content.Context;

public class TransakcijaAddModel {
    String ikona;

    public int getIkonaTransakcije(Context context) {
        return context.getResources().getIdentifier(ikona, "drawable", context.getPackageName());
    }

    public String getRawIkonaTransakcije() {
        return this.ikona;
    }

    public void setIkonaTransakcije(String ikona) {
        this.ikona = ikona;
    }
}
