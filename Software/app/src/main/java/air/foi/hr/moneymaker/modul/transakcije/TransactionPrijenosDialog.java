package air.foi.hr.moneymaker.modul.transakcije;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.modul.transakcije.OnDialogTransactionResult;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.fragmenti.TransakcijaFragment;
import air.foi.hr.moneymaker.manager.CustomAdapterTransakcije;

public class TransactionPrijenosDialog extends Dialog implements android.view.View.OnClickListener {

    private OnDialogTransactionResult onDialogTransactionResult;
    private EditText datumPrijenos;
    private EditText iznosPrijenos;
    private EditText opisPrijenos;
    private Spinner odabirIzRacuna;
    private Spinner odabirURacun;
    private Button btnSlikajPrijenos;
    public ImageView imageViewPrijenos;
    private Button btnUreduPrijenos;
    private Transakcija transakcija;
    private CustomAdapterTransakcije customAdapterTransakcije;
    private TransakcijaFragment transakcijaFragment;
    private Racun odabiruRacun;
    private Racun odabirizRacuna;

    public TransactionPrijenosDialog(@NonNull Context context) {
        super(context);
    }

    public TransactionPrijenosDialog(@NonNull Context context, TransakcijaFragment transakcijaFragment) {
        super(context);
        this.transakcijaFragment=transakcijaFragment;
        this.transakcijaFragment.transactionPrijenosDialog=this;
    }

    public void SetOnDialogTransactionResult(OnDialogTransactionResult onDialogTransactionResult){
        this.onDialogTransactionResult=onDialogTransactionResult;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_prijenos);
        datumPrijenos=findViewById(R.id.txtDatumPrijenos);
        iznosPrijenos=findViewById(R.id.txtOdaberiIznosPrijenos);
        opisPrijenos=findViewById(R.id.txtOpisPrijenos);
        odabirIzRacuna=findViewById(R.id.cmbIzRacunaPrijenos);
        odabirURacun=findViewById(R.id.cmbOdaberiteRacun);
        btnSlikajPrijenos=findViewById(R.id.btnSlikajPrijenos);
        imageViewPrijenos=findViewById(R.id.imageViewPrijenos);
        btnUreduPrijenos=findViewById(R.id.buttonUreduPrijenos);

        btnUreduPrijenos.setOnClickListener(this);
        btnSlikajPrijenos.setOnClickListener(this);

        odabirIzRacuna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabiruRacun=MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacunPoNazivu(parent.getItemAtPosition(position).toString());            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        odabirURacun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabirizRacuna=MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacunPoNazivu(parent.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        PostavispinnerIzRacuna();
        PostavispinnerURacuna();

    }
    private void PostavispinnerIzRacuna(){
        final List<Racun> sviRacuni=MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiSveRacune();
        final List<String> racuniZaSpinner=new ArrayList<>();
        for(Racun racun:sviRacuni){
            racuniZaSpinner.add(racun.getNaziv());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_single_item, racuniZaSpinner);
        odabirIzRacuna.setAdapter(adapter);
    }

    private void PostavispinnerURacuna(){
        final List<Racun> sviRacuni= MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiSveRacune();
        final List<String> racuniZaSpinner=new ArrayList<>();
        for(Racun racun:sviRacuni){
            racuniZaSpinner.add(racun.getNaziv());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_single_item, racuniZaSpinner);
        odabirURacun.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSlikajPrijenos:
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                transakcijaFragment.startActivityForResult(intent,7);
                imageViewPrijenos.setImageURI(transakcijaFragment.slika);
                break;
            case R.id.buttonUreduPrijenos:
                float iznos= Float.parseFloat(iznosPrijenos.getText().toString());
                String datum=datumPrijenos.getText().toString();
                String opis=opisPrijenos.getText().toString();
                int odabranURacun=this.odabirURacun.getId();
                int odabranIzRacuna=this.odabirIzRacuna.getId();
                Transakcija transakcija=new Transakcija();
                transakcija.setIznos(iznos);
                transakcija.setDatum(datum);
                transakcija.setOpis(opis);
                transakcija.setRacunTerecenja(odabranIzRacuna);
                transakcija.setRacunPrijenosa(odabranURacun);

                Log.e("transakcija",transakcija.toString());
                Toast.makeText(v.getContext(), "uso ko kumi",Toast.LENGTH_LONG).show();
                this.hide();
                break;
        }

    }
}