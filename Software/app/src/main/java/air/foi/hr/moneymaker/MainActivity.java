package air.foi.hr.moneymaker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.fragmenti.SplashScreenFragment;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!ProvjeraInterneta(this)){
            Toast.makeText(this,"No Internet connection. Connect to an available network and try again.",Toast.LENGTH_LONG).show();
            finish(); //Calling this method to close this activity when internet is not available.
        }
        prikaziSplashScreen();
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




         */
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.bottomNav) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static boolean ProvjeraInterneta(Context context){
        ConnectivityManager conMan=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())?true:false;
    }


}