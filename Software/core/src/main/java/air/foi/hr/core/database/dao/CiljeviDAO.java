package air.foi.hr.core.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import air.foi.hr.core.entiteti.Ciljevi;
import air.foi.hr.core.entiteti.Transakcija;


@Dao
public interface CiljeviDAO {
    @Query("SELECT * from ciljevi")
    List<Ciljevi> DohvatiSveCiljeve();

    @Query("SELECT * from ciljevi WHERE korisnik=:korisnik")
    LiveData<List<Ciljevi>> DohvatiSveCiljeveKorisnikaLIVE(int korisnik);

    @Query("SELECT * from Transakcija WHERE kategorijaTransakcije=:kategorija AND datum>=:datum")
    List<Transakcija> DohvatiTransakcijeCilja(int kategorija, String datum);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] UnosCilja(Ciljevi... ciljevis);

    @Update
    public void AzurirajCilj(Ciljevi... ciljevis);

    @Delete
    public void IzbrisiCilj(Ciljevi... ciljevis);
}
