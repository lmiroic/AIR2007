package air.foi.hr.moneymaker.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.fragmenti.AnalizaFragment;
import air.foi.hr.moneymaker.fragmenti.BarkodFragment;
import air.foi.hr.moneymaker.fragmenti.CiljeviFragment;
import air.foi.hr.moneymaker.fragmenti.EmailSinkronizacijaFragment;
import air.foi.hr.moneymaker.fragmenti.HomeFragment;
import air.foi.hr.moneymaker.fragmenti.KorisnikPostavkeFragment;
import air.foi.hr.moneymaker.fragmenti.PrijavaFragment;
import air.foi.hr.moneymaker.fragmenti.PromjenaLozinkeFragment;
import air.foi.hr.moneymaker.fragmenti.PromjenaValuteFragment;
import air.foi.hr.moneymaker.fragmenti.RacunFragment;
import air.foi.hr.moneymaker.fragmenti.RegistracijaFragment;
import air.foi.hr.moneymaker.fragmenti.SinkronizacijaFragment;
import air.foi.hr.moneymaker.fragmenti.SplashScreenFragment;
import air.foi.hr.moneymaker.fragmenti.TransakcijaFragment;

public class FragmentSwitcher {

    public static final String PRIJAVA_FRAGMENT = "PRIJAVA_FRAGMENT";
    public static final String SPLASH_SCREEN_FRAGMENT = "SPLASH_SCREEN_FRAGMENT";
    public static final String HOME_FRAGMENT = "HOME_FRAGMENT";
    public static final String REGISTRACIJA_FRAGMENT = "REGISTRACIJA_FRAGMENT";
    public static final String RACUN_FRAGMENT = "RACUN_FRAGMENT";
    public static final String TRANSAKCIJA_FRAGMENT = "TRANSAKCIJA_FRAGMENT";
    public static final String ANALIZA_FRAGMENT = "ANALIZA_FRAGMENT";
    public static final String POSTAVKE_FRAGMENT = "POSTAVKE_FRAGMENT";
    public static final String PROMJENA_LOZINKE_FRAGMENT = "PROMJENA_LOZINKE_FRAGMENT";
    public static final String PROMJENA_VALUTE_FRAGMENT = "PROMJENA_VALUTE_FRAGMENT";
    public static final String CILJEVI_FRAGMENT = "CILJEVI_FRAGMENT";
    public static final String BARCODE_READER_FRAGMENT = "BARCODE_READER_FRAGMENT";
    public static final String SINKRONIZACIJA_FRAGMENT = "SINKRONIZACIJA_FRAGMENT";
    public static final String EMAIL_SINKRONIZACIJA =  "EMAIL_SINKRONIZACIJA";

    public static void ShowFragment(FragmentName fragmentName, AppCompatActivity activity) {
        Fragment fragment = null;
        switch (fragmentName) {
            case PRIJAVA:
                fragment = new PrijavaFragment();
                break;
            case SPLASH_SCREEN:
                fragment = new SplashScreenFragment();
                break;
            case HOME:
                fragment = new HomeFragment();
                break;
            case REGISTRACIJA:
                fragment = new RegistracijaFragment();
                break;
            case RACUN:
                fragment = new RacunFragment();
                break;
            case TRANSAKCIJA:
                fragment = new TransakcijaFragment();
                break;
            case ANALIZA:
                fragment = new AnalizaFragment();
                break;
            case POSTAVKE:
                fragment = new KorisnikPostavkeFragment();
                break;
            case PROMJENA_LOZINKE:
                fragment = new PromjenaLozinkeFragment();
                break;
            case PROMJENA_VALUTE:
                fragment = new PromjenaValuteFragment();
                break;
            case CILJEVI:
                fragment = new CiljeviFragment();
                break;
            case BARCODE_READER:
                fragment = new BarkodFragment();
                break;
            case SINKRONIZACIJA:
                fragment = new SinkronizacijaFragment();
                break;
            case EMAIL_SINKRONIZACIJA:
                fragment = new EmailSinkronizacijaFragment();
                break;
        }
        FragmentManager fm = activity.getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fmMain, fragment, getFragmentTag(fragmentName))
                .addToBackStack(getFragmentTag(fragmentName))
                .commit();
    }

    public static void ShowFragment(FragmentName fragmentName, FragmentManager fm) {
        Fragment fragment = null;
        switch (fragmentName) {
            case PRIJAVA:
                fragment = new PrijavaFragment();
                break;
            case SPLASH_SCREEN:
                fragment = new SplashScreenFragment();
                break;
            case HOME:
                fragment = new HomeFragment();
                break;
            case REGISTRACIJA:
                fragment = new RegistracijaFragment();
                break;
            case RACUN:
                fragment = new RacunFragment();
                break;
            case TRANSAKCIJA:
                fragment = new TransakcijaFragment();
                break;
            case ANALIZA:
                fragment = new AnalizaFragment();
                break;
            case POSTAVKE:
                fragment = new KorisnikPostavkeFragment();
                break;
            case PROMJENA_LOZINKE:
                fragment = new PromjenaLozinkeFragment();
                break;
            case PROMJENA_VALUTE:
                fragment = new PromjenaValuteFragment();
                break;
            case CILJEVI:
                fragment = new CiljeviFragment();
                break;
            case BARCODE_READER:
                fragment = new BarkodFragment();
                break;
            case SINKRONIZACIJA:
                fragment = new SinkronizacijaFragment();
                break;
            case EMAIL_SINKRONIZACIJA:
                fragment = new EmailSinkronizacijaFragment();
                break;
        }
        fm.beginTransaction()
                .replace(R.id.fmMain, fragment, getFragmentTag(fragmentName))
                .addToBackStack(getFragmentTag(fragmentName))
                .commit();
    }

    private static String getFragmentTag(FragmentName fragmentName){
        String fragmentTag = "";
        switch (fragmentName){
            case PRIJAVA:
                fragmentTag = PRIJAVA_FRAGMENT;
                break;
            case SPLASH_SCREEN:
                fragmentTag = SPLASH_SCREEN_FRAGMENT;
                break;
            case HOME:
                fragmentTag = HOME_FRAGMENT;
                break;
            case REGISTRACIJA:
                fragmentTag = REGISTRACIJA_FRAGMENT;
                break;
            case RACUN:
                fragmentTag = RACUN_FRAGMENT;
                break;
            case TRANSAKCIJA:
                fragmentTag = TRANSAKCIJA_FRAGMENT;
                break;
            case ANALIZA:
                fragmentTag = ANALIZA_FRAGMENT;
                break;
            case POSTAVKE:
                fragmentTag = POSTAVKE_FRAGMENT;
                break;
            case PROMJENA_LOZINKE:
                fragmentTag = PROMJENA_LOZINKE_FRAGMENT;
                break;
            case PROMJENA_VALUTE:
                fragmentTag = PROMJENA_VALUTE_FRAGMENT;
                break;
            case CILJEVI:
                fragmentTag = CILJEVI_FRAGMENT;
                break;
            case BARCODE_READER:
                fragmentTag = BARCODE_READER_FRAGMENT;
                break;
            case SINKRONIZACIJA:
                fragmentTag = SINKRONIZACIJA_FRAGMENT;
                break;
            case EMAIL_SINKRONIZACIJA:
                fragmentTag = EMAIL_SINKRONIZACIJA;
                break;
        }
        return fragmentTag;
    }
}
