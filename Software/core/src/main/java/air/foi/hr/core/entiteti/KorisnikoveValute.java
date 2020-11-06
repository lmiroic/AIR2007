package air.foi.hr.core.entiteti;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "korisnikoveValute")
public class KorisnikoveValute {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ForeignKey(entity =Valuta.class,parentColumns = "id",childColumns ="valuta")
    private String valuta;
    @ForeignKey(entity = Korisnik.class,parentColumns = "id",childColumns = "korisnik")
    private int korisnik;

    public KorisnikoveValute() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public int getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(int korisnik) {
        this.korisnik = korisnik;
    }
}
