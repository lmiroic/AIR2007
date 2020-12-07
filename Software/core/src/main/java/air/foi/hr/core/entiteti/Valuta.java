package air.foi.hr.core.entiteti;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName="valuta")
public class Valuta {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String naziv;
    private float tecaj;

    public Valuta() {
    }


    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public float getTecaj() {
        return tecaj;
    }

    public void setTecaj(float tecaj) {
        this.tecaj = tecaj;
    }
}
