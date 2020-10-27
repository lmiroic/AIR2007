package air.foi.hr.core.entiteti;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName="valuta")
public class Valuta {
    @PrimaryKey(autoGenerate =true)
    private int id;
    private String naziv;
    private float tecaj;

    public Valuta() {
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

    public float getTecaj() {
        return tecaj;
    }

    public void setTecaj(float tecaj) {
        this.tecaj = tecaj;
    }
}
