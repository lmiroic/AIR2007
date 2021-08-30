package air.foi.hr.moneymaker.fragmenti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import air.foi.hr.core.modul.transakcije.ISinkronizacijaRacuna;
import air.foi.hr.core.modul.transakcije.SyncInitiator;
import air.foi.hr.moneymaker.R;

public class EmailSinkronizacijaFragment extends Fragment implements ISinkronizacijaRacuna {

    private View view;
    private EditText emailAdress;
    private Button ctaSync;

    public EmailSinkronizacijaFragment() {
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

    private void init() {
        this.emailAdress = view.findViewById(R.id.etEmailAdresaSync);
        this.ctaSync = view.findViewById(R.id.btnCtaEmailSync);
        this.ctaSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkEmailContent(emailAdress.getText().toString())){
                    SyncInitiator initiator = (SyncInitiator) getContext();
                    if(initiator != null){
                        initiator.initiateSync(emailAdress.getText().toString());
                    }
                }
            }
        });
    }

    private boolean checkEmailContent(final String email) {
        return email != null && email != "";
    }

}