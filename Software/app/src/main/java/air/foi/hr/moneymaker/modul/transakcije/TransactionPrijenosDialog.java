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
import java.util.Calendar;
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

public class TransactionPrijenosDialog extends Dialog implements android.view.View.OnClickListener {

    private OnDialogTransactionResult onDialogTransactionResult;
    private EditText datumPrijenos;
    private EditText iznosPrijenos;
    private EditText opisPrijenos;
    private Spinner odabirIzRacuna;
    private Spinner odabirURacun;
    private Button btnSlikajPrijenos;
    private Button btnAzurirajPrijenos;
    public ImageView imageViewPrijenos;
    private Button btnUreduPrijenos;
    private Transakcija transakcija;
    private CustomAdapterTransakcije customAdapterTransakcije;
    private TransakcijaFragment transakcijaFragment;
    private Racun odabranURacun;
    private Racun odabranIzRacun;
    private Spinner kategorijeZaPrijenos;
    private KategorijaTransakcije odabranaKategorijaTransakcije;
    private float iznosTransakcije;

    SimpleDateFormat formaterDate = new SimpleDateFormat("yyyy-MM-dd");

    public TransactionPrijenosDialog(@NonNull Context context) {
        super(context);
    }

    public TransactionPrijenosDialog(@NonNull Context context, Transakcija transakcija) {
        super(context);
        this.transakcija = transakcija;
    }

    public TransactionPrijenosDialog(@NonNull Context context, TransakcijaFragment transakcijaFragment) {
        super(context);
        this.transakcijaFragment = transakcijaFragment;
        this.transakcijaFragment.transactionPrijenosDialog = this;
    }

    public void SetOnDialogTransactionResult(OnDialogTransactionResult onDialogTransactionResult) {
        this.onDialogTransactionResult = onDialogTransactionResult;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_prijenos);
        datumPrijenos = findViewById(R.id.txtDatumPrijenos);
        datumPrijenos.setInputType(InputType.TYPE_CLASS_DATETIME);
        iznosPrijenos = findViewById(R.id.txtOdaberiIznosPrijenos);
        iznosPrijenos.setInputType(InputType.TYPE_CLASS_NUMBER);
        opisPrijenos = findViewById(R.id.txtOpisPrijenos);
        odabirIzRacuna = findViewById(R.id.cmbIzRacunaPrijenos);
        odabirURacun = findViewById(R.id.cmbOdaberiteRacun);
        btnSlikajPrijenos = findViewById(R.id.btnSlikajPrijenos);
        imageViewPrijenos = findViewById(R.id.imageViewPrijenos);
        btnUreduPrijenos = findViewById(R.id.buttonUreduPrijenos);
        kategorijeZaPrijenos = findViewById(R.id.SpinnerKategorijaZaPrijenos);

        btnAzurirajPrijenos = findViewById(R.id.btnAzurirajPrijenos);
        btnAzurirajPrijenos.setEnabled(false);
        btnAzurirajPrijenos.setVisibility(View.INVISIBLE);

        btnUreduPrijenos.setOnClickListener(this);
        btnSlikajPrijenos.setOnClickListener(this);
        PostavispinnerIzRacuna();
        PostavispinnerURacuna();
        PostaviSpinnerKategorija();

