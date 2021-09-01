package eu.airmoneymaker.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HNBApiImplementor {
    //Valute
    //Dohvat podataka o trenutačnom tečaju za sve valute
    String xml = "&format=xml";

    @GET("v1")
    Call<List<HNBValute>> DohvatiTrenutacniTecajZaSveValute();

    //Dohvat podataka o tečaju na datum za sve valute
    @GET("v1")
    Call<List<HNBValute>> DohvatiTečajeveZaOdredeniDatum(@Query("datum") String datum);

    //Dohvat podataka za odabranu valutu:
    @GET("v1")
    Call<List<HNBValute>> DohvatiPodatkeZaValutu(@Query("valuta") String valuta);

    //Dohvat podataka za više odabranih valuta:
    @GET("v1?valuta=EUR&valuta=USD")
    Call<List<HNBValute>> DohvatiPodatkeZaViseValuta(@Query("valuta") String valuta);

    //Dohvat podataka za razdoblje:
    @GET("v1?datum-od=2014-03-02&datum-do=2014-04-02")
    Call<List<HNBValute>> DohvatiPodatkeZaRazdoblje(@Query("datum") String datum);

    //Dohvat podataka u xml formatu:
    @GET("v1?valuta=EUR&format=xml")
    Call<List<HNBValute>> DohvatiPodatkeZaValutuXML(@Query("valuta") String valuta, String xml);


}
