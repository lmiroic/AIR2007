package air.foi.hr.moneymaker.modul.transakcije;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.modul.transakcije.OnDialogTransactionResult;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.fragmenti.TransakcijaFragment;
import air.foi.hr.moneymaker.manager.CustomAdapterTransakcije;
import air.foi.hr.moneymaker.session.Sesija;

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
    private Button btnAzurirajTrosak;
    private int iznosTransakcije;


    public TransactionTrosakDialog(@NonNull Context context) {
        super(context);
    }

    public TransactionTrosakDialog(@NonNull Context context, Transakcija transakcija) {
        super(context);
        this.transakcija = transakcija;
    }

    public TransactionTrosakDialog(@NonNull Context context, TransakcijaFragment transakcijaFragment) {
        super(context);
        this.transakcijaFragment = transakcijaFragment;
        this.transakcijaFragment.transactionTrosakDialog = this;
    }

    public void SetOnDialogTransactionResult(OnDialogTransactionResult onDialogTransactionResult) {
        this.onDialogTransactionResult = onDialogTransactionResult;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_trosak);
        btnAzurirajTrosak = findViewById(R.id.btnAzurirajTrosak);
        btnAzurirajTrosak.setVisibility(View.INVISIBLE);
        btnAzurirajTrosak.setEnabled(false);
        datumTrosak = findViewById(R.id.txtDatumTrosak);
        datumTrosak.setInputType(InputType.TYPE_CLASS_DATETIME);
        iznosTrosak = findViewById(R.id.txtOdaberiIznosTrosak);
        iznosTrosak.setInputType(InputType.TYPE_CLASS_NUMBER);
        opisTrosak = findViewById(R.id.txtOpisTrosak);
        odabirKategorijeTrosak = findViewById(R.id.cmbOdabirKategorijeTrosak);
        odabirRacunaTrosak = findViewById(R.id.cmbOdaberiteRacunTrosak);
        btnSlikajTrosak = findViewById(R.id.btnSlikajTrosak);
        imageViewTrosak = findViewById(R.id.imageViewTrosak);
        btnUreduTrosak = findViewById(R.id.buttonUreduTrosak);
        PostaviSpinnerKategorija();
        PostavispinnerRacuna();
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
                odabraniRacun = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacunPoNazivu(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (transakcija != null) {
            datumTrosak.setText(transakcija.getDatum());
            iznosTrosak.setText(String.valueOf(transakcija.getIznos()));
            opisTrosak.setText(transakcija.getOpis());
            odabirKategorijeTrosak.setSelection(OdaberiSpinnerKategorije());
            odabirRacunaTrosak.setSelection(OdaberiSpinnerRacuna());
            iznosTransakcije=(int)transakcija.getIznos();
            btnUreduTrosak.setEnabled(false);
            btnUreduTrosak.setVisibility(View.INVISIBLE);

            btnAzurirajTrosak.setEnabled(true);
            btnAzurirajTrosak.setVisibility(View.VISIBLE);
            btnAzurirajTrosak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(transakcija!=null){
                        if (!iznosTrosak.getText().toString().isEmpty() && !datumTrosak.getText().toString().isEmpty() && !opisTrosak.getText().toString().isEmpty()) {
                            float iznos = Float.parseFloat(iznosTrosak.getText().toString());
                            String datum = datumTrosak.getText().toString();
                            String opis = opisTrosak.getText().toString();
                            int odabranRacun = odabraniRacun.getId();
                            if(transakcija.getIznos()<iznos){
                                if (odabraniRacun.getPocetno_stanje() >= iznos-transakcija.getIznos()) {
                                    transakcija.setIznos(iznos);
                                    transakcija.setDatum(datum);
                                    transakcija.setOpis(opis);
                                    transakcija.setRacunTerecenja(odabranRacun);
                                    transakcija.setTipTransakcije(2);
                                    transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                                    transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                                    MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(transakcija);

                                    Racun racunZaAzuriranje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabraniRacun.getId());
                                    float pocetnoStanje = racunZaAzuriranje.getPocetno_stanje();
                                    racunZaAzuriranje.setPocetno_stanje(pocetnoStanje - (iznos-iznosTransakcije));
                                    Log.e("racun",String.valueOf(pocetnoStanje-(iznos-iznosTransakcije)));
                                    MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranje);

                                    Log.e("transakcija", transakcija.toString());
                                    Toast.makeText(v.getContext(), "Uspješno ažurirana transakcija troška", Toast.LENGTH_SHORT).show();
                                    TransactionTrosakDialog.this.dismiss();
                                } else
                                    Toast.makeText(v.getContext(), "Nemate dovoljno novaca na računu", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                transakcija.setIznos(iznos);
                                transakcija.setDatum(datum);
                                transakcija.setOpis(opis);
                                transakcija.setRacunTerecenja(odabranRacun);
                                transakcija.setTipTransakcije(2);
                                transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                                transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                                MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(transakcija);

                                Racun racunZaAzuriranje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabraniRacun.getId());
                                float pocetnoStanje = racunZaAzuriranje.getPocetno_stanje();
                                racunZaAzuriranje.setPocetno_stanje(pocetnoStanje + (iznosTransakcije-iznos));
                                MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranje);

                                Log.e("transakcija", transakcija.toString());
                                Toast.makeText(v.getContext(), "Uspješno ažurirana transakcija troška", Toast.LENGTH_SHORT).show();
                                TransactionTrosakDialog.this.dismiss();
                            }

                        } else
                            Toast.makeText(v.getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_SHORT).show();
                    }
                    TransactionTrosakDialog.this.dismiss();
                }
            });
            //-----POTREBNO NAPRAVITI
            //imageViewTrosak.

        }


    }
    private int OdaberiSpinnerKategorije(){
        for(int i =0; i<odabirKategorijeTrosak.getCount(); i++){
            if(odabirKategorijeTrosak.getItemAtPosition(i).toString().equalsIgnoreCase(MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcije(transakcija.getKategorijaTransakcije()).getNaziv())){
                return i;
            }
        }
        return 0;
    }
    private int OdaberiSpinnerRacuna(){
        for(int i =0; i<odabirRacunaTrosak.getCount(); i++){
            if(odabirRacunaTrosak.getItemAtPosition(i).toString().equalsIgnoreCase(MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(transakcija.getRacunTerecenja()).getNaziv())){
                return i;
            }
        }
        return 0;
    }

    private void PostaviSpinnerKategorija() {
        final List<KategorijaTransakcije> kt = MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiSveKategorijeTransakcije();
        final List<String> kategorijeZaSpinner = new ArrayList<>();
        for (KategorijaTransakcije kategorijaTransakcije : kt) {
            if (kategorijaTransakcije.getTipTransakcije() == 2) {
                kategorijeZaSpinner.add(kategorijaTransakcije.getNaziv());
            }
        }
        if (kategorijeZaSpinner.size() != 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_single_item, kategorijeZaSpinner);
            odabirKategorijeTrosak.setAdapter(adapter);
        } else
            Toast.makeText(getContext(), "Morate najprije kreirati kategoriju sa tipom transakcije TROŠAK", Toast.LENGTH_LONG).show();
    }

    private void PostavispinnerRacuna() {
        final List<Racun> sviRacuni = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiSveRacune();
        final List<String> racuniZaSpinner = new ArrayList<>();
        for (Racun racun : sviRacuni) {
            racuniZaSpinner.add(racun.getNaziv());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_single_item, racuniZaSpinner);
        odabirRacunaTrosak.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSlikajTrosak:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                transakcijaFragment.startActivityForResult(intent, 5);
                imageViewTrosak.setImageURI(transakcijaFragment.slika);
                break;
            case R.id.buttonUreduTrosak:
                if (!iznosTrosak.getText().toString().isEmpty() && !datumTrosak.getText().toString().isEmpty() && !opisTrosak.getText().toString().isEmpty()) {
                    float iznos = Float.parseFloat(iznosTrosak.getText().toString());
                    String datum = datumTrosak.getText().toString();
                    String opis = opisTrosak.getText().toString();
                    int odabranRacun = this.odabraniRacun.getId();
                    if (odabraniRacun.getPocetno_stanje() >= iznos) {
                        Transakcija transakcija = new Transakcija();
                        transakcija.setIznos(iznos);
                        transakcija.setDatum(datum);
                        transakcija.setOpis(opis);
                        transakcija.setRacunTerecenja(odabranRacun);
                        transakcija.setTipTransakcije(2);
                        transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                        transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                        MyDatabase.getInstance(getContext()).getTransakcijaDAO().UnosTransakcije(transakcija);
                        Racun racunZaAzuriranje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabraniRacun.getId());
                        float pocetnoStanje = racunZaAzuriranje.getPocetno_stanje();
                        racunZaAzuriranje.setPocetno_stanje(pocetnoStanje - iznos);
                        MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranje);

                        Log.e("transakcija", transakcija.toString());
                        Toast.makeText(v.getContext(), "Uspješno unesena transakcija troška", Toast.LENGTH_SHORT).show();
                        TransactionTrosakDialog.this.dismiss();
                    } else
                        Toast.makeText(v.getContext(), "Nemate dovoljno novaca na računu", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(v.getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_SHORT).show();
                TransactionTrosakDialog.this.dismiss();
                break;

            case R.id.btnAzurirajTrosak:
                if(transakcija!=null){
                    if (!iznosTrosak.getText().toString().isEmpty() && !datumTrosak.getText().toString().isEmpty() && !opisTrosak.getText().toString().isEmpty()) {
                        float iznos = Float.parseFloat(iznosTrosak.getText().toString());
                        String datum = datumTrosak.getText().toString();
                        String opis = opisTrosak.getText().toString();
                        int odabranRacun = this.odabraniRacun.getId();
                        if(transakcija.getIznos()<iznos){
                            if (odabraniRacun.getPocetno_stanje() >= iznos-transakcija.getIznos()) {
                                transakcija.setIznos(iznos);
                                transakcija.setDatum(datum);
                                transakcija.setOpis(opis);
                                transakcija.setRacunTerecenja(odabranRacun);
                                transakcija.setTipTransakcije(2);
                                transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                                transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                                MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(transakcija);

                                Racun racunZaAzuriranje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabraniRacun.getId());
                                float pocetnoStanje = racunZaAzuriranje.getPocetno_stanje();
                                racunZaAzuriranje.setPocetno_stanje(pocetnoStanje - (iznos-transakcija.getIznos()));
                                MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranje);

                                Log.e("transakcija", transakcija.toString());
                                Toast.makeText(v.getContext(), "Uspješno ažurirana transakcija troška", Toast.LENGTH_SHORT).show();
                                TransactionTrosakDialog.this.dismiss();
                            } else
                                Toast.makeText(v.getContext(), "Nemate dovoljno novaca na računu", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            transakcija.setIznos(iznos);
                            transakcija.setDatum(datum);
                            transakcija.setOpis(opis);
                            transakcija.setRacunTerecenja(odabranRacun);
                            transakcija.setTipTransakcije(2);
                            transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                            transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                            MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(transakcija);

                            Racun racunZaAzuriranje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabraniRacun.getId());
                            float pocetnoStanje = racunZaAzuriranje.getPocetno_stanje();
                            racunZaAzuriranje.setPocetno_stanje(pocetnoStanje + (transakcija.getIznos()-iznos));
                            MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranje);

                            Log.e("transakcija", transakcija.toString());
                            Toast.makeText(v.getContext(), "Uspješno ažurirana transakcija troška", Toast.LENGTH_SHORT).show();
                            TransactionTrosakDialog.this.dismiss();
                        }

                    } else
                        Toast.makeText(v.getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_SHORT).show();
                }
                TransactionTrosakDialog.this.dismiss();
                break;
        }

    }

}