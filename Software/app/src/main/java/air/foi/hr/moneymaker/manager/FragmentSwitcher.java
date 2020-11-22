package air.foi.hr.moneymaker.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.fragmenti.AnalizaFragment;
import air.foi.hr.moneymaker.fragmenti.HomeFragment;
import air.foi.hr.moneymaker.fragmenti.PrijavaFragment;
import air.foi.hr.moneymaker.fragmenti.RacunFragment;
import air.foi.hr.moneymaker.fragmenti.RegistracijaFragment;
import air.foi.hr.moneymaker.fragmenti.SplashScreenFragment;
import air.foi.hr.moneymaker.fragmenti.TransakcijaFragment;

public class FragmentSwitcher {
    public static void ShowFragment(FragmentName fragmentName, AppCompatActivity activity){
        Fragment fragment=null;
        switch (fragmentName){
            case PRIJAVA:
                fragment= new PrijavaFragment();
                break;
            case SPLASH_SCREEN:
                fragment = new SplashScreenFragment();
                break;
            case HOME:
                fragment=new HomeFragment();
                break;
            case REGISTRACIJA:
                fragment=new RegistracijaFragment();
                break;
            case RACUN:
                fragment=new RacunFragment();
                break;
            case TRANSAKCIJA:
                fragment=new TransakcijaFragment();
                break;
            case ANALIZA:
                fragment=new AnalizaFragment();
                break;
        }
        FragmentManager fm= activity.getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fmMain,fragment).commit();
    }
    public static void ShowFragment(FragmentName fragmentName, FragmentManager fm){
        Fragment fragment=null;
        switch (fragmentName){
            case PRIJAVA:
                fragment= new PrijavaFragment();
                break;
            case SPLASH_SCREEN:
                fragment = new SplashScreenFragment();
                break;
            case HOME:
                fragment=new HomeFragment();
                break;
            case REGISTRACIJA:
                fragment=new RegistracijaFragment();
                break;
            case RACUN:
                fragment=new RacunFragment();
                break;
            case TRANSAKCIJA:
                fragment=new TransakcijaFragment();
                break;
            case ANALIZA:
                fragment=new AnalizaFragment();
                break;
        }
        fm.beginTransaction().replace(R.id.fmMain,fragment).commit();
    }
}
