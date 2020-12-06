package air.foi.hr.moneymaker.modul.racun;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import air.foi.hr.core.modul.racuni.OnDialogRacunResult;
import air.foi.hr.core.modul.racuni.RacuniImplementor;

public class ConcreteRacun implements RacuniImplementor {

    private String naziv;
    private float pocetnoStanje;
    private String ikona;
    private String valuta;

    public ConcreteRacun() {
    }

    public ConcreteRacun(String naziv, float pocetnoStanje, String ikona, String valuta ) {
        this.naziv = naziv;
        this.ikona=ikona;
        this.pocetnoStanje=pocetnoStanje;
        this.valuta=valuta;
    }

    public ConcreteRacun(String ikona) {
        this.ikona = ikona;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public float getPocetnoStanje() {
        return pocetnoStanje;
    }

    public void setPocetnoStanje(float pocetnoStanje) {
        this.pocetnoStanje = pocetnoStanje;
    }

    public String getIkona() {
        return ikona;
    }

    public void setIkona(String ikona) {
        this.ikona = ikona;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }



    @Override
    public String getImeRacuna() {
        return getNaziv();
    }

    @Override
    public int getIkonaRacuna(Context context) {
        return context.getResources().getIdentifier(getIkona(),"drawable",context.getPackageName());
    }

    @Override
    public float getStanjeRacuna() {
        return getPocetnoStanje();
    }

    @Override
    public void executeAction(Context context) {
        RacunAddDialog racunAddDialog=new RacunAddDialog(context);
        racunAddDialog.setOnDialogRacunResult(new OnDialogRacunResult() {
            @Override
            public void finish() {

            }
        });
        racunAddDialog.show();
    }
}
