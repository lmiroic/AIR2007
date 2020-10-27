package air.foi.hr.core.entiteti;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (tableName = "racun")
public class Racun {
    @PrimaryKey(autoGenerate =true)
    private int id;

    private String naziv;
    private float pocetno_stanje;
    @ForeignKey(entity =Valuta.class,parentColumns ="id",childColumns = "valuta")
    private String valuta;
    private String boja;
    private String ikona;
    @ForeignKey(entity = Korisnik.class,parentColumns = "id",childColumns = "korisnik_id")
    private int korisnik_id;

    public Racun() {
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

    public float getPocetno_stanje() {
        return pocetno_stanje;
    }

    public void setPocetno_stanje(float pocetno_stanje) {
        this.pocetno_stanje = pocetno_stanje;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public String getBoja() {
        return boja;
    }

    public void setBoja(String boja) {
        this.boja = boja;
    }

    public String getIkona() {
        return ikona;
    }

    public void setIkona(String ikona) {
        this.ikona = ikona;
    }

    public int getKorisnik_id() {
        return korisnik_id;
    }

    public void setKorisnik_id(int korisnik_id) {
        this.korisnik_id = korisnik_id;
    }
}
