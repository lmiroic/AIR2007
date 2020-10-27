package air.foi.hr.core.entiteti;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (tableName = "dnevnikRada")
public class DnevnikRada {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ForeignKey(entity = Korisnik.class,parentColumns ="id", childColumns ="korisnik")
    private int korisnik;
    @ForeignKey(entity =RadnjaDnevnika.class,parentColumns ="id",childColumns ="radnja")
    private int radnja;

    public DnevnikRada() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(int korisnik) {
        this.korisnik = korisnik;
    }

    public int getRadnja() {
        return radnja;
    }

    public void setRadnja(int radnja) {
        this.radnja = radnja;
    }
}
