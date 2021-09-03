package air.foi.hr.core.modul.transakcije;

import androidx.fragment.app.Fragment;

public interface ISinkronizacijaRacuna {
    Fragment getFragment();
    String getEmail(String email);

}
