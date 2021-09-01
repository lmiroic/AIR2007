package air.foi.hr.core.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import air.foi.hr.core.entiteti.Valuta;

@Dao
public interface ValutaDAO {
    @Query("SELECT * from valuta")
    List<Valuta> DohvatiSveValute();

    @Query("SELECT * from valuta WHERE naziv=:naziv")
    Valuta DohvatiValutu(String naziv);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] UnosValute(Valuta... valute);

    @Update
    public void AzurirajValutu(Valuta... valute);

    @Delete
    public void IzbrisiValutu(Valuta... valute);

    @Query("DELETE from valuta")
    public void ObrisiSveValute();
}
