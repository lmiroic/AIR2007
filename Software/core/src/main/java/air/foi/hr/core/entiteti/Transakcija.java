package air.foi.hr.core.entiteti;

import android.content.Context;
import android.graphics.Path;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Optional;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.modul.transakcije.TransactionImplementor;


@Entity(tableName = "transakcija")
public class Transakcija implements TransactionImplementor {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private float iznos;
    private String datum;
    @ForeignKey(entity = Racun.class, parentColumns = "id", childColumns = "racun")
    private int racunTerecenja;
    @ForeignKey(entity = Racun.class, parentColumns = "id", childColumns = "racun")
    private int racunPrijenosa;
    private int tipTransakcije;
    private String memo;
    private String opis;
    private boolean ponavljajuciTrosak;
    private String ikona;
    private String boja;
    @ForeignKey(entity = Korisnik.class, parentColumns = "id", childColumns = "korisnik")
    private int korisnik;
    private String doDatuma;
    private String intervalPonavljanja;
    private boolean placenTrosak;
    @ForeignKey(entity = KategorijaTransakcije.class, parentColumns = "id", childColumns = "kategorijaTransakcije")
    private int kategorijaTransakcije;

    public Transakcija() {
    }

    public boolean isPlacenTrosak() {
        return placenTrosak;
    }

    public void setPlacenTrosak(boolean placenTrosak) {
        this.placenTrosak = placenTrosak;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getIznos() {
        return iznos;
    }

    public void setIznos(float iznos) {
        this.iznos = iznos;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getRacunTerecenja() {
        return racunTerecenja;
    }

    public void setRacunTerecenja(int racunTerecenja) {
        this.racunTerecenja = racunTerecenja;
    }

    public int getRacunPrijenosa() {
        return racunPrijenosa;
    }

    public void setRacunPrijenosa(int racunPrijenosa) {
        this.racunPrijenosa = racunPrijenosa;
    }

    public int getTipTransakcije() {
        return tipTransakcije;
    }

    public void setTipTransakcije(int tipTransakcije) {
        this.tipTransakcije = tipTransakcije;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public boolean isPonavljajuciTrosak() {
        return ponavljajuciTrosak;
    }

    public void setPonavljajuciTrosak(boolean ponavljajuciTrosak) {
        this.ponavljajuciTrosak = ponavljajuciTrosak;
    }

    public String getIkona() {
        return ikona;
    }

    public void setIkona(String ikona) {
        this.ikona = ikona;
    }

    public String getBoja() {
        return boja;
    }

    public void setBoja(String boja) {
        this.boja = boja;
    }

    public int getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(int korisnik) {
        this.korisnik = korisnik;
    }

    public String getDoDatuma() {
        return doDatuma;
    }

    public void setDoDatuma(String doDatuma) {
        this.doDatuma = doDatuma;
    }

    public String getIntervalPonavljanja() {
        return intervalPonavljanja;
    }

    public void setIntervalPonavljanja(String intervalPonavljanja) {
        this.intervalPonavljanja = intervalPonavljanja;
    }

    public int getKategorijaTransakcije() {
        return kategorijaTransakcije;
    }

    public void setKategorijaTransakcije(int kategorijaTransakcije) {
        this.kategorijaTransakcije = kategorijaTransakcije;
    }

    @Override
    public String toString() {
        return "Transakcija{" +
                "id=" + id +
                ", iznos=" + iznos +
                ", datum='" + datum + '\'' +
                ", racunTerecenja=" + racunTerecenja +
                ", racunPrijenosa=" + racunPrijenosa +
                ", tipTransakcije=" + tipTransakcije +
                ", memo='" + memo + '\'' +
                ", opis='" + opis + '\'' +
                ", ponavljajuciTrosak=" + ponavljajuciTrosak +
                ", ikona='" + ikona + '\'' +
                ", boja='" + boja + '\'' +
                ", korisnik=" + korisnik +
                ", doDatuma='" + doDatuma + '\'' +
                ", intervalPonavljanja=" + intervalPonavljanja +
                ", kategorijaTransakcije=" + kategorijaTransakcije +
                '}';
    }

    @Override
    public String getImeRacuna(Context context) {
        return Optional.ofNullable(MyDatabase.getInstance(context).getRacunDAO().DohvatiRacun(getRacunPrijenosa())).map(Racun::getNaziv).orElse("");
    }

    @Override
    public int getIkonaTransakcije(Context context) {
        return context.getResources().getIdentifier(ikona, "drawable", context.getPackageName());
    }

    @Override
    public float getIznosTransakcije() {
        return Optional.ofNullable(getIznos()).orElse((float) 0.0);
    }

    @Override
    public void executeAction(Context context, Transakcija transakcija) {

    }
}
