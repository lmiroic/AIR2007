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

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class TransactionPrihodDialog extends Dialog implements android.view.View.OnClickListener {

    private OnDialogTransactionResult onDialogTransactionResult;
    private EditText datumPrihod;
    private EditText iznosPrihod;
    private EditText opisPrihod;
    private Spinner odabirKategorijePrihod;
    private Spinner odabirRacunaPrihod;
    private Button btnSlikajPrihod;
    private Button btnAzurirajPrihod;
    public ImageView imageViewPrihod;
    private Button btnUreduPrihod;
    private Transakcija transakcija;
    private CustomAdapterTransakcije customAdapterTransakcije;
    private TransakcijaFragment transakcijaFragment;
    private KategorijaTransakcije odabranaKategorijaTransakcije;
    private Racun odabraniRacun;
    private float iznosTransakcije;

    SimpleDateFormat formaterDate = new SimpleDateFormat("yyyy-MM-dd");

    public TransactionPrihodDialog(@NonNull Context context) {
        super(context);
    }

    public TransactionPrihodDialog(@NonNull Context context, Transakcija transakcija) {
        super(context);
        this.transakcija = transakcija;
    }

    public TransactionPrihodDialog(@NonNull Context context, TransakcijaFragment transakcijaFragment) {
        super(context);
        this.transakcijaFragment = transakcijaFragment;
        this.transakcijaFragment.transactionPrihodDialog = this;

    }

    public void SetOnDialogTransationResult(OnDialogTransactionResult onDialogTransactionResult) {
        this.onDialogTransactionResult = onDialogTransactionResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_prihod);
        datumPrihod = findViewById(R.id.txtDatum);
        datumPrihod.setInputType(InputType.TYPE_CLASS_DATETIME);

        iznosPrihod = findViewById(R.id.txtOdaberiIznos);
        iznosPrihod.setInputType(InputType.TYPE_CLASS_NUMBER);
        opisPrihod = findViewById(R.id.txtOpis);
        odabirKategorijePrihod = findViewById(R.id.cmbOdabirKategorije);
        odabirRacunaPrihod = findViewById(R.id.cmbOdaberiteRacun);
        btnSlikajPrihod = findViewById(R.id.btnSlikaj);
        imageViewPrihod = findViewById(R.id.imageViewPrihod);
        btnUreduPrihod = findViewById(R.id.btnUreduPrihod);
        btnAzurirajPrihod = findViewById(R.id.btnAzurirajPrihod);

        btnUreduPrihod.setOnClickListener(this);
        btnSlikajPrihod.setOnClickListener(this);
        PostaviSpinnerKategorija();
        PostavispinnerRacuna();

        btnAzurirajPrihod.setEnabled(false);
        btnAzurirajPrihod.setVisibility(View.INVISIBLE);

        if (transakcija != null) {
            btnSlikajPrihod.setEnabled(false);
            btnSlikajPrihod.setVisibility(View.INVISIBLE);
            btnUreduPrihod.setEnabled(false);
            btnUreduPrihod.setVisibility(View.INVISIBLE);

            btnAzurirajPrihod.setEnabled(true);
            btnAzurirajPrihod.setVisibility(View.VISIBLE);

            datumPrihod.setText(transakcija.getDatum());
            iznosPrihod.setText(String.valueOf(transakcija.getIznos()));
            opisPrihod.setText(transakcija.getOpis());
            odabirKategorijePrihod.setSelection(OdaberiSpinnerKategorije());
            odabirRacunaPrihod.setSelection(OdaberiSpinnerRacuna());
            iznosTransakcije = transakcija.getIznos();

            btnAzurirajPrihod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!iznosPrihod.getText().toString().isEmpty() && !datumPrihod.getText().toString().isEmpty() && !opisPrihod.getText().toString().isEmpty()) {
                        float iznos = Float.parseFloat(iznosPrihod.getText().toString());
                        String datum = datumPrihod.getText().toString();
                        String opis = opisPrihod.getText().toString();
                        String formatiraniDatum = "";
                        try {
                            formatiraniDatum = formaterDate.format(formaterDate.parse(datum));
                            if (transakcija.getIznos() <= iznos) {
                                transakcija.setIznos(iznos);
                                transakcija.setDatum(formatiraniDatum);
                                transakcija.setOpis(opis);
                                transakcija.setRacunPrijenosa(odabraniRacun.getId());
                                transakcija.setTipTransakcije(1);
                                transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                                transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                                MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(transakcija);

                                RequestBody requestId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transakcija.getId()));
                                RequestBody requestIznos = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(iznos));
                                RequestBody requestDatum = RequestBody.create(MediaType.parse("text/plain"), formatiraniDatum);
                                RequestBody requestRacunTerecenja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
                                RequestBody requestRacunPrijenosa = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabraniRacun.getId()));
                                RequestBody requestTipTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(TipTransakcije.PRIHOD));
                                RequestBody requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));
                                RequestBody requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), "");
                                RequestBody requestMemo = RequestBody.create(MediaType.parse("text/plain"), transakcija.getMemo() != null ? transakcija.getMemo() : "");
                                RequestBody requestOpis = RequestBody.create(MediaType.parse("text/plain"), opis);
                                RequestBody requestIkona = RequestBody.create(MediaType.parse("text/plain"), "");
                                RequestBody requestKorisnik = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Sesija.getInstance().getKorisnik().getId()));
                                RequestBody requestKategorijaTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranaKategorijaTransakcije.getId()));
                                RequestBody requestPlacenTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));

                                AzurirajTransakcijuUBazi(requestId, requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak);

                                float pocetnoStanje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabraniRacun.getId()).getPocetno_stanje();
                                odabraniRacun.setPocetno_stanje(pocetnoStanje + (iznos - iznosTransakcije));
                                MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(odabraniRacun);
                                AzurirajRacunUBazi(odabraniRacun);
                                Toast.makeText(v.getContext(), "Uspješno ažurirana transakcija prihoda", Toast.LENGTH_SHORT).show();
                                TransactionPrihodDialog.this.dismiss();
                            } else {
                                transakcija.setIznos(iznos);
                                transakcija.setDatum(formatiraniDatum);
                                transakcija.setOpis(opis);
                                transakcija.setRacunPrijenosa(odabraniRacun.getId());
                                transakcija.setTipTransakcije(1);
                                transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                                transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                                MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(transakcija);

                                RequestBody requestId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transakcija.getId()));
                                RequestBody requestIznos = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(iznos));
                                RequestBody requestDatum = RequestBody.create(MediaType.parse("text/plain"), formatiraniDatum);
                                RequestBody requestRacunTerecenja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
                                RequestBody requestRacunPrijenosa = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabraniRacun.getId()));
                                RequestBody requestTipTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(TipTransakcije.PRIHOD));
                                RequestBody requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));
                                RequestBody requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), "");
                                RequestBody requestMemo = RequestBody.create(MediaType.parse("text/plain"), transakcija.getMemo() != null ? transakcija.getMemo() : "");
                                RequestBody requestOpis = RequestBody.create(MediaType.parse("text/plain"), opis);
                                RequestBody requestIkona = RequestBody.create(MediaType.parse("text/plain"), "");
                                RequestBody requestKorisnik = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Sesija.getInstance().getKorisnik().getId()));
                                RequestBody requestKategorijaTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranaKategorijaTransakcije.getId()));
                                RequestBody requestPlacenTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));

                                AzurirajTransakcijuUBazi(requestId, requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak);

                                float pocetnoStanje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabraniRacun.getId()).getPocetno_stanje();
                                odabraniRacun.setPocetno_stanje(pocetnoStanje - (iznosTransakcije - iznos));
                                MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(odabraniRacun);
                                AzurirajRacunUBazi(odabraniRacun);
                                Toast.makeText(v.getContext(), "Uspješno ažurirana transakcija prihoda", Toast.LENGTH_SHORT).show();
                                TransactionPrihodDialog.this.dismiss();
                            }
                        } catch (ParseException e) {
                            Log.e("Exception", e.getMessage());
                            Toast.makeText(v.getContext(), "Morate unijeti datum u obliku yyyy-mm-dd", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        odabirKategorijePrihod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabranaKategorijaTransakcije = MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcijePremaNazivu(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        odabirRacunaPrihod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabraniRacun = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacunPoNazivu(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private int OdaberiSpinnerKategorije() {
        for (int i = 0; i < odabirKategorijePrihod.getCount(); i++) {
            if (odabirKategorijePrihod.getItemAtPosition(i).toString().equalsIgnoreCase(MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcije(transakcija.getKategorijaTransakcije()).getNaziv())) {
                return i;
            }
        }
        return 0;
    }

    private int OdaberiSpinnerRacuna() {
        for (int i = 0; i < odabirRacunaPrihod.getCount(); i++) {
            if (odabirRacunaPrihod.getItemAtPosition(i).toString().equalsIgnoreCase(MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(transakcija.getRacunPrijenosa()).getNaziv())) {
                return i;
            }
        }
        return 0;
    }

    private void PostaviSpinnerKategorija() {
        final List<KategorijaTransakcije> kt = MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiSveKategorijeTransakcije();
        final List<String> kategorijeZaSpinner = new ArrayList<>();
        for (KategorijaTransakcije kategorijaTransakcije : kt) {
            if (kategorijaTransakcije.getTipTransakcije() == 1) {
                kategorijeZaSpinner.add(kategorijaTransakcije.getNaziv());
            }
        }
        if (kategorijeZaSpinner.size() != 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_single_item, kategorijeZaSpinner);
            odabirKategorijePrihod.setAdapter(adapter);
        } else
            Toast.makeText(getContext(), "Morate najprije kreirati kategoriju sa tipom transakcije PRIHOD", Toast.LENGTH_LONG).show();
    }

    private void PostavispinnerRacuna() {
        final List<Racun> sviRacuni = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiSveRacune();
        final List<String> racuniZaSpinner = new ArrayList<>();
        for (Racun racun : sviRacuni) {
            racuniZaSpinner.add(racun.getNaziv());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_single_item, racuniZaSpinner);
        odabirRacunaPrihod.setAdapter(adapter);
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
            case R.id.btnSlikaj:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                transakcijaFragment.startActivityForResult(intent, 6);
                imageViewPrihod.setImageURI(transakcijaFragment.slika);
                break;
            case R.id.btnUreduPrihod:
                if (!iznosPrihod.getText().toString().isEmpty() && !datumPrihod.getText().toString().isEmpty() && !opisPrihod.getText().toString().isEmpty()) {
                    float iznos = Float.parseFloat(iznosPrihod.getText().toString());
                    String datum = datumPrihod.getText().toString();
                    String opis = opisPrihod.getText().toString();
                    String formatiraniDatum = "";
                    try {
                        formatiraniDatum = formaterDate.format(formaterDate.parse(datum));
                        Transakcija transakcija = new Transakcija();
                        transakcija.setIznos(iznos);
                        transakcija.setDatum(formatiraniDatum);
                        transakcija.setOpis(opis);
                        transakcija.setRacunPrijenosa(odabraniRacun.getId());
                        transakcija.setTipTransakcije(1);
                        transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                        transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                        transakcija.setIntervalPonavljanja("");
                        if (transakcijaFragment.slika != null) {

                            File datotekaSlike = new File(ImageFilePath.getPath(getContext(), transakcijaFragment.slika));
                            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), datotekaSlike);
                            RequestBody requestIznos = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(iznos));
                            RequestBody requestDatum = RequestBody.create(MediaType.parse("text/plain"), formatiraniDatum);
                            RequestBody requestRacunTerecenja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
                            RequestBody requestRacunPrijenosa = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabraniRacun.getId()));
                            RequestBody requestTipTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(TipTransakcije.PRIHOD));
                            MultipartBody.Part requestMemo = MultipartBody.Part.createFormData("memo", datotekaSlike.getName(), requestFile);
                            RequestBody requestOpis = RequestBody.create(MediaType.parse("text/plain"), opis);
                            RequestBody requestIkona = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody requestKorisnik = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Sesija.getInstance().getKorisnik().getId()));
                            RequestBody requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody requestKategorijaTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranaKategorijaTransakcije.getId()));
                            RequestBody requestPlacenTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));
                            RequestBody requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));

                            UnesiTransakcijuUBazu(transakcija, requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak);
                            float pocetnoStanje = odabraniRacun.getPocetno_stanje();
                            odabraniRacun.setPocetno_stanje(pocetnoStanje + iznos);
                            MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(odabraniRacun);
                            AzurirajRacunUBazi(odabraniRacun);
                            Toast.makeText(v.getContext(), "Uspješno unesena transakcija prihoda", Toast.LENGTH_SHORT).show();
                            TransactionPrihodDialog.this.dismiss();
                        } else
                            Toast.makeText(v.getContext(), "Morate ucitati sliku transakcije", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        Log.e("Exception", e.getMessage());
                        Toast.makeText(v.getContext(), "Morate unijeti datum u obliku yyyy-mm-dd", Toast.LENGTH_SHORT).show();
                    }

                } else
                    Toast.makeText(v.getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}