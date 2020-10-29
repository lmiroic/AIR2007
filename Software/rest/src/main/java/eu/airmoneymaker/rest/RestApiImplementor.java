package eu.airmoneymaker.rest;

import java.util.List;

import air.foi.hr.core.entiteti.Korisnik;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RestApiImplementor {
    @GET("korisnik/index.php?query=getall")
    Call<List<Korisnik>>DohvatiSveKorisnike();
    @GET("korisnik/index.php?query=selectOneUser")
    Call<List<Korisnik>>DohvatiKorisnika(@Query("id")int id);
    @Multipart
    @POST("korisnik/index.php?query=insert")
    Call<Void> UnesiKorisnika (@Part("ime") String ime,
            @Part("prezime") String prezime,
            @Part("google_ID") String googleId,
            @Part("email") String email,
            @Part("lozinka") String lozinka
    );

}
