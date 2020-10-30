package air.foi.hr.moneymaker.session;

import air.foi.hr.core.entiteti.Korisnik;

public class Sesija {
    private static Sesija INSTANCE=null;
    private Korisnik korisnik;
    public static Sesija getInstance(){
        if(INSTANCE==null){
            synchronized (Sesija.class){
                if(INSTANCE==null){
                    INSTANCE= new Sesija();
                }
            }
        }
        return INSTANCE;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}
