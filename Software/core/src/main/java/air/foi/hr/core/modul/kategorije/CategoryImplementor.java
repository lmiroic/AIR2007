package air.foi.hr.core.modul.kategorije;

import android.content.Context;
import android.graphics.drawable.Drawable;

public interface CategoryImplementor {
    public String getCategoryName();
    public int getCategoryIcon(Context context);
    public float getCategorySum();
    public void executeAction(Context context);
}
