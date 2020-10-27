package air.foi.hr.core.entiteti;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "kategorijaTransakcije")
public class KategorijaTransakcije {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String naziv;
    private int tipTransakcije;

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
}
