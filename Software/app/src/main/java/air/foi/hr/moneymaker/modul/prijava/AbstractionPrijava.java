package air.foi.hr.moneymaker.modul.prijava;

import androidx.fragment.app.FragmentManager;

public class AbstractionPrijava {
    private IPrijava prijava;

    public AbstractionPrijava(IPrijava prijava) {
        this.prijava = prijava;
    }

    public IPrijava getPrijava() {
        return prijava;
    }

    public void setPrijava(IPrijava prijava) {
        this.prijava = prijava;
    }

    public void PrijaviKorisnika(final FragmentManager fragmentManager) {
        this.prijava.PrijaviKorisnika(fragmentManager);
    }
}
