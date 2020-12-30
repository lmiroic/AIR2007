package air.foi.hr.core.entiteti;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import air.foi.hr.core.modul.racuni.RacuniImplementor;

@Entity (tableName = "racun")
public class Racun implements RacuniImplementor {
    @SerializedName("id")
    @PrimaryKey(autoGenerate =true)
    private int id;
    @SerializedName("naziv")
    private String naziv;
    @SerializedName("pocetno_stanje")
    private float pocetno_stanje;
    @SerializedName("valuta")
    @ForeignKey(entity =Valuta.class,parentColumns ="naziv",childColumns = "valuta")
    private String valuta;
    @SerializedName("ikona")
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

    @Override
    public String getImeRacuna() {
        return getNaziv();
    }

    @Override
    public int getIkonaRacuna(Context context) {
        return context.getResources().getIdentifier(getIkona(),"drawable",context.getPackageName());
    }

    @Override
    public float getStanjeRacuna() {
        return getPocetno_stanje();
    }

    @Override
    public void executeAction(Context context) {

    }
}
