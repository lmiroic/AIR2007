package air.foi.hr.core.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import air.foi.hr.core.entiteti.KorisnikoveValute;

@Dao
public interface KorisnikoveValuteDAO {
    @Query("SELECT * from korisnikoveValute")
    List<KorisnikoveValute> DohvatiSveKorisnikoveValute();

    @Query("SELECT * from korisnikoveValute WHERE id=:id")
    KorisnikoveValute DohvatiKorisnikovuValutu(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] UnosKorisnikoveValute(KorisnikoveValute... korisnikoveValute);

    @Update
    public void AzurirajuKorisnikovuValutu(KorisnikoveValute... korisnikoveValute);

    @Delete
    public void IzbrisiKorisnikovuValutu(KorisnikoveValute... korisnikoveValute);
}
