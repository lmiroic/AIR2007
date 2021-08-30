package air.foi.hr.moneymaker.fragmenti;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Korisnik;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.TipTransakcije;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.moneymaker.MainActivity;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.HomeScreenViewModel;
import air.foi.hr.moneymaker.manager.CustomAdapterHome;
import air.foi.hr.moneymaker.modul.kategorije.ConcreteCategory;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.HomeScreenViewModel;
import air.foi.hr.moneymaker.manager.CustomAdapterHome;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import air.foi.hr.moneymaker.modul.transakcije.ImageFilePath;
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


public class HomeFragment extends Fragment {
    private LiveData<List<KategorijaTransakcije>> sveKategorije;
    private View view;
    private HomeScreenViewModel viewModel;
    RecyclerView recyclerView;
    private CustomAdapterHome adapter;
    private ImageButton btnPostavke;

    SimpleDateFormat formaterDate = new SimpleDateFormat("yyyy-MM-dd");
    Calendar datumPonavljajucegTroska = Calendar.getInstance();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        InicijalizacijaVarijabli();
        PostaviRecycleView();
        ProvjeraPonavljajucihTroskova();
        return view;
    }

    private void PostaviRecycleView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new CustomAdapterHome(getContext());
        viewModel.getKategorijeTransakcije().observe(this, new Observer<List<KategorijaTransakcije>>() {
            @Override
            public void onChanged(List<KategorijaTransakcije> kategorijaTransakcijes) {
                adapter.SetKategorije(kategorijaTransakcijes);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void InicijalizacijaVarijabli() {
        btnPostavke = view.findViewById(R.id.btnPostavke);
        btnPostavke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.POSTAVKE, getFragmentManager());
            }
        });

        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(HomeScreenViewModel.class);
        viewModel.konstruktor(getContext(), (BottomNavigationView) view.findViewById(R.id.bottomNav));
        viewModel.UpravljanjeNavigacijom(getFragmentManager());
    }

    private void UnesiTransakcijuUBazu(Transakcija tranPonavTroska, RequestBody requestIznos, RequestBody requestDatum, RequestBody requestRacunTerecenja, RequestBody requestRacunPrijenosa, RequestBody requestTipTransakcije, RequestBody requestMemo, RequestBody requestOpis, RequestBody requestPonavljajuciTrosak, RequestBody requestIkona, RequestBody requestKorisnik, RequestBody requestIntervalPonavljanja, RequestBody requestKategorijaTransakcije, RequestBody requestPlacenTrosak) {
        Retrofit retrofit = RetrofitInstance.getInstance();
        RestApiImplementor restApiImplementor = retrofit.create(RestApiImplementor.class);
        restApiImplementor.UnesiTransakcijuBezSlike(requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak).enqueue(new Callback<Transakcija>() {
            @Override
            public void onResponse(Call<Transakcija> call, Response<Transakcija> response) {
                if (response.isSuccessful()) {
                    tranPonavTroska.setMemo(response.body().getMemo());

                    //MyDatabase.getInstance(getContext()).getTransakcijaDAO().UnosTransakcije(tranPonavTroska);
                    MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(tranPonavTroska);
                }
            }

            @Override
            public void onFailure(Call<Transakcija> call, Throwable t) {

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

    private void ProvjeraPonavljajucihTroskova() {
        List<Transakcija> svetransakcije = MyDatabase.getInstance(getContext()).getTransakcijaDAO().DohvatiTransakcijePonavljajucegTroska(true);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        for (Transakcija t : svetransakcije) {
            if (t.getIntervalPonavljanja().equals(date) && t.isPlacenTrosak() == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Obavijest o ponavljajućem trošku!");
                builder.setMessage("Imate ponavljajuci trosak u iznosu od" + t.getIznos() + " za " + t.getOpis());
                builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Plati", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Racun racunTerecenja = MyDatabase.getInstance(getContext()).getRacunDAO().DohvatiRacun(t.getRacunTerecenja());
                        if (t.getIznos() <= racunTerecenja.getPocetno_stanje()) {
                            Transakcija ponavljajuciTrosak = new Transakcija();
                            Date datumPonavljanjaTroska = null;
                            try {
                                datumPonavljanjaTroska = formaterDate.parse(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            datumPonavljajucegTroska.setTime(datumPonavljanjaTroska);
                            datumPonavljajucegTroska.add(Calendar.MONTH, 1);
                            ponavljajuciTrosak.setIntervalPonavljanja(formaterDate.format(datumPonavljajucegTroska.getTime()));
                            ponavljajuciTrosak.setOpis(t.getOpis());
                            ponavljajuciTrosak.setDatum(date);
                            ponavljajuciTrosak.setKategorijaTransakcije(t.getKategorijaTransakcije());
                            ponavljajuciTrosak.setPonavljajuciTrosak(true);
                            ponavljajuciTrosak.setTipTransakcije(t.getTipTransakcije());
                            ponavljajuciTrosak.setKorisnik(t.getKorisnik());
                            ponavljajuciTrosak.setIznos(t.getIznos());
                            ponavljajuciTrosak.setOpis(t.getOpis());
                            ponavljajuciTrosak.setMemo(t.getMemo());
                            ponavljajuciTrosak.setPlacenTrosak(false);
                            ponavljajuciTrosak.setRacunTerecenja(t.getRacunTerecenja());

                            RequestBody requestPonavljajuciTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(true));
                            RequestBody requestIntervalPonavljanja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(formaterDate.format(datumPonavljajucegTroska.getTime())));
                            RequestBody requestIznos = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(t.getIznos()));
                            RequestBody requestDatum = RequestBody.create(MediaType.parse("text/plain"), date);
                            RequestBody requestRacunTerecenja = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(t.getRacunTerecenja()));
                            RequestBody requestRacunPrijenosa = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
                            RequestBody requestTipTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(TipTransakcije.TROSAK));
                            RequestBody requestMemo = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(t.getMemo()));
                            RequestBody requestOpis = RequestBody.create(MediaType.parse("text/plain"), t.getOpis());
                            RequestBody requestIkona = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody requestKorisnik = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Sesija.getInstance().getKorisnik().getId()));
                            RequestBody requestKategorijaTransakcije = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(t.getKategorijaTransakcije()));
                            RequestBody requestPlacenTrosak = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));

                            UnesiTransakcijuUBazu(ponavljajuciTrosak, requestIznos, requestDatum, requestRacunTerecenja, requestRacunPrijenosa, requestTipTransakcije, requestMemo, requestOpis, requestPonavljajuciTrosak, requestIkona, requestKorisnik, requestIntervalPonavljanja, requestKategorijaTransakcije, requestPlacenTrosak);
                            MyDatabase.getInstance(getContext()).getTransakcijaDAO().UnosTransakcije(ponavljajuciTrosak);

                            float pocetnoStanje = racunTerecenja.getPocetno_stanje();
                            racunTerecenja.setPocetno_stanje(pocetnoStanje - t.getIznos());

                            MyDatabase.getInstance(getContext()).getRacunDAO().AzurirajRacun(racunTerecenja);
                            AzurirajRacunUBazi(racunTerecenja);

                            t.setPlacenTrosak(true);
                            t.setPonavljajuciTrosak(false);
                            MyDatabase.getInstance(getContext()).getTransakcijaDAO().AzurirajTransakciju(t);
                            RequestBody requestIdA = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(t.getId()));
                            RequestBody requestPonavljajuciTrosakA = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));
                            RequestBody requestIntervalPonavljanjaA = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(formaterDate.format(datumPonavljajucegTroska.getTime())));
                            RequestBody requestIznosA = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(t.getIznos()));
                            RequestBody requestDatumA = RequestBody.create(MediaType.parse("text/plain"), t.getDatum());
                            RequestBody requestRacunTerecenjaA = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(t.getRacunTerecenja()));
                            RequestBody requestRacunPrijenosaA = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
                            RequestBody requestTipTransakcijeA = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(TipTransakcije.TROSAK));
                            RequestBody requestMemoA = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(t.getMemo()));
                            RequestBody requestOpisA = RequestBody.create(MediaType.parse("text/plain"), t.getOpis());
                            RequestBody requestIkonaA = RequestBody.create(MediaType.parse("text/plain"), "");
                            RequestBody requestKorisnikA = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Sesija.getInstance().getKorisnik().getId()));
                            RequestBody requestKategorijaTransakcijeA = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(t.getKategorijaTransakcije()));
                            RequestBody requestPlacenTrosakA = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(true));
                            AzurirajTransakcijuUBazi(requestIdA, requestIznosA, requestDatumA, requestRacunTerecenjaA, requestRacunPrijenosaA, requestTipTransakcijeA, requestMemoA, requestOpisA, requestPonavljajuciTrosakA, requestIkonaA, requestKorisnikA, requestIntervalPonavljanjaA, requestKategorijaTransakcijeA, requestPlacenTrosakA);

                            dialog.dismiss();
                        } else
                            Toast.makeText(getContext(), "Nemate dovoljno sredstva na računu za plaćanje troškova!", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();
            }
        }

    }


}