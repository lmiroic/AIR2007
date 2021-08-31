package air.foi.hr.moneymaker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.modul.transakcije.SyncInitiator;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import air.foi.hr.moneymaker.session.SinkronizacijaBazePodataka;

public class MainActivity extends AppCompatActivity implements SyncInitiator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!ProvjeraInterneta(this)) {
            Toast.makeText(this, "No Internet connection. Connect to an available network and try again.", Toast.LENGTH_LONG).show();
            finish(); //Calling this method to close this activity when internet is not available.
        }
        prikaziSplashScreen();

    }

    private void prikaziSplashScreen() {
        FragmentSwitcher.ShowFragment(FragmentName.SPLASH_SCREEN, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.bottomNav) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean ProvjeraInterneta(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected()) ? true : false;
    }


    @Override
    public void initiateSync(String email) {
        SinkronizacijaBazePodataka sbp = new SinkronizacijaBazePodataka(MainActivity.this);
        sbp.sinkroniziraj(email);
    }
}