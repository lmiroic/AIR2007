package air.foi.hr.core.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import air.foi.hr.core.entiteti.Racun;

@Dao
public interface RacunDAO {
    @Query("SELECT * from racun")
    List<Racun> DohvatiSveRacune();

    @Query("SELECT * from racun WHERE id=:id")
    Racun DohvatiRacun(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] UnosRacuna(Racun... racuni);

    @Update
    public void AzurirajRacun(Racun... racuni);

    @Delete
    public void IzbrisiRacun(Racun... racuni);
}
