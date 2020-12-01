package air.foi.hr.core.modul.kategorije;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class ConcreteCategory implements CategoryImplementor {
    private String naziv;
    private String Ikona;


    public ConcreteCategory() {
    }

    public ConcreteCategory(String naziv, String ikona) {
        this.naziv = naziv;
        Ikona = ikona;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getIkona() {
        return Ikona;
    }

    public void setIkona(String ikona) {
        Ikona = ikona;
    }

    @Override
    public String getCategoryName() {
        return getNaziv();
    }

    @Override
    public int getCategoryIcon(Context context) {
        return context.getResources().getIdentifier(getIkona(),"drawable",context.getPackageName());
    }

    @Override
    public float getCategorySum() {
        return 0;
    }

    @Override
    public void executeAction() {
        Log.e("Kategorija","Dodana kategorija");
    }
}
