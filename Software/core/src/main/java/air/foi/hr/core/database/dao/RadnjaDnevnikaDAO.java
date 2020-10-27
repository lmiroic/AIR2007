package air.foi.hr.core.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import air.foi.hr.core.entiteti.RadnjaDnevnika;

@Dao
public interface RadnjaDnevnikaDAO {
    @Query("SELECT * from radnjaDnevnika")
    List<RadnjaDnevnika> DohvatiSveRadnjeDnevnika();

    @Query("SELECT * from radnjaDnevnika WHERE id=:id")
    RadnjaDnevnika DohvatiRadnjuDnevnika(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] UnosRadnjeDnevnika(RadnjaDnevnika... radnjeDnevnika);

    @Update
    public void AzurirajRadnjuDnevnika(RadnjaDnevnika... radnjeDnevnika);

    @Delete
    public void IzbrisiRadnjuDnevnika(RadnjaDnevnika... radnjeDnevnika);
}
