package air.foi.hr.moneymaker.manager;

import android.content.Context;

public class CategoryAddModel {
    String ikona;

    public int getIkona(Context context) {
        return context.getResources().getIdentifier(ikona, "drawable", context.getPackageName());
    }

    public String getRawIkona() {
        return this.ikona;
    }

    public void setIkona(String ikona) {
        this.ikona = ikona;
    }
}
