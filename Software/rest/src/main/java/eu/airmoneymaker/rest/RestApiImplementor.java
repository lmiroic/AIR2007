package eu.airmoneymaker.rest;

import java.util.List;

import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.TipTransakcije;
import air.foi.hr.core.entiteti.Transakcija;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RestApiImplementor {
    //Korisnik
    @GET("korisnik/index.php?query=getall")
    Call<List<Korisnik>> DohvatiSveKorisnike();

    @GET("korisnik/index.php?query=selectOneUser")
    Call<List<Korisnik>> DohvatiKorisnika(@Query("id") int id);

    @Multipart
    @POST("korisnik/index.php?query=insert")
    Call<Void> UnesiKorisnika(@Part("ime") RequestBody ime,
                              @Part("prezime") RequestBody prezime,
                              @Part("google_ID") RequestBody google_ID,
                              @Part("email") RequestBody email,
                              @Part("lozinka") RequestBody lozinka
    );

    @Multipart
    @POST("korisnik/index.php?query=update")
    Call<Void> AzurirajKorisnika(@Part("id") int id,
                                 @Part("atribut") RequestBody atribut,
                                 @Part("vrijednost") RequestBody vrijednost
    );

    @Multipart
    @POST("korisnik/index.php?query=delete")
    Call<Void> ObrisiKorisnika(@Part("id") int id);

    @Multipart
    @POST("korisnik/index.php?query=selectOneUserLogin")
    Call<List<Korisnik>> DohvatiKorisnikaLogin(@Part("email") RequestBody email,
                                               @Part("lozinka") RequestBody lozinka
    );

    //Racun
    @GET("racun/index.php?query=selectRacuneKorisnika")
    Call<List<Racun>> DohvatiKorisnikoveRacune(@Query("korisnik_id") int korisnik_id);

    @GET("racun/index.php?query=getall")
    Call<List<Racun>> DohvatiSveRacune();

    @GET("racun/index.php?query=selectOneRacun")
    Call<List<Racun>> DohvatiRacun(@Query("id") int id);

    @Multipart
    @POST("racun/index.php?query=insert")
    Call<Void> UnesiRacun(@Part("naziv") RequestBody naziv,
                          @Part("pocetno_stanje") RequestBody pocetno_stanje,
                          @Part("valuta") RequestBody valuta,
                          @Part("ikona") RequestBody ikona,
                          @Part("korisnik_id") RequestBody korisnik_id
    );

    @Multipart
    @POST("racun/index.php?query=updateRacun")
    Call<Void> AzurirajRacun(@Part("id") RequestBody id,
                             @Part("naziv") RequestBody naziv,
                             @Part("pocetno_stanje") RequestBody pocetno_stanje,
                             @Part("valuta") RequestBody valuta,
                             @Part("ikona") RequestBody ikona,
                             @Part("korisnik_id") RequestBody korisnik_id
    );

    @Multipart
    @POST("racun/index.php?query=delete")
    Call<Void> ObrisiRacun(@Part("id") RequestBody id);

    //KategorijaTransakcije
    @GET("kategorijaTransakcije/index.php?query=getall")
    Call<List<KategorijaTransakcije>> DohvatiSveKategorijeTransakcije();

    //Transakcije
    @GET("upload/upload.php?query=selectTransakcijeKorisnika")
    Call<List<Transakcija>> DohvatiKorisnikoveTransakcije(@Query("korisnik") int korisnik);

    @Multipart
    @POST("upload/upload.php?query=insert")
    Call<Transakcija> UnesiTransakciju(@Part("iznos") RequestBody iznos,
                                @Part("datum") RequestBody datum,
                                @Part("racunTerecenja") RequestBody racunTerecenja,
                                @Part("racunPrijenosa") RequestBody racunPrijenosa,
                                @Part("tipTransakcije") RequestBody tipTransakcije,
                                @Part MultipartBody.Part memo,
                                @Part("opis") RequestBody opis,
                                @Part("ponavljajuciTrosak") RequestBody ponavljajuciTrosak,
                                @Part("ikona") RequestBody ikona,
                                @Part("korisnik") RequestBody korisnik,
                                @Part("intervalPonavljanja") RequestBody intervalPonavljanja,
                                @Part("kategorijaTransakcije") RequestBody kategorijaTransakcije,
                                @Part("placenTrosak") RequestBody placenTrosak
    );

    @Multipart
    @POST("upload/upload.php?query=updateTransakcije")
    Call<Void> AzurirajTransakciju(@Part("id") RequestBody id,
                                @Part("iznos") RequestBody iznos,
                                @Part("datum") RequestBody datum,
                                @Part("racunTerecenja") RequestBody racunTerecenja,
                                @Part("racunPrijenosa") RequestBody racunPrijenosa,
                                @Part("tipTransakcije") RequestBody tipTransakcije,
                                @Part ("memo") RequestBody memo,
                                @Part("opis") RequestBody opis,
                                @Part("ponavljajuciTrosak") RequestBody ponavljajuciTrosak,
                                @Part("ikona") RequestBody ikona,
                                @Part("korisnik") RequestBody korisnik,
                                @Part("intervalPonavljanja") RequestBody intervalPonavljanja,
                                @Part("kategorijaTransakcije") RequestBody kategorijaTransakcije,
                                @Part("placenTrosak") RequestBody placenTrosak
    );

    @Multipart
    @POST("transakcija/index.php?query=insert")
    Call<Transakcija> UnesiTransakcijuBezSlike(@Part("iznos") RequestBody iznos,
                                   @Part("datum") RequestBody datum,
                                   @Part("racunTerecenja") RequestBody racunTerecenja,
                                   @Part("racunPrijenosa") RequestBody racunPrijenosa,
                                   @Part("tipTransakcije") RequestBody tipTransakcije,
                                   @Part ("memo") RequestBody memo,
                                   @Part("opis") RequestBody opis,
                                   @Part("ponavljajuciTrosak") RequestBody ponavljajuciTrosak,
                                   @Part("ikona") RequestBody ikona,
                                   @Part("korisnik") RequestBody korisnik,
                                   @Part("intervalPonavljanja") RequestBody intervalPonavljanja,
                                   @Part("kategorijaTransakcije") RequestBody kategorijaTransakcije,
                                   @Part("placenTrosak") RequestBody placenTrosak
    );

    @Multipart
    @POST("upload/upload.php?query=delete")
    Call<Void> ObrisiTransakciju(@Part("id") RequestBody id);


}
