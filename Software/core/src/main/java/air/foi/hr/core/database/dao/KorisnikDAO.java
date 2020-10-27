package air.foi.hr.core.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import air.foi.hr.core.entiteti.Korisnik;


@Dao
public interface KorisnikDAO {
    @Query("SELECT * from korisnik")
    List<Korisnik> DohvatiSveKorisnike();

    @Query("SELECT * from korisnik WHERE id=:id")
    Korisnik DohvatiKorisnika(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] UnosKorisnika(Korisnik... korisnici);

    @Update
    public void azurirajKorisnika(Korisnik... korisnici);

    @Delete
    public void izbrisiKorisnika(Korisnik... korisnici);
}
