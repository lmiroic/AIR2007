package air.foi.hr.core.entiteti;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import air.foi.hr.core.modul.kategorije.CategoryImplementor;

@Entity(tableName = "kategorijaTransakcije")
public class KategorijaTransakcije implements CategoryImplementor {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("naziv")
    private String naziv;
    @SerializedName("tipTransakcije")
    private int tipTransakcije;

    public int getIkonaKategorije() {
        return ikonaKategorije;
    }

    public void setIkonaKategorije(int ikonaKategorije) {
        this.ikonaKategorije = ikonaKategorije;
    }
    @SerializedName("drawable")
    private int ikonaKategorije;

    public KategorijaTransakcije() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getTipTransakcije() {
        return tipTransakcije;
    }

    public void setTipTransakcije(int tipTransakcije) {
        this.tipTransakcije = tipTransakcije;
    }

    @Override
    public String getCategoryName() {
        return getNaziv();
    }

    @Override
    public int getCategoryIcon() {
        return getIkonaKategorije();
    }

    @Override
    public float getCategorySum() {
        return 1111;
    }

    @Override
    public void executeAction() {

    }
}
