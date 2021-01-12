package air.foi.hr.moneymaker.fragmenti;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import air.foi.hr.core.database.MockData;
import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.AnalizaViewModel;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;

public class AnalizaFragment extends Fragment {
    private AnalizaViewModel viewModel;
    private View view;
    private Context context;

    public static MyDatabase database;
    List <Transakcija> transakcijas = database.getInstance(context).getTransakcijaDAO().DohvatiSveTransakcije();
    List<KategorijaTransakcije> kategorijaTransakcijes = database.getInstance(context).getKategorijaTransakcijeDAO().DohvatiSveKategorijeTransakcije();
    List<Racun> racuns = database.getInstance(context).getRacunDAO().DohvatiSveRacune();

    float ukNovac;
    float prosDan=0;
    float trenDan=0;
    float tjedan=0;

    String sve;
    String currentYear;
    String currentMonth;

    Boolean pritisnut=false;

    PieChart pieChart;
    BarChart barChart;

    Spinner dropRac;
    Spinner dropValuta;

    private Button buttonTrošak;
    private Button buttonPrihod;
    private Button buttonOboje;

    private ImageButton buttonPostavke;
    private ImageButton buttonDesno;
    private ImageButton buttonLijevo;

    private TextView ukupanTP;

    String odabrani="Svi računi";
    String odabranaValuta="HRK";

    private TextView textProsDan;
    private TextView textTrenutDan;
    private TextView textTjedan;
    private TextView textVrijeme;

    List<Racun> potrebniRačuni = new ArrayList<>();
    List<Transakcija> sveTransakcijeTroškovaValute= new ArrayList<Transakcija>();
    List<Transakcija> sveTransakcijePrihodaValute= new ArrayList<Transakcija>();
    List<Transakcija> sveTransakcijeValute= new ArrayList<Transakcija>();

