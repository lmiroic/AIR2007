package air.foi.hr.core.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import air.foi.hr.core.entiteti.Podsjetnik;

@Dao
public interface PodsjetnikDAO {
    @Query("SELECT * from podsjetnik")
    List<Podsjetnik> DohvatiSvePodsjetnike();

    @Query("SELECT * from podsjetnik WHERE id=:id")
    Podsjetnik DohvatiPodsjetnik(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] UnosPodsjetnika(Podsjetnik... podsjetnici);

    @Update
    public void AzurirajPodsjetnik(Podsjetnik... podsjetnici);

    @Delete
    public void IzbrisiPodsjetnik(Podsjetnik... podsjetnici);
}
