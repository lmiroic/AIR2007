package air.foi.hr.moneymaker.modul.ciljevi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Ciljevi;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.modul.ciljevi.OnDialogCiljeviResult;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.fragmenti.CiljeviFragment;
import air.foi.hr.moneymaker.session.Sesija;

public class CiljeviAddDialog extends Dialog implements View.OnClickListener {

    private EditText nazivCilja;
    private EditText datumCilja;
    private EditText iznosCilja;
    private Spinner kategorijeCilja;
    private Button buttonDodajCilj;

    private CiljeviFragment ciljeviFragment;
    private KategorijaTransakcije odabranaKategorijaTransakcijeCilja;

    private OnDialogCiljeviResult onDialogCiljeviResult;
    SimpleDateFormat formaterDate = new SimpleDateFormat("yyyy-MM-dd");

    public CiljeviAddDialog(@NonNull Context context) {
        super(context);
    }
    public CiljeviAddDialog(@NonNull Context context,CiljeviFragment ciljeviFragment) {
        super(context);
        this.ciljeviFragment=ciljeviFragment;
        this.ciljeviFragment.ciljeviAddDialog=this;
    }

    public void SetOnDialogCiljeciResult(OnDialogCiljeviResult onDialogCiljeviResult) {
        this.onDialogCiljeviResult = onDialogCiljeviResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_ciljevi);
        buttonDodajCilj =findViewById(R.id.btnDodajCilj);
        nazivCilja=findViewById(R.id.txtDodajNazivCilja);
        datumCilja=findViewById(R.id.txtDatumCilja);
        datumCilja.setInputType(InputType.TYPE_CLASS_DATETIME);
        iznosCilja=findViewById(R.id.txtDodajIznosCilja);
        iznosCilja.setInputType(InputType.TYPE_CLASS_NUMBER);
        kategorijeCilja=findViewById(R.id.spinnerDodajKategorijuCilja);

        PostaviSpinnerKategorijaCilja();

        buttonDodajCilj.setOnClickListener(this);
        kategorijeCilja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabranaKategorijaTransakcijeCilja = MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcijePremaNazivu(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void PostaviSpinnerKategorijaCilja() {
        final List<KategorijaTransakcije> kt = MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiSveKategorijeTransakcije();
        final List<String> kategorijeZaSpinner = new ArrayList<>();
        for (KategorijaTransakcije kategorijaTransakcije : kt) {
            kategorijeZaSpinner.add(kategorijaTransakcije.getNaziv());
        }
        if (kategorijeZaSpinner.size() != 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_single_item, kategorijeZaSpinner);
            kategorijeCilja.setAdapter(adapter);
        } else
            Toast.makeText(getContext(), "Morate najprije kreirati kategorije", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnDodajCilj:
                if(!nazivCilja.getText().toString().isEmpty()&&!datumCilja.getText().toString().isEmpty()&&!iznosCilja.getText().toString().isEmpty()){
                    float iznos=Float.parseFloat(iznosCilja.getText().toString());
                    String datum=datumCilja.getText().toString();
                    String naziv=nazivCilja.getText().toString();
                    Date ciljaniDatum = null;
                    try {
                        ciljaniDatum = formaterDate.parse(datum);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Ciljevi noviCilj=new Ciljevi();
                    noviCilj.setDatum(formaterDate.format(ciljaniDatum));
                    noviCilj.setIznos(iznos);
                    noviCilj.setKategorija(odabranaKategorijaTransakcijeCilja.getId());
                    noviCilj.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                    noviCilj.setNaziv(naziv);
                    noviCilj.setOstvarenCilj(false);
                    MyDatabase.getInstance(getContext()).getCiljeviDAO().UnosCilja(noviCilj);
                }
                else
                    Toast.makeText(v.getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_SHORT).show();
                CiljeviAddDialog.this.dismiss();
                break;

        }

    }

}
