package air.foi.hr.moneymaker.fragmenti;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
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

    PieChart pieChart;
    Spinner racuniDrop;

    private Button buttonTrošak;
    private Button buttonPrihod;
    private Button buttonOboje;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_analiza, container, false);
        mockData();
        InicijalizacijaVarijabli();
        setUpPieChart(2);
        ukNovac=0;
        return view;

    }




    private void InicijalizacijaVarijabli() {
        buttonTrošak = view.findViewById(R.id.buttonTrosak);
        buttonPrihod= view.findViewById(R.id.buttonPrihod);
        buttonOboje = view.findViewById(R.id.buttonUK);



        buttonTrošak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpPieChart(2);
                ukNovac=0;
            }
        });

        buttonPrihod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpPieChart(1);
                ukNovac=0;
            }
        });


        ViewModelProvider.Factory factory=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        viewModel=new ViewModelProvider(this,factory).get(AnalizaViewModel.class);
        viewModel.konstruktor(getContext(), (BottomNavigationView) view.findViewById(R.id.bottomNav));
        viewModel.UpravljanjeNavigacijom(getFragmentManager());

    }

    private void setUpPieChart(int broj) {
        pieChart = (PieChart) view.findViewById(R.id.chart);
        pieChart.setHoleRadius(35f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setCenterTextSize(15);
        pieChart.animateY(1000);
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelColor(Color.parseColor("Red"));


        final List<Transakcija> transakcijeTroškova= new ArrayList<Transakcija>();

        for (Transakcija t:transakcijas){
            if (t.getTipTransakcije()==broj){
                transakcijeTroškova.add(t);
            }
        }


        dodajDataSet(transakcijeTroškova,broj);

        if (broj == 2){
            pieChart.setCenterText("Troškovi: " + "\n" + ukNovac + " HRK");
        }

        else if (broj==1){
            pieChart.setCenterText("Prihodi: " + "\n" + ukNovac + " HRK");

        }

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // Log.d("ucitaj", "onValueSelected: "+ e.toString());
                // Log.d("ucitaj", "onValueSelected: "+ h.toString());

                int pos1= e.toString().indexOf("y: ");
                String iznos = e.toString().substring(pos1+3);
                Log.d("ucitaj", "onValueSelected: "+ iznos);

                float brojac=0;
                //String naziv=null;

                for (Transakcija i: transakcijeTroškova){
                    if(i.getIznos()==Float.parseFloat(iznos)){
                        pos1=i.getKategorijaTransakcije();


                        //String naziv=null;
                        for (KategorijaTransakcije k: kategorijaTransakcijes){
                            if(pos1==k.getId()){
                                //int katID=pos1;
                                //Log.d("ucitajN3", "onValueSelected: "+ katID);
                                brojac+=i.getIznos();
                                //Log.d("ucitaj", "onValueSelected: "+ k.getNaziv());
                                //Toast.makeText(getActivity(), "Kategorija:"+ k.getNaziv() +"\n"+"Novac: " + iznos + "HRK", Toast.LENGTH_SHORT).show();
                                //Log.d("ucitajN3", "onValueSelected: "+ pos1);
                            }

                            //Log.d("ucitajN3", "onValueSelected: "+ k.getNaziv());

                        }
                        //Log.d("ucitajN", "onValueSelected: "+ naziv);
                        break;
                    }
                }
                Log.d("ucitaj", "onValueSelected: " + " " + brojac);
            }

            @Override
            public void onNothingSelected() {

            }
        });

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

            //Log.d("ucitaj","Transakcije"+ukIznos + " "+ kat2.getCategoryName());

            if (ukIznos!=0){
                yEntries.add(new PieEntry(ukIznos,kat2.getCategoryName()));
                ukIznos=0;
            }
        }



        PieDataSet dataSet= new PieDataSet(yEntries, "Kategorije");
        dataSet.setSliceSpace(1);
        dataSet.setValueTextSize(12);

        //boje
        ArrayList<Integer> colors= new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.MAGENTA);
        colors.add(Color.GREEN);
        colors.add(Color.GRAY);
        colors.add(Color.YELLOW);
        dataSet.setColors(colors);

        //zapis
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
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