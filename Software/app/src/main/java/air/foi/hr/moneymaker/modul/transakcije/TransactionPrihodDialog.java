package air.foi.hr.moneymaker.modul.transakcije;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceControl;
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
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.modul.transakcije.OnDialogTransactionResult;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.fragmenti.TransakcijaFragment;
import air.foi.hr.moneymaker.manager.CustomAdapterTransakcije;

public class TransactionPrihodDialog extends Dialog implements android.view.View.OnClickListener {

    private OnDialogTransactionResult onDialogTransactionResult;
    private EditText datumPrihod;
    private EditText iznosPrihod;
    private EditText opisPrihod;
    private Spinner odabirKategorijePrihod;
    private Spinner odabirRacunaPrihod;
    private Button btnSlikajPrihod;
    public ImageView imageViewPrihod;
    private Button btnUreduPrihod;
    private Transakcija transakcija;
    private CustomAdapterTransakcije customAdapterTransakcije;
    private TransakcijaFragment transakcijaFragment;
    private KategorijaTransakcije odabranaKategorijaTransakcije;
    private Racun odabraniRacun;


    public TransactionPrihodDialog(@NonNull Context context) {
        super(context);
    }

    public TransactionPrihodDialog(@NonNull Context context, TransakcijaFragment transakcijaFragment) {
        super(context);
        this.transakcijaFragment=transakcijaFragment;
        this.transakcijaFragment.transactionPrihodDialog=this;

    }

    public void SetOnDialogTransationResult(OnDialogTransactionResult onDialogTransactionResult){
        this.onDialogTransactionResult=onDialogTransactionResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_prihod);
        datumPrihod=findViewById(R.id.txtDatum);
        iznosPrihod=findViewById(R.id.txtOdaberiIznos);
        opisPrihod=findViewById(R.id.txtOpis);
        odabirKategorijePrihod=findViewById(R.id.cmbOdabirKategorije);
        odabirRacunaPrihod=findViewById(R.id.cmbOdaberiteRacun);
        btnSlikajPrihod=findViewById(R.id.btnSlikaj);
        imageViewPrihod=findViewById(R.id.imageViewPrihod);
        btnUreduPrihod=findViewById(R.id.btnUreduPrihod);

        btnUreduPrihod.setOnClickListener(this);
        btnSlikajPrihod.setOnClickListener(this);

        odabirKategorijePrihod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabranaKategorijaTransakcije= MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcijePremaNazivu(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        odabirRacunaPrihod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabraniRacun=MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacunPoNazivu(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        PostaviSpinnerKategorija();
        PostavispinnerRacuna();

    }
    private void PostaviSpinnerKategorija(){
        final List<KategorijaTransakcije> kt= MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiSveKategorijeTransakcije();
        final List<String>kategorijeZaSpinner=new ArrayList<>();
        for (KategorijaTransakcije kategorijaTransakcije:kt){
            kategorijeZaSpinner.add(kategorijaTransakcije.getNaziv());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_single_item, kategorijeZaSpinner);
        odabirKategorijePrihod.setAdapter(adapter);
    }
    private void PostavispinnerRacuna(){
        final List<Racun> sviRacuni=MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiSveRacune();
        final List<String> racuniZaSpinner=new ArrayList<>();
        for(Racun racun:sviRacuni){
            racuniZaSpinner.add(racun.getNaziv());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_single_item, racuniZaSpinner);
        odabirRacunaPrihod.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSlikaj:
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                transakcijaFragment.startActivityForResult(intent,6);
                imageViewPrihod.setImageURI(transakcijaFragment.slika);
                break;

            case R.id.btnUreduPrihod:
                float iznos= Float.parseFloat(iznosPrihod.getText().toString());
                String datum=datumPrihod.getText().toString();
                String opis=opisPrihod.getText().toString();
                int odabranRacun=this.odabraniRacun.getId();
                int kategorijaTroska=this.odabranaKategorijaTransakcije.getId();
                Transakcija transakcija=new Transakcija();
                transakcija.setIznos(iznos);
                transakcija.setDatum(datum);
                transakcija.setOpis(opis);
                transakcija.setRacunTerecenja(odabranRacun);
                transakcija.setTipTransakcije(kategorijaTroska);
                Log.e("transakcija",transakcija.toString());
                Toast.makeText(v.getContext(), "uso ko kumi",Toast.LENGTH_LONG).show();
                this.hide();
                break;
        }
    }

}