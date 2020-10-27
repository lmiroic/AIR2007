package air.foi.hr.core.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import air.foi.hr.core.database.dao.DnevnikRadaDAO;
import air.foi.hr.core.database.dao.KategorijaTransakcijeDAO;
import air.foi.hr.core.database.dao.KorisnikDAO;
import air.foi.hr.core.database.dao.KorisnikoveValuteDAO;
import air.foi.hr.core.database.dao.PodsjetnikDAO;
import air.foi.hr.core.database.dao.RacunDAO;
import air.foi.hr.core.database.dao.RadnjaDnevnikaDAO;
import air.foi.hr.core.database.dao.TransakcijaDAO;
import air.foi.hr.core.database.dao.ValutaDAO;
import air.foi.hr.core.entiteti.DnevnikRada;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.entiteti.KorisnikoveValute;
import air.foi.hr.core.entiteti.Podsjetnik;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.RadnjaDnevnika;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.entiteti.Valuta;


@Database(entities = {DnevnikRada.class, KategorijaTransakcije.class, Korisnik.class, KorisnikoveValute.class, Podsjetnik.class, Racun.class,RadnjaDnevnika.class,
        Transakcija.class, Valuta.class},version=MyDatabase.VERSION,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public static final int VERSION=1;
    public static final String NAME="MoneyMaker";
    private static MyDatabase INSTANCE=null;

    public abstract KorisnikDAO getKorisnikDAO();
    public abstract DnevnikRadaDAO getDnevnikRadaDAO();
    public abstract KategorijaTransakcijeDAO getKategorijaTransakcijeDAO();
    public abstract KorisnikoveValuteDAO getKorisnikoveValuteDAO();
    public abstract PodsjetnikDAO getPodsjetnikDAO();
    public abstract RacunDAO getRacunDAO();
    public abstract RadnjaDnevnikaDAO getRadnjaDnevnika();
    public abstract TransakcijaDAO getTransakcijaDAO();
    public abstract ValutaDAO getValutaDAO();

    public static synchronized MyDatabase getInstance(final Context context){
        if(INSTANCE==null){
            synchronized (MyDatabase.class){
                if(INSTANCE==null){
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            MyDatabase.class,
                            MyDatabase.NAME
                    ).allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}

