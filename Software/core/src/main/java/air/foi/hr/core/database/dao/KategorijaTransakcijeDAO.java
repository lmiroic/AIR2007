package air.foi.hr.core.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import air.foi.hr.core.entiteti.KategorijaTransakcije;

@Dao
public interface KategorijaTransakcijeDAO {
    @Query("SELECT * from kategorijaTransakcije")
    List<KategorijaTransakcije> DohvatiSveKategorijeTransakcije();

    @Query("SELECT * from kategorijaTransakcije WHERE id=:id")
    KategorijaTransakcije DohvatiKategorijuTransakcije(int id);

    @Query("SELECT * from kategorijaTransakcije WHERE naziv=:naziv")
    KategorijaTransakcije DohvatiKategorijuTransakcijePremaNazivu(String naziv);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] UnosKategorijeTransakcije(KategorijaTransakcije... kategorijeTransakcija);

    @Update
    public void AzurirajKategorijuTransakcije(KategorijaTransakcije... kategorijeTransakcija);

    @Delete
    public void IzbrisiKategorijuTransakcije(KategorijaTransakcije... kategorijeTransakcija);

    @Query("SELECT * from kategorijaTransakcije")
    LiveData<List<KategorijaTransakcije>> DohvatiSveKategorijeTransakcijeLIVE();
}