    SimpleDateFormat formaterMMyy = new SimpleDateFormat("MMM yyyy");
    SimpleDateFormat formateryy = new SimpleDateFormat("yyyy");
    SimpleDateFormat formaterDate = new SimpleDateFormat("yyyy-MM-dd");
    Calendar kalendarVrijeme = Calendar.getInstance();
    Calendar danasDanTjedan =  Calendar.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_analiza, container, false);
        mockData();
        InicijalizacijaVarijabli();
        bojeIzmjena(buttonTrošak,buttonPrihod,buttonOboje);
        spinnerValuta(2);
        ukNovac=0;
        return view;
    }


    private void InicijalizacijaVarijabli() {
        dropRac=view.findViewById(R.id.racunDrop);
        dropValuta=view.findViewById(R.id.spinnerValuta);
        spinnerValuta(2);

        textVrijeme = view.findViewById(R.id.vrijemeFilter);
        buttonDesno = view.findViewById(R.id.buttonDesno);
        buttonLijevo= view.findViewById(R.id.buttonLijevo);

        buttonPostavke = view.findViewById(R.id.buttonPostavke);

        buttonTrošak = view.findViewById(R.id.buttonTrosak);
        buttonPrihod= view.findViewById(R.id.buttonPrihod);
        buttonOboje = view.findViewById(R.id.buttonUK);

        textProsDan = view.findViewById(R.id.prosDan);
        textTrenutDan = view.findViewById(R.id.trenDan);
        textTjedan = view.findViewById(R.id.tjedan);

        ukupanTP = view.findViewById(R.id.iznosTP);
        barChart=(BarChart) view.findViewById(R.id.chartBar);
        barChart.setVisibility(view.INVISIBLE);


        buttonTrošak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bojeIzmjena(buttonTrošak,buttonPrihod,buttonOboje);
                spinnerValuta(2);
                ukNovac=0;
            }
        });

        buttonPrihod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bojeIzmjena(buttonPrihod,buttonTrošak,buttonOboje);
                spinnerValuta(1);
                ukNovac=0;
            }
        });

        buttonOboje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bojeIzmjena(buttonOboje,buttonTrošak,buttonPrihod);
                spinnerValuta(3);
                ukNovac=0;
            }
        });

        buttonPostavke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.POSTAVKE,getFragmentManager());
            }
        });

        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(AnalizaViewModel.class);
        viewModel.konstruktor(getContext(), (BottomNavigationView) view.findViewById(R.id.bottomNav));
        viewModel.UpravljanjeNavigacijom(getFragmentManager());

    }


    private void spinnerValuta(final int brojP){
        List<String> items = new ArrayList<>();
        items.add("HRK");
        for (Racun rac:racuns){
            if(!items.contains(rac.getValuta())){
                items.add(rac.getValuta());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);

        dropValuta.setAdapter(adapter);
        dropValuta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                odabranaValuta = adapterView.getItemAtPosition(i).toString();
                spinnerRacun(brojP);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void spinnerRacun(final int brojP){
        List<String> items = new ArrayList<>();
        potrebniRačuni.clear();
        items.add("Svi računi");
        for (Racun rac:racuns){
            if(odabranaValuta.equals(rac.getValuta())){
                items.add(rac.getNaziv());
                potrebniRačuni.add(rac);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        dropRac.setAdapter(adapter);
        dropRac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                odabrani = adapterView.getItemAtPosition(i).toString();
                if(brojP==1||brojP==2){
                    setUpPieChart(brojP,odabrani);
                }
                else if(brojP==3){
                    setUpBarChart(brojP,odabrani);
                }
                ukNovac=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void setUpPieChart(int broj, String odabrani) {
        pieChart = (PieChart) view.findViewById(R.id.chart);
        pieChart.setHoleRadius(35f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setCenterTextSize(15);
        pieChart.animateY(1000);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);

        postavaFiltiranjePoMjesecu(broj,odabrani);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                int pos1= e.toString().indexOf("y: ");

                String iznos = e.toString().substring(pos1+3);
                Toast toast=Toast.makeText(getActivity(), "Kategorija:"+ pe.getLabel() +"\n"+"Novac: " + iznos + " " + odabranaValuta, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 1450);
                toast.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void setUpBarChart(int broj, String odabrani){
        barChart.setDescription(null);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        postavaFiltiranjePoMjesecu(broj,odabrani);

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                BarEntry pe = (BarEntry) e;
                int pos1= e.toString().indexOf("y: ");
                String iznos = e.toString().substring(pos1+3);

                if(!iznos.equals("0.0")){
                    Toast toast=Toast.makeText(getActivity(), iznos + " " + odabranaValuta, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 1450);
                    toast.show();
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }


    private void postavaFiltiranjePoMjesecu(final int broj, final String odabrani) {
        if (broj==1||broj==2){
            kalendarVrijeme = Calendar.getInstance();
            currentMonth= formaterMMyy.format(kalendarVrijeme.getTime());
            textVrijeme.setText(currentMonth);
            filtracijaVrijeme(odabrani,broj, currentMonth);
            strelice(true);

            buttonLijevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vrijednost = textVrijeme.getText().toString();
                    try {
                        Date datum=formaterMMyy.parse(vrijednost);
                        kalendarVrijeme.setTime(datum);
                        kalendarVrijeme.add(Calendar.MONTH,-1);
                        textVrijeme.setText(formaterMMyy.format(kalendarVrijeme.getTime()));
                        strelice(false);
                        filtracijaVrijeme(odabrani,broj,formaterMMyy.format(kalendarVrijeme.getTime()));

                        buttonDesno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                kalendarVrijeme.add(Calendar.MONTH,1);
                                textVrijeme.setText(formaterMMyy.format(kalendarVrijeme.getTime()));
                                filtracijaVrijeme(odabrani, broj,formaterMMyy.format(kalendarVrijeme.getTime()));
                                if(formaterMMyy.format(kalendarVrijeme.getTime()).equals(currentMonth)){
                                    strelice(true);
                                }
                                else{
                                    strelice(false);
                                }}
                        });
                    } catch (ParseException e) {
                        e.printStackTrace();}}
            });
        }

        else if (broj==3){
            kalendarVrijeme = Calendar.getInstance();
            currentYear= formateryy.format(kalendarVrijeme.getTime());
            textVrijeme.setText(currentYear);
            strelice(true);
            filtracijaVrijeme(odabrani,broj, currentYear);

            buttonLijevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vrijednost = textVrijeme.getText().toString();
                    try {
                        Date datum=formateryy.parse(vrijednost);
                        kalendarVrijeme.setTime(datum);
                        kalendarVrijeme.add(Calendar.YEAR,-1);
                        textVrijeme.setText(formateryy.format(kalendarVrijeme.getTime()));
                        filtracijaVrijeme(odabrani,broj, formateryy.format(kalendarVrijeme.getTime()));
                        strelice(false);
                        buttonDesno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                kalendarVrijeme.add(Calendar.YEAR,1);
                                textVrijeme.setText(formateryy.format(kalendarVrijeme.getTime()));
                                filtracijaVrijeme(odabrani,broj, formateryy.format(kalendarVrijeme.getTime()));
                                if(formateryy.format(kalendarVrijeme.getTime()).equals(currentYear)){
                                    strelice(true);
                                }
                                else{
                                    strelice(false);
                                }
                            }
                        });

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }    }
            });
        }
    }

    private void filtracijaVrijeme(String odabrani, int broj, String vrijeme){
        sveVrijednost(vrijeme,broj);
        unesiDanTjedan(vrijeme,broj);
        SimpleDateFormat formater = null;
        String zeljeniMjesec = null;
        List<Transakcija> zeljeneTransakcije=new ArrayList<>();

        if(broj==1 || broj==2){
            formater=formaterMMyy;
        }
        else if(broj==3){
            formater=formateryy;
        }

        for(Transakcija t: sveTransakcijeValute){
            try {
                Date datum = new SimpleDateFormat("yyyy-MM-dd").parse(t.getDatum());
                zeljeniMjesec=formater.format(datum);
                if(zeljeniMjesec.equals(vrijeme)){
                    zeljeneTransakcije.add(t);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        filtracijaPoRačunima(broj,odabrani, zeljeneTransakcije);


    }

    private void filtracijaPoRačunima (int broj, String odabrani, List<Transakcija>potrebneTransakcije){
        sveTransakcijeValute.clear();
        List<Transakcija> transakcijeRačuna = new ArrayList<Transakcija>();

        int id_rac=0;
        for (Transakcija t:potrebneTransakcije){
            for(Racun racT:potrebniRačuni){
                if(broj==1||broj==2){
                    if (t.getTipTransakcije()==broj&&t.getRacunTerecenja()==racT.getId()){
                        sveTransakcijeValute.add(t);
                    }
                }
                else if(broj==3){
                    if (t.getRacunTerecenja()==racT.getId()){
                        sveTransakcijeValute.add(t);
                    }
                }
            }
        }

        ukNovac=0;

        if (odabrani.equals("Svi računi")){
            if(broj==1||broj==2){
                dodajDataSet(sveTransakcijeValute,broj);
            }
            else if (broj==3){
                dodajDataSetBar(sveTransakcijeValute);
            }
        }

        else{

            for (Racun rac:potrebniRačuni){
                if(rac.getNaziv().equals(odabrani)){
                    id_rac=rac.getId();
                }
            }
            for (Transakcija tranRac:sveTransakcijeValute){
                if(tranRac.getRacunTerecenja()==id_rac){
                    transakcijeRačuna.add(tranRac);
                }
            }

            if(broj==1||broj==2){
                dodajDataSet(transakcijeRačuna,broj);
            }
            else if (broj==3){
                dodajDataSetBar(transakcijeRačuna);
            }



        }

    }



    private void unesiDanTjedan(String vrijeme, int broj) {
        sveTransakcijeTroškovaValute.clear();
        for (Transakcija t:transakcijas){
            for(Racun racT:potrebniRačuni){
                if (t.getRacunTerecenja()==racT.getId()&&t.getTipTransakcije()==2){
                    sveTransakcijeTroškovaValute.add(t);
                }
            }
        }
        danasnjiDatum(vrijeme,broj);
        prosjecniDatum(vrijeme,broj);
        ukupnoTjedan(vrijeme,broj);
    }

    private void danasnjiDatum(String vrijeme, int broj){
        String danasDat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String danasMjesec = new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(new Date());

        if(broj==1||broj==2){
            trenDan=0;
            if (danasMjesec.equals(vrijeme)){
                for (Transakcija tr:sveTransakcijeTroškovaValute){
                    if(danasDat.equals(tr.getDatum())){
                        trenDan+=tr.getIznos();}}
            }

            else{
                try {
                    Date converted = formaterMMyy.parse(vrijeme);
                    danasDanTjedan = Calendar.getInstance();
                    danasDanTjedan.setTime(converted);
                    danasDanTjedan.set(Calendar.DAY_OF_MONTH, danasDanTjedan.getActualMaximum(Calendar.DAY_OF_MONTH));
                    String formiranoVrijeme=formaterDate.format(danasDanTjedan.getTime());

                    for (Transakcija tr2:sveTransakcijeTroškovaValute){
                        if(formiranoVrijeme.equals(tr2.getDatum())){
                            trenDan+=tr2.getIznos();}}
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            textTrenutDan.setVisibility(view.VISIBLE);
            textTrenutDan.setText("Danas"+"\n"+ String.valueOf(Math.round(trenDan*100.0)/100.0)+" "+odabranaValuta);
        }

        else if(broj==3){
            textTrenutDan.setVisibility(view.INVISIBLE);
        }
    }

    private void prosjecniDatum(String vrijeme,int broj){
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM");
        int brojDana = 0;
        if(broj==1||broj==2){
            float avera=0;

            try {
                Date datum = formaterMMyy.parse(vrijeme);
                danasDanTjedan.setTime(datum);
                danasDanTjedan.add(Calendar.MONTH,-1);
                brojDana=danasDanTjedan.getActualMaximum(Calendar.DAY_OF_MONTH);
                prosDan=0;
                String formiranoVrijeme=formater.format(danasDanTjedan.getTime());

                for (Transakcija t: sveTransakcijeTroškovaValute){
                    Date datumTransakcije = new SimpleDateFormat("yyyy-MM-dd").parse(t.getDatum());
                    String zeljeniMjesec=formater.format(datumTransakcije);
                    if(formiranoVrijeme.equals(zeljeniMjesec)){
                        avera+=t.getIznos();}}
            } catch (ParseException e) {
                e.printStackTrace();
            }

            prosDan=avera/brojDana;
            textProsDan.setVisibility(view.VISIBLE);
            textProsDan.setText("Dan(posj.)"+"\n"+ String.valueOf(Math.round(prosDan*100.0)/100.0)+" "+odabranaValuta);
        }
        else if(broj==3){
            textProsDan.setVisibility(view.INVISIBLE);
        }
    }

    private void ukupnoTjedan (String vrijeme, int broj){
        String danasMjesec = new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(new Date());
        if (broj==1||broj==2){
            String pocetniDatum;
            String zavrsniDatum;
            float zbr = 0;
            tjedan=0;

            if (danasMjesec.equals(vrijeme)){
                danasDanTjedan = Calendar.getInstance();
                danasDanTjedan.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                pocetniDatum =df.format(danasDanTjedan.getTime());
                danasDanTjedan.add(Calendar.DATE, 6);
                zavrsniDatum = df.format(danasDanTjedan.getTime());

                for (Transakcija t:sveTransakcijeTroškovaValute){
                    if (t.getDatum().compareTo(pocetniDatum)>=0){
                        if (t.getDatum().compareTo(zavrsniDatum)<=0){
                            zbr+=t.getIznos();
                        }
                    }
                }
            }

            else{
                try {
                    Date converted = formaterMMyy.parse(vrijeme);
                    danasDanTjedan.setTime(converted);
                    danasDanTjedan.set(Calendar.DAY_OF_MONTH, danasDanTjedan.getActualMaximum(Calendar.DAY_OF_MONTH));
                    danasDanTjedan.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                    danasDanTjedan.add(Calendar.DATE, 28);
                    pocetniDatum=formaterDate.format(danasDanTjedan.getTime());
                    danasDanTjedan.add(Calendar.DATE, 6);
                    zavrsniDatum = formaterDate.format(danasDanTjedan.getTime());

                    for (Transakcija t:sveTransakcijeTroškovaValute){
                        if (t.getDatum().compareTo(pocetniDatum)>=0){
                            if (t.getDatum().compareTo(zavrsniDatum)<=0){
                                zbr+=t.getIznos();
                            }
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            tjedan=zbr/7;
            textTjedan.setVisibility(view.VISIBLE);
            textTjedan.setText("Tjedan"+"\n"+ String.valueOf(Math.round(tjedan*100.0)/100.0)+" "+odabranaValuta);
        }
        else if(broj==3){
            textTjedan.setVisibility(view.INVISIBLE);
        }
    }


    private void sveVrijednost(String vrijeme, int broj){
        float prihod=0;
        float trosak=0;
        SimpleDateFormat format = null;
        String zeljeniMjesec = null;
        sveTransakcijeValute.clear();

        if(broj==1 || broj==2){
            format = formaterMMyy;
        }
        else if(broj==3){
            format = formateryy;
        }

        for (Transakcija t:transakcijas){
            for(Racun racT:potrebniRačuni){
                if (t.getRacunTerecenja()==racT.getId()){
                    sveTransakcijeValute.add(t);
                }
            }
        }

        for(Transakcija t: sveTransakcijeValute){
            try {
                Date datum = new SimpleDateFormat("yyyy-MM-dd").parse(t.getDatum());
                zeljeniMjesec=format.format(datum);
                if(zeljeniMjesec.equals(vrijeme) && t.getTipTransakcije()==1){
                    prihod+=t.getIznos();
                }
                if(t.getTipTransakcije()==2 && zeljeniMjesec.equals(vrijeme)){
                    trosak+=t.getIznos();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        sve=String.valueOf(prihod-trosak);
        ukupanTP.setText(sve);

    }

    private void dodajDataSet(List<Transakcija> potrebneTransakcije, int broj) {
        pieChart.setVisibility(view.VISIBLE);
        barChart.setVisibility(view.INVISIBLE);

        ArrayList<PieEntry> yEntries = new ArrayList<>();
        float ukIznos=0;

        for (KategorijaTransakcije kat2: kategorijaTransakcijes){
            for (Transakcija t: potrebneTransakcije){
                if(t.getKategorijaTransakcije()==kat2.getId()){
                    ukNovac+=t.getIznos();
                    ukIznos+=t.getIznos();
                }
            }

            if (ukIznos!=0){

                yEntries.add(new PieEntry(ukIznos,kat2.getCategoryName()));
                ukIznos=0;
            }
        }

        PieDataSet dataSet= new PieDataSet(yEntries, "Kategorije");
        dataSet.setSliceSpace(1);
        dataSet.setValueTextSize(12);

        dataSet.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setUsePercentValues(true);


        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        pieChart.getLegend().setEnabled(false);

        PieData pieData = new PieData(dataSet);

        if (broj == 2){
            pieChart.setCenterText("Troškovi: " + "\n" + ukNovac + " " + odabranaValuta);}

        else if (broj==1){
            pieChart.setCenterText("Prihodi: " + "\n" + ukNovac + " "+ odabranaValuta);}


        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void dodajDataSetBar(List<Transakcija> potrebneTransakcije){
        pieChart.setVisibility(view.INVISIBLE);
        barChart.setVisibility(view.VISIBLE);

        SimpleDateFormat format = new SimpleDateFormat("MMM");
        String zeljeniMjesec = null;
        float zbrT=0;
        float zbrP=0;
        ArrayList<String> xVal = new ArrayList<>();
        ArrayList yVal1=  new ArrayList<>();
        ArrayList yVal2 = new ArrayList<>();

        danasDanTjedan=Calendar.getInstance();
        for(int i=0; i<12; i++){
            danasDanTjedan.set(Calendar.MONTH,Calendar.JANUARY);
            danasDanTjedan.add(Calendar.MONTH, i);
            xVal.add(format.format(danasDanTjedan.getTime()));
        }

        for (int i=0; i<12; i++){
            for (Transakcija t: potrebneTransakcije){
                Date datum = null;
                try {
                    datum = new SimpleDateFormat("yyyy-MM-dd").parse(t.getDatum());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                zeljeniMjesec=format.format(datum);
                if(xVal.get(i).equals(zeljeniMjesec)){
                    if(t.getTipTransakcije()==1){
                        zbrP+=t.getIznos();

                    }
                    else if(t.getTipTransakcije()==2){
                        zbrT+=t.getIznos();
                    }
                }
            }

            yVal1.add(new BarEntry(i+1, (float) zbrP));
            yVal2.add(new BarEntry(i+1, (float) zbrT));
            zbrP=0;
            zbrT=0;
        }

        BarDataSet set1, set2;
        set1=new BarDataSet(yVal1, "Prihod");
        set1.setColor(Color.GREEN);
        set2=new BarDataSet(yVal2, "Trosak");
        set2.setColor(Color.RED);

        BarData barData=new BarData(set1,set2);
        barData.setDrawValues(false);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisRight().setAxisMinimum(0);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVal));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(12);
        barData.setBarWidth(0.4f);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0+ barChart.getBarData().getGroupWidth(0.3f, 0f)*12);
        barChart.groupBars(0, 0.3f, 0f);
        barChart.invalidate();
    }


    private void strelice(Boolean first){

        if (first) {
            buttonLijevo.setVisibility(View.VISIBLE);
            buttonDesno.setVisibility(View.INVISIBLE);
        }

        else{
            buttonLijevo.setVisibility(View.VISIBLE);
            buttonDesno.setVisibility(View.VISIBLE);
        }

    }

    private void bojeIzmjena(Button btn, Button btn2, Button btn3){
        pritisnut=true;
        List<Button> buttons = new ArrayList<>();
        Collections.addAll(buttons,btn2,btn3);

        if (pritisnut==true){
            btn.setBackgroundColor(Color.parseColor("#3c1361"));
            for (Button but: buttons){
                but.setBackgroundColor(Color.parseColor("#7c5295"));
            }
        }
        pritisnut=false;
    }

    private void mockData(){

        if (!transakcijas.isEmpty()){
            for (Transakcija t:transakcijas){
                Log.d("ucitaj","Transakcije"+t.getId());
            }
            //MockData.writeAll(context);

        }
        else {
            Log.d("ucitaj","Transakcije nema");
            MockData.writeAll(context);
        }


        if (!racuns.isEmpty()){
            for (Racun r:racuns){
                Log.d("ucitajR","Rac:"+ r.getNaziv()+r.getId());
            }
            //MockData.writeAllR(context);

        }

        else { MockData.writeAllR(context);}



    }

}