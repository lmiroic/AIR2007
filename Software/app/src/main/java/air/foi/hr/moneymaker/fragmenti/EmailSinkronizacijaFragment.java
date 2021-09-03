package air.foi.hr.moneymaker.fragmenti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.modul.transakcije.ISinkronizacijaRacuna;
import air.foi.hr.core.modul.transakcije.SyncInitiator;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import air.foi.hr.moneymaker.session.ISyncRacuna;
import air.foi.hr.moneymaker.session.OnEnteredEmail;

public class EmailSinkronizacijaFragment extends Fragment implements ISyncRacuna {

    private View view;
    private EditText emailAdress;
    private Button ctaSync;
    private String fragmentTag;

    public EmailSinkronizacijaFragment() {
    }

    public EmailSinkronizacijaFragment(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_email_sinkronizacija, container, false);
        this.init();
        return view;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public String dohvatiTag() {
        return getTag();
    }

    private void init() {
        this.emailAdress = view.findViewById(R.id.etEmailAdresaSync);
        this.ctaSync = view.findViewById(R.id.btnCtaEmailSync);
        this.ctaSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmailContent(emailAdress.getText().toString())) {
                    OnEnteredEmail sync = getEmailManager();
                    sync.syncEmailAddress(emailAdress.getText().toString());
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    private boolean checkEmailContent(final String email) {
        return email != null && email != "";
    }

    private OnEnteredEmail getEmailManager() {
        return (OnEnteredEmail) getFragmentManager().findFragmentByTag(fragmentTag);
    }

}