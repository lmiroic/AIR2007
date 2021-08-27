package air.foi.hr.core.entiteti;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity( tableName = "ciljevi",foreignKeys = {@ForeignKey(entity = Korisnik.class,parentColumns = "id",childColumns = "korisnik"),@ForeignKey(entity = KategorijaTransakcije.class,parentColumns = "id",childColumns = "kategorija")})
public class Ciljevi{
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("naziv")
    private String naziv;

    @SerializedName("iznos")
    private float iznos;

    @SerializedName("korisnik")
    private int korisnik;

    @SerializedName("datum")
    private String datum;

    @SerializedName("kategorija")
    private int kategorija;

    private boolean ostvarenCilj;

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

    public float getIznos() {
        return iznos;
    }

    public void setIznos(float iznos) {
        this.iznos = iznos;
    }

    public int getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(int korisnik) {
        this.korisnik = korisnik;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getKategorija() {
        return kategorija;
    }

    public void setKategorija(int kategorija) {
        this.kategorija = kategorija;
    }

    public boolean isOstvarenCilj() {
        return ostvarenCilj;
    }

    public void setOstvarenCilj(boolean ostvarenCilj) {
        this.ostvarenCilj = ostvarenCilj;
    }
}
