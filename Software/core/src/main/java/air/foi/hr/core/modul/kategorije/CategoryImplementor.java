package air.foi.hr.core.modul.kategorije;

import android.content.Context;
import android.graphics.drawable.Drawable;

import air.foi.hr.core.entiteti.KategorijaTransakcije;

public interface CategoryImplementor {
    public String getCategoryName();
    public int getCategoryIcon(Context context);
    public float getCategorySum(Context context, KategorijaTransakcije kategorijaTransakcije);
    public void executeAction(Context context);
}
