package air.foi.hr.moneymaker.manager;

import air.foi.hr.core.entiteti.Valuta;

public class Mjenjacnica {
    public static float PromjeniValute(Valuta valutaRacunaTerecenja,Valuta valutaRacunaPrijenos,float iznos){
        float racunTerecenjaIznos=!valutaRacunaTerecenja.getNaziv().equals("HRK")?valutaRacunaTerecenja.getTecaj()*iznos:iznos;

        float racunPrijenosaIznos=!valutaRacunaPrijenos.getNaziv().equals("HRK")?racunTerecenjaIznos/valutaRacunaPrijenos.getTecaj():iznos;

        return racunPrijenosaIznos;
    }
}
