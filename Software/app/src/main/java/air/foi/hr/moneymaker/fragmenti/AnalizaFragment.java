package air.foi.hr.moneymaker.fragmenti;

import android.content.Context;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import air.foi.hr.core.database.MockData;
import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.AnalizaViewModel;

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
    int month;
    String currentYear;
    String lastYear;

    Boolean pritisnut=false;

    PieChart pieChart;
    //LineChart lineChart;
    //BarChart barChart;

    Spinner dropRac;
    Spinner dropTime;

    private Button buttonTrošak;
    private Button buttonPrihod;
    private Button buttonOboje;
    private Button buttonNedavno;

    private ImageButton buttonPostavke;
    private ImageButton buttonInfo;
    private ImageButton buttonDesno;
    private ImageButton buttonLijevo;

    private TextView ukupanTP;
    String odabrani="Svi računi";

    private String odabranoVrijeme;

    private TextView textProsDan;
    private TextView textTrenutDan;
    private TextView textTjedan;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_analiza, container, false);
        mockData();
        InicijalizacijaVarijabli();
        spinnerTime(2);
        spinnerRacun(2);
        ukNovac=0;
        return view;
    }

    private void InicijalizacijaVarijabli() {
        dropRac=view.findViewById(R.id.racunDrop);
        spinnerRacun(2);

        dropTime=view.findViewById(R.id.spinnerMjeseci);

        buttonDesno = view.findViewById(R.id.buttonDesno);
        buttonLijevo= view.findViewById(R.id.buttonLijevo);

        buttonPostavke = view.findViewById(R.id.buttonPostavke);
        buttonInfo= view.findViewById(R.id.buttonInfo);

        buttonTrošak = view.findViewById(R.id.buttonTrosak);
        buttonPrihod= view.findViewById(R.id.buttonPrihod);
        buttonOboje = view.findViewById(R.id.buttonUK);
        buttonNedavno = view.findViewById(R.id.buttonNedavnaPotrosnja);
        pocetnaBoja(buttonTrošak,buttonPrihod,buttonOboje,buttonNedavno);

        textProsDan = view.findViewById(R.id.prosDan);
        textTrenutDan = view.findViewById(R.id.trenDan);
        textTjedan = view.findViewById(R.id.tjedan);
        unesiDanTjedan();

        ukupanTP = view.findViewById(R.id.iznosTP);
        sveVrijednost(transakcijas);

        buttonTrošak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerTime(2);
                bojeIzmjena(buttonTrošak,buttonPrihod,buttonOboje,buttonNedavno);
                spinnerRacun(2);
                ukNovac=0;

            }
        });

        buttonPrihod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerTime(1);
                bojeIzmjena(buttonPrihod,buttonTrošak,buttonOboje,buttonNedavno);
                spinnerRacun(1);
                ukNovac=0;
            }
        });

        buttonOboje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerTime(3);
                bojeIzmjena(buttonOboje,buttonTrošak,buttonPrihod,buttonNedavno);
                //setUpBarChart();
                ukNovac=0;
            }
        });

        buttonNedavno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerTime(4);
                bojeIzmjena(buttonNedavno,buttonTrošak,buttonPrihod,buttonOboje);
                //setUpLineChart();
                ukNovac=0;
            }
        });

        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(AnalizaViewModel.class);
        viewModel.konstruktor(getContext(), (BottomNavigationView) view.findViewById(R.id.bottomNav));
        viewModel.UpravljanjeNavigacijom(getFragmentManager());

    }

    private void unesiDanTjedan() {
        textTjedan.setText("Tjedan"+"\n"+ String.valueOf(tjedan)+" kn");
        textProsDan.setText("Dan(posj.)"+"\n"+ String.valueOf(prosDan)+" kn");
        textTrenutDan.setText("Danas"+"\n"+ String.valueOf(trenDan)+" kn");
    }

    private void spinnerTime(final int broj) {
        ArrayAdapter<String> adapter = null;

        if (broj==1||broj==2){
            vidljivostTime(dropTime,buttonLijevo,buttonDesno);
            List<String> items = new ArrayList<String>(Arrays.asList("Siječanj","Veljača","Ožujak","Travanj","Svibanj","Lipanj","Srpanj","Kolovoz","Rujan","Listopad","Studeni","Prosinac"));
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
            dropTime.setAdapter(adapter);
            month=Calendar.getInstance().get(Calendar.MONTH);
            dropTime.setSelection(month);

        }

        else if (broj==3){
            vidljivostTime(dropTime,buttonLijevo,buttonDesno);
            currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            int parse = Integer.parseInt(currentYear);
            List<String> items = new ArrayList<String>();
            for (int i=0; i<6 ; i++){
                items.add(String.valueOf(parse));
                parse-=1;
            }

            lastYear = items.get(items.size()-1);

            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
            dropTime.setAdapter(adapter);
        }

        else{
            dropTime.setVisibility(View.INVISIBLE);
            buttonLijevo.setVisibility(View.INVISIBLE);
            buttonDesno.setVisibility(View.INVISIBLE);
        }

        dropTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                odabranoVrijeme = adapterView.getItemAtPosition(i).toString();
                if (broj==1||broj==2){strelice("Siječanj","Prosinac", odabranoVrijeme);}
                else if(broj==3){strelice (lastYear,currentYear, odabranoVrijeme);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void vidljivostTime(Spinner spinner, ImageButton btn, ImageButton btn2){
        spinner.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
    }

    private void strelice(String first, String last, String odabrani){
        if (first.equals(odabrani)) {
            buttonLijevo.setVisibility(View.INVISIBLE);
            buttonDesno.setVisibility(View.VISIBLE);
        }

        else if(last.equals(odabrani)){
            buttonLijevo.setVisibility(View.VISIBLE);
            buttonDesno.setVisibility(View.INVISIBLE);

        }

        else{
            buttonLijevo.setVisibility(View.VISIBLE);
            buttonDesno.setVisibility(View.VISIBLE);
        }

    }

    private void spinnerRacun(final int brojP){

        List<String> items = new ArrayList<>();
        items.add("Svi računi");
        for (Racun rac:racuns){
            items.add(rac.getNaziv());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);

        dropRac.setAdapter(adapter);
        dropRac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               odabrani = adapterView.getItemAtPosition(i).toString();
               setUpPieChart(brojP,odabrani);
               ukNovac=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void pocetnaBoja (Button btn, Button btn2, Button btn3, Button btn4){
        List<Button> buttons = new ArrayList<>();
        Collections.addAll(buttons,btn,btn2,btn3,btn4);

        for (Button but: buttons){
            but.setBackgroundColor(Color.parseColor("#7c5295"));
        }
    }

    private void bojeIzmjena(Button btn, Button btn2, Button btn3, Button btn4){
        pritisnut=true;
        List<Button> buttons = new ArrayList<>();
        Collections.addAll(buttons,btn2,btn3,btn4);

        if (pritisnut==true){
            btn.setBackgroundColor(Color.parseColor("#3c1361"));
            for (Button but: buttons){
                but.setBackgroundColor(Color.parseColor("#7c5295"));
            }
        }
        pritisnut=false;
    }

    private void setUpPieChart(int broj, String odabrani) {
        pieChart = (PieChart) view.findViewById(R.id.chart);
        pieChart.setHoleRadius(35f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setCenterTextSize(15);
        pieChart.animateY(1000);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);

        filtracijaPoRačunima(broj,odabrani);

        if (broj == 2){
            pieChart.setCenterText("Troškovi: " + "\n" + ukNovac + " HRK");}

        else if (broj==1){
            pieChart.setCenterText("Prihodi: " + "\n" + ukNovac + " HRK");}

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                int pos1= e.toString().indexOf("y: ");

                String iznos = e.toString().substring(pos1+3);

                Toast toast=Toast.makeText(getActivity(), "Kategorija:"+ pe.getLabel().toString() +"\n"+"Novac: " + iznos + " HRK", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 1450);
                toast.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

   // private void setUpLineChart(){
    //    lineChart=(LineChart) view.findViewById(R.id.chartLine);
  //  }

  //  private void setUpBarChart(){
       // barChart=(BarChart) view.findViewById(R.id.chartBar);
    //}

    private void sveVrijednost(List<Transakcija> potrebneTransakcije){
        float prihod=0;
        float trosak=0;

        for (Transakcija t: potrebneTransakcije){
            if(t.getTipTransakcije()==1){
                prihod+=t.getIznos();
            }
            if(t.getTipTransakcije()==2){
                trosak+=t.getIznos();
            }
        }
        sve=String.valueOf(prihod-trosak);
        ukupanTP.setText(sve + " kn");
    }

    private void dodajDataSet(List<Transakcija> potrebneTransakcije, int broj) {
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

        //boje
        ArrayList<Integer> colors= new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.MAGENTA);
        colors.add(Color.GREEN);
        colors.add(Color.GRAY);
        colors.add(Color.YELLOW);
        dataSet.setColors(colors);

        pieChart.getLegend().setEnabled(false);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void filtracijaPoRačunima (int broj, String odabrani){

        List<Transakcija> transakcijeTroškova= new ArrayList<Transakcija>();
        List<Transakcija> transakcijeRačuna = new ArrayList<Transakcija>();

        int id_rac=0;
        for (Transakcija t:transakcijas){
            if (t.getTipTransakcije()==broj){
                transakcijeTroškova.add(t);
            }
        }


        if (odabrani.equals("Svi računi")){
            dodajDataSet(transakcijeTroškova,broj);
        }
        else{
            for (Racun rac:racuns){
                if(rac.getNaziv().equals(odabrani)){
                    id_rac=rac.getId();
                }
            }
            for (Transakcija tranRac:transakcijeTroškova){
                if(tranRac.getRacunTerecenja()==id_rac){
                    transakcijeRačuna.add(tranRac);
                }
            }
            dodajDataSet(transakcijeRačuna,broj);
        }
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
                Log.d("ucitaj","Rac:"+ r.getNaziv()+r.getId());
            }
            //MockData.writeAllR(context);

        }

        else { MockData.writeAllR(context);}



    }

}