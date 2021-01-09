package air.foi.hr.core.database;

import android.content.Context;

import air.foi.hr.core.database.dao.KategorijaTransakcijeDAO;
import air.foi.hr.core.database.dao.RacunDAO;
import air.foi.hr.core.database.dao.TransakcijaDAO;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Transakcija;

public class MockData {
    private static TransakcijaDAO dao;
    private static RacunDAO daoR;


    public static void writeAll(Context context){
        dao=MyDatabase.getInstance(context).getTransakcijaDAO();


        Transakcija transakcija = new Transakcija();
        transakcija.setId(1);
        transakcija.setIznos(1000);
        transakcija.setDatum("2020-12-03");
        transakcija.setRacunTerecenja(4);
        transakcija.setRacunPrijenosa(4);
        transakcija.setTipTransakcije(2);
        transakcija.setMemo("memo");
        transakcija.setOpis("Opis");
        transakcija.setPonavljajuciTrosak(false);
        transakcija.setIkona("ikona");
        transakcija.setBoja("Boja");
        transakcija.setKorisnik(1);
        transakcija.setDoDatuma("20.10.2010");
        transakcija.setIntervalPonavljanja(1);
        transakcija.setKategorijaTransakcije(1);


        Transakcija transakcijaP = new Transakcija();
        transakcijaP.setId(505);
        transakcijaP.setIznos(500);
        transakcijaP.setDatum("2021-01-05");
        transakcijaP.setRacunTerecenja(4);
        transakcijaP.setRacunPrijenosa(4);
        transakcijaP.setTipTransakcije(1);
        transakcijaP.setMemo("memo");
        transakcijaP.setOpis("Opis");
        transakcijaP.setPonavljajuciTrosak(false);
        transakcijaP.setIkona("ikona");
        transakcijaP.setBoja("Boja");
        transakcijaP.setKorisnik(1);
        transakcijaP.setDoDatuma("20.10.2010");
        transakcijaP.setIntervalPonavljanja(1);
        transakcijaP.setKategorijaTransakcije(5);


        Transakcija transakcija2 = new Transakcija();
        transakcija2.setId(2);
        transakcija2.setIznos(2000);
        transakcija2.setDatum("2021-01-07");
        transakcija2.setRacunTerecenja(1);
        transakcija2.setRacunPrijenosa(1);
        transakcija2.setTipTransakcije(2);
        transakcija2.setMemo("memo");
        transakcija2.setOpis("Opis");
        transakcija2.setPonavljajuciTrosak(false);
        transakcija2.setIkona("ikona");
        transakcija2.setBoja("Boja");
        transakcija2.setKorisnik(1);
        transakcija2.setDoDatuma("20.10.2010");
        transakcija2.setIntervalPonavljanja(1);
        transakcija2.setKategorijaTransakcije(1);

        Transakcija transakcija3 = new Transakcija();
        transakcija3.setId(3);
        transakcija3.setIznos(350);
        transakcija3.setDatum("2021-01-05");
        transakcija3.setRacunTerecenja(1);
        transakcija3.setRacunPrijenosa(1);
        transakcija3.setTipTransakcije(1);
        transakcija3.setMemo("memo");
        transakcija3.setOpis("Opis");
        transakcija3.setPonavljajuciTrosak(false);
        transakcija3.setIkona("ikona");
        transakcija3.setBoja("Boja");
        transakcija3.setKorisnik(1);
        transakcija3.setDoDatuma("20.10.2010");
        transakcija3.setIntervalPonavljanja(1);
        transakcija3.setKategorijaTransakcije(5);

        Transakcija transakcija4 = new Transakcija();
        transakcija4.setId(656);
        transakcija4.setIznos(700);
        transakcija4.setDatum("2021-01-04");
        transakcija4.setRacunTerecenja(1);
        transakcija4.setRacunPrijenosa(4);
        transakcija4.setTipTransakcije(2);
        transakcija4.setMemo("memo");
        transakcija4.setOpis("Opis");
        transakcija4.setPonavljajuciTrosak(false);
        transakcija4.setIkona("ikona");
        transakcija4.setBoja("Boja");
        transakcija4.setKorisnik(1);
        transakcija4.setDoDatuma("20.10.2010");
        transakcija4.setIntervalPonavljanja(1);
        transakcija4.setKategorijaTransakcije(4);

        Transakcija transakcija5 = new Transakcija();
        transakcija5.setId(545);
        transakcija5.setIznos(600);
        transakcija5.setDatum("2021-01-05");
        transakcija5.setRacunTerecenja(1);
        transakcija5.setRacunPrijenosa(4);
        transakcija5.setTipTransakcije(2);
        transakcija5.setMemo("memo");
        transakcija5.setOpis("Opis");
        transakcija5.setPonavljajuciTrosak(false);
        transakcija5.setIkona("ikona");
        transakcija5.setBoja("Boja");
        transakcija5.setKorisnik(1);
        transakcija5.setDoDatuma("20.10.2010");
        transakcija5.setIntervalPonavljanja(1);
        transakcija5.setKategorijaTransakcije(3);





        //dao.IzbrisiTransakciju(transakcija,transakcija2,transakcija3,transakcija4,transakcija5, transakcijaP);

        dao.UnosTransakcije(transakcija,transakcija2,transakcija3, transakcijaP, transakcija4, transakcija5);


    }

    public static void writeAllR(Context context){

        daoR=MyDatabase.getInstance(context).getRacunDAO();
        Racun racuni = new Racun();
        racuni.setId(1);
        racuni.setIkona("ikona");
        racuni.setNaziv("Racun1");
        racuni.setKorisnik_id(1);
        racuni.setPocetno_stanje(1000);
        racuni.setValuta("HRK");


        Racun racuni2 = new Racun();
        racuni2.setId(2);
        racuni2.setIkona("ikona");
        racuni2.setNaziv("Racun2");
        racuni2.setKorisnik_id(1);
        racuni2.setPocetno_stanje(1000);
        racuni2.setValuta("HRK");

        daoR.UnosRacuna(racuni,racuni2);
        //daoR.IzbrisiRacun(racuni,racuni2);

    }

}
