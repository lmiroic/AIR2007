package air.foi.hr.core.modul.racuni;

public class ConcreteRacun implements RacuniImplementor{

    public String naziv;
    public float pocetnoStanje;
    public String valuta;


    @Override
    public String getImeRacuna() {
        return null;
    }

    @Override
    public int getIkonaRacuna() {
        return 0;
    }

    @Override
    public float getStanjeRacuna() {
        return 0;
    }

    @Override
    public void executeAction() {

    }
}
