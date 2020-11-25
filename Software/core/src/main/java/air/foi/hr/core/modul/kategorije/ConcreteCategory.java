package air.foi.hr.core.modul.kategorije;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class ConcreteCategory implements CategoryImplementor {
    private String naziv;
    private int Ikona;

    public ConcreteCategory() {
    }

    public ConcreteCategory(String naziv, int ikona) {
        this.naziv = naziv;
        Ikona = ikona;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getIkona() {
        return Ikona;
    }

    public void setIkona(int ikona) {
        Ikona = ikona;
    }

    @Override
    public String getCategoryName() {
        return getNaziv();
    }

    @Override
    public int getCategoryIcon() {
        return getIkona();
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
