package eu.airmoneymaker.rest;

import com.google.gson.annotations.SerializedName;

public class HNBValute {
    @SerializedName("Broj tečajnice")
    private String broj_tecajnice;
    @SerializedName("Datum primjene")
    private String datum;
    @SerializedName("Država")
    private String drzava;
    @SerializedName("Šifra valute")
    private String sifra_valute;
    @SerializedName("Valuta")
    private String valuta;
    @SerializedName("Jedinica")
    private String jedinica;
    @SerializedName("Kupovni za devize")
    private String kupovni_tecaj;
    @SerializedName("Srednji za devize")
    private String srednji_tecaj;
    @SerializedName("Prodajni za devize")
    private String prodajni_tecaj;

    public HNBValute() {
    }
    public String getBroj_tecajnice() {
        return broj_tecajnice;
    }

    public void setBroj_tecajnice(String broj_tecajnice) {
        this.broj_tecajnice = broj_tecajnice;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public String getSifra_valute() {
        return sifra_valute;
    }

    public void setSifra_valute(String sifra_valute) {
        this.sifra_valute = sifra_valute;
    }

    public String getValuta() {
        return valuta;
    }
    public void setValuta(String valuta) {
        this.valuta = valuta;
    }
    public String getJedinica() {
        return jedinica;
    }
    public void setJedinica(String jedinica) {
        this.jedinica = jedinica;
    }
    public String getKupovni_tecaj() {
        return kupovni_tecaj;
    }
    public void setKupovni_tecaj(String kupovni_tecaj) {
        this.kupovni_tecaj = kupovni_tecaj;
    }
    public String getSrednji_tecaj() {
        return srednji_tecaj;
    }
    public void setSrednji_tecaj(String srednji_tecaj) {
        this.srednji_tecaj = srednji_tecaj;
    }
    public String getProdajni_tecaj() {
        return prodajni_tecaj;
    }
    public void setProdajni_tecaj(String prodajni_tecaj) {
        this.prodajni_tecaj = prodajni_tecaj;
    }

}
