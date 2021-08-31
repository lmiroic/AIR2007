package air.foi.hr.moneymaker.fragmenti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.RegistracijaFragmentViewModel;


public class RegistracijaFragment extends Fragment {
    public View view;
    private EditText Ime;
    private EditText Prezime;
    private EditText Email;
    private EditText Lozinka;
    private Button btnRegistracija;
    private RegistracijaFragmentViewModel viewModel;

    public RegistracijaFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registracija, container, false);
        InicijalizacijaVarijabli();
        return view;
    }


    private void InicijalizacijaVarijabli() {
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(RegistracijaFragmentViewModel.class);
        viewModel.konstruktor(getContext());
        Ime = view.findViewById(R.id.editTextIme);
        Prezime = view.findViewById(R.id.editTextPrezime);
        Email = view.findViewById(R.id.editTextEmail);
        Lozinka = view.findViewById(R.id.editTextLozinka);
        btnRegistracija = view.findViewById(R.id.btnRegistracija);
        btnRegistracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.RegistrirajKorisnika(Ime.getText().toString(), Prezime.getText().toString(), Email.getText().toString(), Lozinka.getText().toString(), getFragmentManager());
            }
        });
    }
}