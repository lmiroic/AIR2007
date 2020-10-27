package air.foi.hr.core.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import air.foi.hr.core.entiteti.DnevnikRada;

@Dao
public interface DnevnikRadaDAO {
    @Query("SELECT * from dnevnikRada")
    List<DnevnikRada> DohvatiSveDnevnikeRada();

    @Query("SELECT * from dnevnikRada WHERE id=:id")
    DnevnikRada DohvatiDnevnikRada(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] UnesiDnevnikRada(DnevnikRada... dnevniciRada);

    @Update
    public void AzurirajDnevnikRada(DnevnikRada... dnevniciRada);

    @Delete
    public void IzbrisiDnevnikRada(DnevnikRada... dnevniciRada);
}
