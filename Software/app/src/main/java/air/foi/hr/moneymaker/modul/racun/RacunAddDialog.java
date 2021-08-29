package air.foi.hr.moneymaker.modul.racun;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Valuta;
import air.foi.hr.core.modul.racuni.OnDialogRacunResult;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.manager.CustomAdapterAddRacun;
import air.foi.hr.moneymaker.manager.RacunAddModel;
import air.foi.hr.moneymaker.modul.kategorije.CategoryAddDialog;
import air.foi.hr.moneymaker.session.Sesija;
import eu.airmoneymaker.rest.RestApiImplementor;
import eu.airmoneymaker.rest.RetrofitInstance;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Part;

public class RacunAddDialog extends Dialog implements View.OnClickListener {
    private EditText imeRacuna, stanjeRacuna;
    private Spinner valuta;
    private ImageView ikona;
    private RecyclerView recyclerView;
    private Button unesi,obrisi;
    private OnDialogRacunResult onDialogRacunResult;
    private String odabranaValuta="";
    private Racun racun;
    public String ikoneRacuna[]={"ic_money","ic_credit_card", "ic_maestro", "ic_visa","ic_mastercard","ic_paypal","ic_american", "ic_kasica"};
    private ArrayList<RacunAddModel> arrayList;
    private CustomAdapterAddRacun adapterAddRacun;
    public RacunAddDialog(@NonNull Context context) {
        super(context);
    }
    public RacunAddDialog(@NonNull Context context, Racun racun) {
        super(context);
        this.racun=racun;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDodajRacun:
                new OkListenerAddRacun().onClick(v);
                break;
        }
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_racun_add);
        valuta=findViewById(R.id.spinerAddValuta);
        unesi=findViewById(R.id.btnDodajRacun);
        obrisi=findViewById(R.id.btnIzbrisiRacun);
        unesi.setOnClickListener(this);
        imeRacuna=findViewById(R.id.txtAddImeRacuna);
        stanjeRacuna=findViewById(R.id.txtAddStanje);
        PostaviRecycleView();
        PostaviSpinner();
        if(racun!=null){
            imeRacuna.setText(racun.getNaziv());
            valuta.setSelection(OdaberiSpinner());
            stanjeRacuna.setText(String.valueOf(racun.getPocetno_stanje()));
            oznaciIkonu(racun.getIkona());
            unesi.setText("Ažuriraj");
            unesi.setOnClickListener(this);
            obrisi.setVisibility(View.VISIBLE);
            obrisi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDialogRacunResult != null) {
                        Racun r = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(racun.getId());
                        MyDatabase.getInstance(getContext()).getRacunDAO().IzbrisiRacun(r);
                        Retrofit retrofit = RetrofitInstance.getInstance();
                        RestApiImplementor api=retrofit.create(RestApiImplementor.class);
                        Call<Void> pozivUnosa=api.ObrisiRacun(RequestBody.create(MediaType.parse("text/plain"),String.valueOf(racun.getId())));
                        pozivUnosa.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.e("Racun", "izbrisan racun");
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                        Toast.makeText(getContext(), "Izbrisali ste račun:  " + r.getNaziv(), Toast.LENGTH_SHORT).show();
                        onDialogRacunResult.finish();
                    }
                    RacunAddDialog.this.dismiss();
                }
            });
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public void setOnDialogRacunResult(OnDialogRacunResult onDialogRacunResult){
        this.onDialogRacunResult = onDialogRacunResult;

    }
    private void PostaviSpinner(){
        final List<Valuta> valute = MyDatabase.getInstance(getContext()).getValutaDAO().DohvatiSveValute();
        final List<String> valutaZaSpinner = new ArrayList<>();
        for(Valuta v:valute){
            valutaZaSpinner.add(v.getNaziv());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.spinner_valuta_racuna,valutaZaSpinner);
        valuta.setAdapter(adapter);
        valuta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                odabranaValuta=valutaZaSpinner.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private List<RacunAddModel> VratiListuIkona(){
        arrayList=new ArrayList<>();
        for(int i=0; i<ikoneRacuna.length; i++) {
            RacunAddModel racunAddModel = new RacunAddModel();
            racunAddModel.setIkonaRacuna(ikoneRacuna[i]);
            arrayList.add(racunAddModel);
        }
        return arrayList;
    }

    private class OkListenerAddRacun implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(racun==null) {
                if (onDialogRacunResult != null) {
                    if (!imeRacuna.getText().toString().equals("") && !stanjeRacuna.getText().toString().equals("")) {
                        Racun noviRacun = new Racun();
                        noviRacun.setKorisnik_id(Sesija.getInstance().getKorisnik().getId());
                        noviRacun.setIkona(adapterAddRacun.arrayList.get(adapterAddRacun.focusedItemRacun).getRawIkonaRacuna());
                        noviRacun.setNaziv(imeRacuna.getText().toString());
                        noviRacun.setPocetno_stanje(Float.parseFloat(stanjeRacuna.getText().toString()));
                        noviRacun.setValuta(odabranaValuta);
                        MyDatabase.getInstance(getContext()).getRacunDAO().UnosRacuna(noviRacun);
                        Retrofit r= RetrofitInstance.getInstance();
                        RestApiImplementor api=r.create(RestApiImplementor.class);
                        Call<Void> pozivUnosa = api.UnesiRacun(RequestBody.create(MediaType.parse("text/plain"),imeRacuna.getText().toString()),
                                RequestBody.create(MediaType.parse("text/plain"),stanjeRacuna.getText().toString()),
                                RequestBody.create(MediaType.parse("text/plain"),String.valueOf(odabranaValuta)),
                                RequestBody.create(MediaType.parse("text/plain"),String.valueOf(adapterAddRacun.arrayList.get(adapterAddRacun.focusedItemRacun).getRawIkonaRacuna())),
                                RequestBody.create(MediaType.parse("text/plain"),String.valueOf(Sesija.getInstance().getKorisnik().getId())
                        ));
                        pozivUnosa.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.e("Racun","unesen novi racun baza");
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                            }
                        });
                        Toast.makeText(getContext(), "Unijeli ste novi račun: " + noviRacun.getNaziv(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_LONG).show();
                    }
                    onDialogRacunResult.finish();
                }
                RacunAddDialog.this.dismiss();
            }
            else{
                if (onDialogRacunResult != null) {
                    if (!imeRacuna.getText().toString().equals("") && !stanjeRacuna.getText().toString().equals("")) {
                        Racun r=MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(racun.getId());
                        r.setKorisnik_id(Sesija.getInstance().getKorisnik().getId());
                        r.setId(r.getId());
                        r.setNaziv(imeRacuna.getText().toString());
                        r.setIkona(adapterAddRacun.arrayList.get(adapterAddRacun.focusedItemRacun).getRawIkonaRacuna());
                        r.setPocetno_stanje(Float.parseFloat(stanjeRacuna.getText().toString()));
                        r.setValuta(odabranaValuta);
                        MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(r);
                        Retrofit retrofit= RetrofitInstance.getInstance();
                        RestApiImplementor api=retrofit.create(RestApiImplementor.class);
                        Call<Void> pozivUnosa = api.AzurirajRacun(r.getId(), RequestBody.create(MediaType.parse("text/plain"), "naziv"), RequestBody.create(MediaType.parse("text/plain"), String.valueOf(imeRacuna.getText())));
                        pozivUnosa.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.e("Racun", "azuriran racun u bazi");
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                        Toast.makeText(getContext(), "Ažuriran račun " + r.getNaziv(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_LONG).show();
                    }

                    onDialogRacunResult.finish();
                }
                RacunAddDialog.this.dismiss();
            }

        }
    }
    private void PostaviRecycleView(){
        recyclerView= (RecyclerView) findViewById(R.id.recyclerViewAddRacun);
        CustomAdapterAddRacun adapter = new CustomAdapterAddRacun(getContext(), VratiListuIkona());
        adapterAddRacun = adapter;
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }
    private int OdaberiSpinner(){
        for(int i =0; i<valuta.getCount(); i++){
            if(valuta.getItemAtPosition(i).toString().equalsIgnoreCase(racun.getValuta())){
                return i;
            }
        }
        return 0;
    }
    private void oznaciIkonu(String ikona){
        adapterAddRacun.focusedItemRacun=dohvatiIndexIkone(ikona);
    }
    private int dohvatiIndexIkone(String ikona){
        for(int i=0; i<ikoneRacuna.length; i++)
            if(ikoneRacuna[i].equalsIgnoreCase(ikona)){
                return i;
            }
        return 0;
    }

}
