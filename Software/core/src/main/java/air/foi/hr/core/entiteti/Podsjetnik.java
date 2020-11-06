package air.foi.hr.core.entiteti;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (tableName = "podsjetnik")
public class Podsjetnik {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String naziv;
    private String opis;
    @ForeignKey(entity = Korisnik.class,parentColumns = "id",childColumns = "korisnik")
    private int korisnik;

    public Podsjetnik() {
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

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public int getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(int korisnik) {
        this.korisnik = korisnik;
    }
}