        if (transakcija != null) {
            btnSlikajPrijenos.setEnabled(false);
            btnSlikajPrijenos.setVisibility(View.INVISIBLE);
            datumPrijenos.setText(transakcija.getDatum());
            iznosPrijenos.setText(String.valueOf(transakcija.getIznos()));
            opisPrijenos.setText(transakcija.getOpis());
            odabirIzRacuna.setSelection(OdaberiSpinnerRacunaTerecenja());
            odabirURacun.setSelection(OdaberiSpinnerRacunaPrijenosa());
            kategorijeZaPrijenos.setSelection(OdaberiSpinnerKategorije());
            iznosTransakcije = transakcija.getIznos();

            btnAzurirajPrijenos.setEnabled(true);
            btnAzurirajPrijenos.setVisibility(View.VISIBLE);

            btnAzurirajPrijenos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!iznosPrijenos.getText().toString().isEmpty() && !datumPrijenos.getText().toString().isEmpty() && !opisPrijenos.getText().toString().isEmpty()) {
                        float iznos = Float.parseFloat(iznosPrijenos.getText().toString());
                        String datum = datumPrijenos.getText().toString();
                        String opis = opisPrijenos.getText().toString();
                        String formatiraniDatum="";
                        try {
                            formatiraniDatum=(formaterDate.format(formaterDate.parse(datum)));
                        } catch (ParseException e) {
                            Log.e("Exception", e.getMessage(), e);
                            Toast.makeText(v.getContext(),"Morate unijeti datum u obliku yyyy-MM-dd",Toast.LENGTH_SHORT).show();
                        }
                        if (transakcija.getIznos() < iznos) {
                            if (odabranIzRacun.getPocetno_stanje() >= iznos - transakcija.getIznos()) {
                                transakcija.setIznos(iznos);
                                transakcija.setDatum(formatiraniDatum);
                                transakcija.setOpis(opis);
                                transakcija.setRacunPrijenosa(odabranURacun.getId());
                                transakcija.setRacunTerecenja(odabranIzRacun.getId());
                                transakcija.setTipTransakcije(3);
                                transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                                transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                                MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(transakcija);

                                RequestBody requestId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transakcija.getId()));
                                RequestBody requestIznos = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(iznos));
                                RequestBody requestDatum = RequestBody.create(MediaType.parse("text/plain"), formatiraniDatum);
                                RequestBody requestRacunTerecenja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranIzRacun.getId()));
                                RequestBody requestRacunPrijenosa = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranURacun.getId()));
                                RequestBody requestTipTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(TipTransakcije.PRIJENOS));
                                RequestBody requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));
                                RequestBody requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), "");
                                RequestBody requestMemo = RequestBody.create(MediaType.parse("text/plain"), transakcija.getMemo()!=null?transakcija.getMemo():"");
                                RequestBody requestOpis = RequestBody.create(MediaType.parse("text/plain"), opis);
                                RequestBody requestIkona = RequestBody.create(MediaType.parse("text/plain"), "");
                                RequestBody requestKorisnik = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Sesija.getInstance().getKorisnik().getId()));
                                RequestBody requestKategorijaTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranaKategorijaTransakcije.getId()));
                                RequestBody requestPlacenTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));

                                AzurirajTransakcijuUBazi(requestId, requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak);


                                Racun racunZaAzuriranje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabranIzRacun.getId());
                                float pocetnoStanje = racunZaAzuriranje.getPocetno_stanje();
                                racunZaAzuriranje.setPocetno_stanje(pocetnoStanje - (iznos - iznosTransakcije));
                                MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranje);
                                AzurirajRacunUBazi(racunZaAzuriranje);

                                float pocetnoStanjeR2 = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabranURacun.getId()).getPocetno_stanje();
                                odabranURacun.setPocetno_stanje(pocetnoStanjeR2 + (iznos - iznosTransakcije));
                                MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(odabranURacun);
                                AzurirajRacunUBazi(odabranURacun);


                                Log.e("transakcija", transakcija.toString());
                                Toast.makeText(v.getContext(), "Uspješno ažurirana transakcija prijenosa", Toast.LENGTH_SHORT).show();
                                TransactionPrijenosDialog.this.dismiss();
                            } else
                                Toast.makeText(v.getContext(), "Nemate dovoljno novaca na računu", Toast.LENGTH_SHORT).show();
                        } else {
                            transakcija.setIznos(iznos);
                            transakcija.setDatum(formatiraniDatum);
                            transakcija.setOpis(opis);
                            transakcija.setRacunPrijenosa(odabranURacun.getId());
                            transakcija.setRacunTerecenja(odabranIzRacun.getId());
                            transakcija.setTipTransakcije(3);
                            transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                            transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                            MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(transakcija);

                            RequestBody requestId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transakcija.getId()));
                            RequestBody requestIznos = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(iznos));
                            RequestBody requestDatum = RequestBody.create(MediaType.parse("text/plain"), formatiraniDatum);
                            RequestBody requestRacunTerecenja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranIzRacun.getId()));
                            RequestBody requestRacunPrijenosa = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranURacun.getId()));
                            RequestBody requestTipTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(TipTransakcije.PRIJENOS));
                            RequestBody requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));
                            RequestBody requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody requestMemo = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transakcija.getMemo()));
                            RequestBody requestOpis = RequestBody.create(MediaType.parse("text/plain"), opis);
                            RequestBody requestIkona = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody requestKorisnik = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Sesija.getInstance().getKorisnik().getId()));
                            RequestBody requestKategorijaTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranaKategorijaTransakcije.getId()));
                            RequestBody requestPlacenTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));

                            AzurirajTransakcijuUBazi(requestId, requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak);

                            Racun racunZaAzuriranje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabranIzRacun.getId());
                            float pocetnoStanje = racunZaAzuriranje.getPocetno_stanje();
                            racunZaAzuriranje.setPocetno_stanje(pocetnoStanje + (iznosTransakcije - iznos));
                            MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranje);
                            AzurirajRacunUBazi(racunZaAzuriranje);

                            Racun racunZaAzuriranjeR2 = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabranURacun.getId());
                            float pocetnoStanje2 = racunZaAzuriranjeR2.getPocetno_stanje();
                            racunZaAzuriranjeR2.setPocetno_stanje(pocetnoStanje2 - (iznosTransakcije - iznos));
                            MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranjeR2);
                            AzurirajRacunUBazi(racunZaAzuriranjeR2);

                            Log.e("transakcija", transakcija.toString());
                            Toast.makeText(v.getContext(), "Uspješno ažurirana transakcija prijenosa", Toast.LENGTH_SHORT).show();
                            TransactionPrijenosDialog.this.dismiss();
                        }

                    } else
                        Toast.makeText(v.getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_SHORT).show();
                }
            });

            btnUreduPrijenos.setEnabled(false);
            btnUreduPrijenos.setVisibility(View.INVISIBLE);
            // IMAGE VIEW POTREBNO IZRADITI!!

        }

        odabirIzRacuna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabranIzRacun = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacunPoNazivu(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        odabirURacun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabranURacun = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacunPoNazivu(parent.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        kategorijeZaPrijenos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabranaKategorijaTransakcije = MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcijePremaNazivu(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

    private int OdaberiSpinnerKategorije() {
        for (int i = 0; i < kategorijeZaPrijenos.getCount(); i++) {
            if (kategorijeZaPrijenos.getItemAtPosition(i).toString().equalsIgnoreCase(MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcije(transakcija.getKategorijaTransakcije()).getNaziv())) {
                return i;
            }
        }
        return 0;
    }

    private int OdaberiSpinnerRacunaTerecenja() {
        for (int i = 0; i < odabirIzRacuna.getCount(); i++) {
            try{
                if (MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(transakcija.getRacunTerecenja()).getNaziv()!=null&&odabirIzRacuna.getItemAtPosition(i).toString().equalsIgnoreCase(MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(transakcija.getRacunTerecenja()).getNaziv())) {
                    return i;
                }
            }
            catch (Exception e){
                Log.e("Probal je biti null",e.getMessage(),e);
            }


        }
        return 0;
    }

    private int OdaberiSpinnerRacunaPrijenosa() {
        for (int i = 0; i < odabirURacun.getCount(); i++) {
            if (odabirURacun.getItemAtPosition(i).toString().equalsIgnoreCase(MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(transakcija.getRacunPrijenosa()).getNaziv())) {
                return i;
            }
        }
        return 0;
    }

    private void PostavispinnerIzRacuna() {
        final List<Racun> sviRacuni = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiSveRacune();
        final List<String> racuniZaSpinner = new ArrayList<>();
        for (Racun racun : sviRacuni) {
            racuniZaSpinner.add(racun.getNaziv());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_single_item, racuniZaSpinner);
        odabirIzRacuna.setAdapter(adapter);
    }

    private void PostaviSpinnerKategorija() {
        final List<KategorijaTransakcije> kt = MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().DohvatiSveKategorijeTransakcije();
        final List<String> kategorijeZaSpinner = new ArrayList<>();
        for (KategorijaTransakcije kategorijaTransakcije : kt) {
            if (kategorijaTransakcije.getTipTransakcije() == 3) {
                kategorijeZaSpinner.add(kategorijaTransakcije.getNaziv());
            }
        }
        if (kategorijeZaSpinner.size() != 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_single_item, kategorijeZaSpinner);
            kategorijeZaPrijenos.setAdapter(adapter);
        } else
            Toast.makeText(getContext(), "Morate najprije kreirati kategoriju sa tipom transakcije PRIJENOS", Toast.LENGTH_LONG).show();


    }

    private void PostavispinnerURacuna() {
        final List<Racun> sviRacuni = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiSveRacune();
        final List<String> racuniZaSpinner = new ArrayList<>();
        for (Racun racun : sviRacuni) {
            racuniZaSpinner.add(racun.getNaziv());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_single_item, racuniZaSpinner);
        odabirURacun.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSlikajPrijenos:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                transakcijaFragment.startActivityForResult(intent, 7);
                imageViewPrijenos.setImageURI(transakcijaFragment.slika);
                break;
            case R.id.buttonUreduPrijenos:
                if (!iznosPrijenos.getText().toString().isEmpty() && !datumPrijenos.getText().toString().isEmpty() && !opisPrijenos.getText().toString().isEmpty()) {
                    float iznos = Float.parseFloat(iznosPrijenos.getText().toString());
                    if (odabranIzRacun.getPocetno_stanje() >= iznos) {
                        String datum = datumPrijenos.getText().toString();
                        String opis = opisPrijenos.getText().toString();
                        Transakcija transakcija = new Transakcija();
                        transakcija.setIznos(iznos);
                        try {
                            transakcija.setDatum(formaterDate.format(formaterDate.parse(datum)));
                        } catch (ParseException e) {
                            Log.e("Exception", e.getMessage(), e);
                        }
                        transakcija.setOpis(opis);
                        transakcija.setRacunTerecenja(odabranIzRacun.getId());
                        transakcija.setRacunPrijenosa(odabranURacun.getId());
                        transakcija.setKategorijaTransakcije(odabranaKategorijaTransakcije.getId());
                        transakcija.setKorisnik(Sesija.getInstance().getKorisnik().getId());
                        transakcija.setTipTransakcije(3);
                        transakcija.setPonavljajuciTrosak(false);
                        transakcija.setIntervalPonavljanja("");

                        Retrofit retrofit = RetrofitInstance.getInstance();
                        RestApiImplementor restApiImplementor = retrofit.create(RestApiImplementor.class);
                        if(transakcijaFragment.slika!=null) {


                            File datotekaSlike = new File(ImageFilePath.getPath(getContext(), transakcijaFragment.slika));
                            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), datotekaSlike);
                            RequestBody requestIznos = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(iznos));
                            RequestBody requestDatum = RequestBody.create(MediaType.parse("text/plain"), datum);
                            RequestBody requestRacunTerecenja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranIzRacun.getId()));
                            RequestBody requestRacunPrijenosa = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranURacun.getId()));
                            RequestBody requestTipTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(TipTransakcije.PRIJENOS));
                            MultipartBody.Part requestMemo = MultipartBody.Part.createFormData("memo", datotekaSlike.getName(), requestFile);
                            RequestBody requestOpis = RequestBody.create(MediaType.parse("text/plain"), opis);
                            RequestBody requestIkona = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody requestKorisnik = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Sesija.getInstance().getKorisnik().getId()));
                            RequestBody requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody requestKategorijaTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(odabranaKategorijaTransakcije.getId()));
                            RequestBody requestPlacenTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));
                            RequestBody requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));
                            restApiImplementor.UnesiTransakciju(requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak).enqueue(new Callback<Transakcija>() {
                                @Override
                                public void onResponse(Call<Transakcija> call, Response<Transakcija> response) {
                                    if(response.isSuccessful()){
                                        transakcija.setMemo(response.body().getMemo());
                                        MyDatabase.getInstance(getContext()).getTransakcijaDAO().UnosTransakcije(transakcija);
                                        //MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(transakcija);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Transakcija> call, Throwable t) {
                                    Log.e("response", t.getMessage(), t);
                                }
                            });

                            Racun racunZaAzuriranje = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabranIzRacun.getId());
                            float pocetnoStanje = racunZaAzuriranje.getPocetno_stanje();
                            racunZaAzuriranje.setPocetno_stanje(pocetnoStanje - iznos);
                            MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranje);

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

                            Racun racunZaAzuriranje2 = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(odabranURacun.getId());
                            float pocetnoStanje2 = racunZaAzuriranje2.getPocetno_stanje();
                            racunZaAzuriranje2.setPocetno_stanje(pocetnoStanje2 + iznos);
                            MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunZaAzuriranje2);

                            Call<Void> pozivUnosaRacuna = restApiImplementor.AzurirajRacun(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(racunZaAzuriranje2.getId())), RequestBody.create(MediaType.parse("text/plain"), racunZaAzuriranje2.getNaziv()), RequestBody.create(MediaType.parse("text/plain"), String.valueOf(racunZaAzuriranje2.getPocetno_stanje())), RequestBody.create(MediaType.parse("text/plain"), racunZaAzuriranje2.getValuta()), RequestBody.create(MediaType.parse("text/plain"), racunZaAzuriranje2.getIkona()), RequestBody.create(MediaType.parse("text/plain"), (String.valueOf(racunZaAzuriranje2.getKorisnik_id()))));
                            pozivUnosaRacuna.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Log.e("Racun", "azuriran racun u bazi");
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });

                            Log.e("transakcija", transakcija.toString());
                            Toast.makeText(v.getContext(), "Uspješno unesena transakcija prijenosa", Toast.LENGTH_SHORT).show();
                            TransactionPrijenosDialog.this.dismiss();
                        }
                        else
                            Toast.makeText(v.getContext(),"Morate ucitati sliku za unos transakacije",Toast.LENGTH_SHORT).show();
                    }

                } else
                    Toast.makeText(v.getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_SHORT).show();

                break;
        }

    }
}