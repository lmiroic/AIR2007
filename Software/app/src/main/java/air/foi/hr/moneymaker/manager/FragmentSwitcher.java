package air.foi.hr.moneymaker.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.fragmenti.HomeFragment;
import air.foi.hr.moneymaker.fragmenti.PrijavaFragment;
import air.foi.hr.moneymaker.fragmenti.SplashScreenFragment;

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
        }
        fm.beginTransaction().replace(R.id.fmMain,fragment).commit();
    }
}
