package air.foi.hr.moneymaker.modul.prijava;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class GoogleSignIn implements IPrijava {
    private Intent intent;
    private Fragment fragment;
    private Context context;

    public GoogleSignIn(Intent intent, Fragment fragment, Context context) {
        this.intent = intent;
        this.fragment = fragment;
        this.context = context;
    }

    @Override
    public void PrijaviKorisnika(final FragmentManager fragmentManager) {
        fragment.startActivityForResult(intent, 0);
    }
}
