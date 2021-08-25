package air.foi.hr.moneymaker.modul.transakcije;

import android.app.DatePickerDialog;
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

import com.google.android.gms.common.api.Api;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.modul.transakcije.OnDialogTransactionResult;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.fragmenti.TransakcijaFragment;
import air.foi.hr.moneymaker.manager.CustomAdapterTransakcije;

public class TransactionTrosakDialog extends Dialog implements android.view.View.OnClickListener {

    private OnDialogTransactionResult onDialogTransactionResult;
    private EditText datumTrosak;
    private EditText iznosTrosak;
    private EditText opisTrosak;
    private Spinner odabirKategorijeTrosak;
    private Spinner odabirRacunaTrosak;
    private Button btnSlikajTrosak;
    public ImageView imageViewTrosak;
    private Button btnUreduTrosak;
    private Transakcija transakcija;
    private CustomAdapterTransakcije customAdapterTransakcije;
    private TransakcijaFragment transakcijaFragment;
    private KategorijaTransakcije odabranaKategorijaTransakcije;
    private Racun odabraniRacun;



    public TransactionTrosakDialog(@NonNull Context context) {
        super(context);
    }

    public TransactionTrosakDialog(@NonNull Context context, Transakcija transakcija) {
        super(context);
        this.transakcija=transakcija;
    }

    public TransactionTrosakDialog(@NonNull Context context, TransakcijaFragment transakcijaFragment) {
        super(context);
        this.transakcijaFragment =transakcijaFragment;
        this.transakcijaFragment.transactionTrosakDialog=this;
    }

    public void SetOnDialogTransactionResult(OnDialogTransactionResult onDialogTransactionResult){
        this.onDialogTransactionResult=onDialogTransactionResult;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_trosak);
        datumTrosak=findViewById(R.id.txtDatumTrosak);
        iznosTrosak=findViewById(R.id.txtOdaberiIznosTrosak);
        opisTrosak=findViewById(R.id.txtOpisTrosak);
        odabirKategorijeTrosak=findViewById(R.id.cmbOdabirKategorijeTrosak);
        odabirRacunaTrosak=findViewById(R.id.cmbOdaberiteRacunTrosak);
        btnSlikajTrosak=findViewById(R.id.btnSlikajTrosak);
        imageViewTrosak=findViewById(R.id.imageViewTrosak);
        btnUreduTrosak=findViewById(R.id.buttonUreduTrosak);

        btnUreduTrosak.setOnClickListener(this);
        btnSlikajTrosak.setOnClickListener(this);
        odabirKategorijeTrosak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabranaKategorijaTransakcije = MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcijePremaNazivu(parent.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        odabirRacunaTrosak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        odabirKategorijeTrosak.setAdapter(adapter);
    }
    private void PostavispinnerRacuna(){
        final List<Racun> sviRacuni=MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiSveRacune();
        final List<String> racuniZaSpinner=new ArrayList<>();
        for(Racun racun:sviRacuni){
            racuniZaSpinner.add(racun.getNaziv());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_single_item, racuniZaSpinner);
        odabirRacunaTrosak.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSlikajTrosak:
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                transakcijaFragment.startActivityForResult(intent,5);
                imageViewTrosak.setImageURI(transakcijaFragment.slika);
                break;
            case R.id.buttonUreduTrosak:
                float iznos= Float.parseFloat(iznosTrosak.getText().toString());
                String datum=datumTrosak.getText().toString();
                String opis=opisTrosak.getText().toString();
                int odabranRacun=this.odabraniRacun.getId();
                int kategorijaTroska=this.odabranaKategorijaTransakcije.getId();
                Transakcija transakcija=new Transakcija();
                transakcija.setIznos(iznos);
                transakcija.setDatum(datum);
                transakcija.setOpis(opis);
                transakcija.setRacunTerecenja(odabranRacun);
                transakcija.setTipTransakcije(kategorijaTroska);
                MyDatabase.getInstance(getContext()).getTransakcijaDAO().UnosTransakcije(transakcija);

                Log.e("transakcija",transakcija.toString());
                Toast.makeText(v.getContext(), "uso ko kumi",Toast.LENGTH_LONG).show();
                TransactionTrosakDialog.this.dismiss();
                break;
        }

    }

}