package air.foi.hr.core.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import air.foi.hr.core.entiteti.Transakcija;

@Dao
public interface TransakcijaDAO {
    @Query("SELECT * from transakcija")
    List<Transakcija> DohvatiSveTransakcije();

    @Query("SELECT * from transakcija WHERE id=:id")
    Transakcija DohvatiTransakciju(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] UnosTransakcije(Transakcija... transakcije);

    @Update
    public void AzurirajTransakciju(Transakcija... transakcije);

    @Delete
    public void IzbrisiTransakciju(Transakcija... transakcije);
    @Query("SELECT * from transakcija")
    LiveData<List<Transakcija>> DohvatiSveTransakcijeLIVE();

    @Query("SELECT * from transakcija WHERE ponavljajuciTrosak=:ponTrosak")
    List<Transakcija> DohvatiTransakcijePonavljajucegTroska(boolean ponTrosak);
}
