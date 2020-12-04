package air.foi.hr.core.entiteti;

import android.content.Context;
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
    @SerializedName("tip_transakcije")
    private int tipTransakcije;

    public String getIkonaKategorije() {
        return ikonaKategorije;
    }

    public void setIkonaKategorije(String ikonaKategorije) {
        this.ikonaKategorije = ikonaKategorije;
    }
    @SerializedName("drawable")
    private String ikonaKategorije;

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
    public int getCategoryIcon(Context context) {
        return context.getResources().getIdentifier(ikonaKategorije,"drawable",context.getPackageName());
    }

    @Override
    public float getCategorySum() {
        return 1111;
    }

    @Override
    public void executeAction(Context context) {

    }
}
