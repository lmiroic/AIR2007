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
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.TipTransakcije;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.modul.transakcije.OnDialogTransactionResult;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.fragmenti.TransakcijaFragment;
import air.foi.hr.moneymaker.manager.CustomAdapterTransakcije;
import air.foi.hr.moneymaker.session.Sesija;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    private Switch switchTroska;
    private Transakcija transakcija;
    private CustomAdapterTransakcije customAdapterTransakcije;
    private TransakcijaFragment transakcijaFragment;
    private KategorijaTransakcije odabranaKategorijaTransakcije;
    private Racun odabraniRacun;
    private Button btnAzurirajTrosak;
    private int iznosTransakcije;
    SimpleDateFormat formaterDate = new SimpleDateFormat("yyyy-MM-dd");
    Calendar datumPonavljajucegTroska = Calendar.getInstance();


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
        switchTroska = findViewById(R.id.switchTroska);
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
            iznosTransakcije = (int) transakcija.getIznos();
            btnUreduTrosak.setEnabled(false);
            btnUreduTrosak.setVisibility(View.INVISIBLE);
            btnSlikajTrosak.setEnabled(false);
            btnSlikajTrosak.setVisibility(View.INVISIBLE);

            if (transakcija.isPonavljajuciTrosak()) {
                switchTroska.setChecked(true);
            }
            btnAzurirajTrosak.setEnabled(true);
            btnAzurirajTrosak.setVisibility(View.VISIBLE);
            btnAzurirajTrosak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!iznosTrosak.getText().toString().isEmpty() && !datumTrosak.getText().toString().isEmpty() && !opisTrosak.getText().toString().isEmpty()) {
                        float iznos = Float.parseFloat(iznosTrosak.getText().toString());
                        String datum = datumTrosak.getText().toString();
                        String opis = opisTrosak.getText().toString();
                        int odabranRacun = odabraniRacun.getId();
                        if (transakcija.getIznos() < iznos) {
                            if (odabraniRacun.getPocetno_stanje() >= iznos - transakcija.getIznos()) {
                                transakcija.setIznos(iznos);
                                transakcija.setOpis(opis);
                                RequestBody requestPonavljajuciTrosak;
                                RequestBody requestIntervalPonavljanja;
                                if (switchTroska.isChecked()) {
                                    transakcija.setPonavljajuciTrosak(true);
                                    Date datumPonavljanjaTroska = null;
                                    try {
                                        datumPonavljanjaTroska = formaterDate.parse(datum);
                                    } catch (ParseException e) {
                                        Log.e("Exception", e.getMessage(), e);
                                    }
                                    datumPonavljajucegTroska.setTime(datumPonavljanjaTroska);
                                    datumPonavljajucegTroska.add(Calendar.MONTH, 1);
                                    transakcija.setIntervalPonavljanja(formaterDate.format(datumPonavljajucegTroska.getTime()));
                                    transakcija.setDatum(formaterDate.format(datumPonavljanjaTroska));
                                    requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(true));
                                    requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(formaterDate.format(datumPonavljajucegTroska.getTime())));
                                } else {
                                    transakcija.setPonavljajuciTrosak(false);
                                    transakcija.setDatum(datum);
                                    transakcija.setIntervalPonavljanja(null);
                                    requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));
                                    requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), "");
                                }
                                transakcija.setRacunTerecenja(odabranRacun);
                                transakcija.setTipTransakcije(2);
                                transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                                transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                                MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(transakcija);

                                RequestBody requestId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transakcija.getId()));
                                RequestBody requestIznos = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(iznos));
                                RequestBody requestDatum = RequestBody.create(MediaType.parse("text/plain"), datum);
                                RequestBody requestRacunTerecenja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranRacun));
                                RequestBody requestRacunPrijenosa = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
                                RequestBody requestTipTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(TipTransakcije.TROSAK));
                                RequestBody requestMemo = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transakcija.getMemo()));
                                RequestBody requestOpis = RequestBody.create(MediaType.parse("text/plain"), opis);
                                RequestBody requestIkona = RequestBody.create(MediaType.parse("text/plain"), "");
                                RequestBody requestKorisnik = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Sesija.getInstance().getKorisnik().getId()));
                                RequestBody requestKategorijaTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranaKategorijaTransakcije.getId()));
                                RequestBody requestPlacenTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));

                                AzurirajTransakcijuUBazi(requestId, requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak);

                                Racun racunZaAzuriranje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabraniRacun.getId());
                                float pocetnoStanje = racunZaAzuriranje.getPocetno_stanje();
                                racunZaAzuriranje.setPocetno_stanje(pocetnoStanje - (iznos - iznosTransakcije));
                                Log.e("racun", String.valueOf(pocetnoStanje - (iznos - iznosTransakcije)));
                                MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranje);

                                AzurirajRacunUBazi(racunZaAzuriranje);

                                Log.e("transakcija", transakcija.toString());
                                Toast.makeText(v.getContext(), "Uspješno ažurirana transakcija troška", Toast.LENGTH_SHORT).show();
                                TransactionTrosakDialog.this.dismiss();
                            } else
                                Toast.makeText(v.getContext(), "Nemate dovoljno novaca na računu", Toast.LENGTH_SHORT).show();
                        } else {
                            RequestBody requestPonavljajuciTrosak;
                            RequestBody requestIntervalPonavljanja;
                            transakcija.setIznos(iznos);
                            transakcija.setOpis(opis);
                            transakcija.setRacunTerecenja(odabranRacun);
                            transakcija.setTipTransakcije(2);
                            transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                            transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                            if (switchTroska.isChecked() == true) {
                                transakcija.setPonavljajuciTrosak(true);
                                Date datumPonavljanjaTroska = null;
                                try {
                                    datumPonavljanjaTroska = formaterDate.parse(datum);
                                } catch (ParseException e) {
                                    Log.e("Exception", e.getMessage(), e);
                                }
                                datumPonavljajucegTroska.setTime(datumPonavljanjaTroska);
                                datumPonavljajucegTroska.add(Calendar.MONTH, 1);
                                transakcija.setIntervalPonavljanja(formaterDate.format(datumPonavljajucegTroska.getTime()));
                                transakcija.setDatum(formaterDate.format(datumPonavljanjaTroska));
                                requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(true));
                                requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(formaterDate.format(datumPonavljajucegTroska.getTime())));
                            } else {
                                transakcija.setPonavljajuciTrosak(false);
                                transakcija.setDatum(datum);
                                transakcija.setIntervalPonavljanja(null);
                                requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));
                                requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), "");
                            }
                            MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(transakcija);

                            RequestBody requestId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transakcija.getId()));
                            RequestBody requestIznos = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(iznos));
                            RequestBody requestDatum = RequestBody.create(MediaType.parse("text/plain"), datum);
                            RequestBody requestRacunTerecenja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranRacun));
                            RequestBody requestRacunPrijenosa = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
                            RequestBody requestTipTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(TipTransakcije.TROSAK));
                            RequestBody requestMemo = RequestBody.create(MediaType.parse("text/plain"), transakcija.getMemo());
                            RequestBody requestOpis = RequestBody.create(MediaType.parse("text/plain"), opis);
                            RequestBody requestIkona = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody requestKorisnik = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Sesija.getInstance().getKorisnik().getId()));
                            RequestBody requestKategorijaTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranaKategorijaTransakcije.getId()));
                            RequestBody requestPlacenTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));

                            AzurirajTransakcijuUBazi(requestId, requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak);


                            Racun racunZaAzuriranje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabraniRacun.getId());
                            float pocetnoStanje = racunZaAzuriranje.getPocetno_stanje();
                            racunZaAzuriranje.setPocetno_stanje(pocetnoStanje + (iznosTransakcije - iznos));
                            MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranje);

                            AzurirajRacunUBazi(racunZaAzuriranje);

                            Log.e("transakcija", transakcija.toString());
                            Toast.makeText(v.getContext(), "Uspješno ažurirana transakcija troška", Toast.LENGTH_SHORT).show();
                            TransactionTrosakDialog.this.dismiss();
                        }

                    } else
                        Toast.makeText(v.getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private int OdaberiSpinnerKategorije() {
        for (int i = 0; i < odabirKategorijeTrosak.getCount(); i++) {
            if (odabirKategorijeTrosak.getItemAtPosition(i).toString().equalsIgnoreCase(MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcije(transakcija.getKategorijaTransakcije()).getNaziv())) {
                return i;
            }
        }
        return 0;
    }

    private int OdaberiSpinnerRacuna() {
        for (int i = 0; i < odabirRacunaTrosak.getCount(); i++) {
            if (odabirRacunaTrosak.getItemAtPosition(i).toString().equalsIgnoreCase(MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(transakcija.getRacunTerecenja()).getNaziv())) {
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

    private void AzurirajTransakcijuUBazi(RequestBody requestId, RequestBody requestIznos, RequestBody requestDatum, RequestBody requestRacunTerecenja, RequestBody requestRacunPrijenosa, RequestBody requestTipTransakcije, RequestBody requestMemo, RequestBody requestOpis, RequestBody requestPonavljajuciTrosak, RequestBody requestIkona, RequestBody requestKorisnik, RequestBody requestIntervalPonavljanja, RequestBody requestKategorijaTransakcije, RequestBody requestPlacenTrosak) {
        Retrofit retrofit = RetrofitInstance.getInstance();
        RestApiImplementor restApiImplementor = retrofit.create(RestApiImplementor.class);
        restApiImplementor.AzurirajTransakciju(requestId, requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("response", t.getMessage(), t);

            }
        });
    }

    private void AzurirajRacunUBazi(Racun racunZaAzuriranje) {
        Retrofit retrofit = RetrofitInstance.getInstance();
        RestApiImplementor restApiImplementor = retrofit.create(RestApiImplementor.class);
        Call<Void> pozivUnosa = restApiImplementor.AzurirajRacun(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(racunZaAzuriranje.getId())), RequestBody.create(MediaType.parse("text/plain"), racunZaAzuriranje.getNaziv()), RequestBody.create(MediaType.parse("text/plain"), String.valueOf(racunZaAzuriranje.getPocetno_stanje())), RequestBody.create(MediaType.parse("text/plain"), racunZaAzuriranje.getValuta()), RequestBody.create(MediaType.parse("text/plain"), racunZaAzuriranje.getIkona()), RequestBody.create(MediaType.parse("text/plain"), (String.valueOf(racunZaAzuriranje.getKorisnik_id()))));
        pozivUnosa.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("Racun", "azuriran racun u bazi");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void UnesiTransakcijuUBazu(Transakcija transakcija, RequestBody requestIznos, RequestBody requestDatum, RequestBody requestRacunTerecenja, RequestBody requestRacunPrijenosa, RequestBody requestTipTransakcije, MultipartBody.Part requestMemo, RequestBody requestOpis, RequestBody requestPonavljajuciTrosak, RequestBody requestIkona, RequestBody requestKorisnik, RequestBody requestIntervalPonavljanja, RequestBody requestKategorijaTransakcije, RequestBody requestPlacenTrosak) {
        Retrofit retrofit = RetrofitInstance.getInstance();
        RestApiImplementor restApiImplementor = retrofit.create(RestApiImplementor.class);
        restApiImplementor.UnesiTransakciju(requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak).enqueue(new Callback<Transakcija>() {
            @Override
            public void onResponse(Call<Transakcija> call, Response<Transakcija> response) {
                if (response.isSuccessful()) {
                    transakcija.setMemo(response.body().getMemo());
                    MyDatabase.getInstance(getContext()).getTransakcijaDAO().UnosTransakcije(transakcija);
                    //MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(transakcija);
                }
            }

            @Override
            public void onFailure(Call<Transakcija> call, Throwable t) {

            }
        });
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
                    RequestBody requestPonavljajuciTrosak;
                    RequestBody requestIntervalPonavljanja;
                    String opis = opisTrosak.getText().toString();
                    int odabranRacun = this.odabraniRacun.getId();
                    if (odabraniRacun.getPocetno_stanje() >= iznos) {
                        Transakcija transakcija = new Transakcija();
                        transakcija.setIznos(iznos);
                        transakcija.setOpis(opis);
                        transakcija.setRacunTerecenja(odabranRacun);
                        transakcija.setTipTransakcije(2);
                        transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                        if (switchTroska.isChecked() == true) {
                            transakcija.setPonavljajuciTrosak(true);
                            Date datumPonavljanjaTroska = null;
                            try {
                                datumPonavljanjaTroska = formaterDate.parse(datum);
                            } catch (ParseException e) {
                                Log.e("Exception", e.getMessage(), e);
                            }
                            datumPonavljajucegTroska.setTime(datumPonavljanjaTroska);
                            datumPonavljajucegTroska.add(Calendar.MONTH, 1);
                            transakcija.setIntervalPonavljanja(formaterDate.format(datumPonavljajucegTroska.getTime()));
                            transakcija.setDatum(formaterDate.format(datumPonavljanjaTroska));
                            requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(true));
                            requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(formaterDate.format(datumPonavljajucegTroska.getTime())));
                        } else {
                            transakcija.setPonavljajuciTrosak(false);
                            requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));
                            requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), "");
                            try {
                                transakcija.setDatum(formaterDate.format(formaterDate.parse(datum)));
                            } catch (ParseException e) {
                                Log.e("Exception", e.getMessage(), e);
                            }
                        }
                        transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                        if (transakcijaFragment.slika != null) {

                            File datotekaSlike = new File(ImageFilePath.getPath(getContext(), transakcijaFragment.slika));
                            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), datotekaSlike);
                            RequestBody requestIznos = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(iznos));
                            RequestBody requestDatum = RequestBody.create(MediaType.parse("text/plain"), datum);
                            RequestBody requestRacunTerecenja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranRacun));
                            RequestBody requestRacunPrijenosa = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
                            RequestBody requestTipTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(TipTransakcije.TROSAK));
                            MultipartBody.Part requestMemo = MultipartBody.Part.createFormData("memo", datotekaSlike.getName(), requestFile);
                            RequestBody requestOpis = RequestBody.create(MediaType.parse("text/plain"), opis);
                            RequestBody requestIkona = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody requestKorisnik = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Sesija.getInstance().getKorisnik().getId()));
                            RequestBody requestKategorijaTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranaKategorijaTransakcije.getId()));
                            RequestBody requestPlacenTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));

                            UnesiTransakcijuUBazu(transakcija, requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak);
                        }
                        Racun racunZaAzuriranje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabraniRacun.getId());
                        float pocetnoStanje = racunZaAzuriranje.getPocetno_stanje();
                        racunZaAzuriranje.setPocetno_stanje(pocetnoStanje - iznos);
                        MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranje);

                        AzurirajRacunUBazi(racunZaAzuriranje);

                        Log.e("transakcija", transakcija.toString());
                        Toast.makeText(v.getContext(), "Uspješno unesena transakcija troška", Toast.LENGTH_SHORT).show();
                        TransactionTrosakDialog.this.dismiss();
                    } else
                        Toast.makeText(v.getContext(), "Nemate dovoljno novaca na računu", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_SHORT).show();
                }
                TransactionTrosakDialog.this.dismiss();
                break;
        }

    }

}