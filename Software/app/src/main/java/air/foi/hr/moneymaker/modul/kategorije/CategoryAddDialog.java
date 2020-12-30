package air.foi.hr.moneymaker.modul.kategorije;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.core.modul.kategorije.OnDialogCategoryResult;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.manager.CategoryAddModel;
import air.foi.hr.moneymaker.manager.CustomAdapterHome;
import air.foi.hr.moneymaker.manager.CustomAdapterKategorije;

public class CategoryAddDialog extends Dialog implements View.OnClickListener {
    private Context context;
    public int tipTroska=0;
    private Button button,button2;
    private EditText nazivKategorije;
    private RecyclerView recyclerView;
    public Spinner spinner;
    private OnDialogCategoryResult onDialogCategoryResult;
    private ArrayList<CategoryAddModel> arrayList;
    private CustomAdapterKategorije customAdapterKategorije;
    private KategorijaTransakcije kategorijaTransakcije;
    public  String ikone[] = {"ic_book","ic_fitness","ic_gaming","ic_kucni_ljubimci","ic_lijekovi","ic_obrazovanje","ic_pokloni","ic_travelling","ic_bank","ic_coffee","ic_growth","ic_car_service","ic_money_transfer","ic_life_insurance","ic_gorivo","ic_hrana","ic_placa","ic_odjeca","ic_rezije"};
    public CategoryAddDialog(@NonNull Context context) {
        super(context);
    }

    public CategoryAddDialog(@NonNull Context context, KategorijaTransakcije kategorijaTransakcije) {
        super(context);
        this.kategorijaTransakcije = kategorijaTransakcije;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_kategorije);
        button=findViewById(R.id.btnDialogKategorije);
        button2=findViewById(R.id.btnDeleteKategorije);
        button2.setVisibility(View.GONE);
        spinner=findViewById(R.id.cmboxTipTransakcije);
        nazivKategorije=findViewById(R.id.editTextDialogText);
        PostaviRecycleView();
        button.setOnClickListener(this);
        PostaviSpinner();
        if(kategorijaTransakcije!=null){
            nazivKategorije.setText(kategorijaTransakcije.getNaziv());
            spinner.setSelection(kategorijaTransakcije.getTipTransakcije()-1);
            oznaciKategoriju(kategorijaTransakcije.getIkonaKategorije());
            button.setText("Ažuriraj");
            button.setOnClickListener(this);
            button2.setVisibility(View.VISIBLE);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDialogCategoryResult != null) {
                        KategorijaTransakcije kt = MyDatabase.getInstance(context).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcijePremaNazivu(kategorijaTransakcije.getNaziv());
                        MyDatabase.getInstance(context).getKategorijaTransakcijeDAO().IzbrisiKategorijuTransakcije(kt);
                        Toast.makeText(getContext(), "Izbrisana kategorija " + kt.getNaziv(), Toast.LENGTH_SHORT).show();
                        onDialogCategoryResult.finish();
                    }
                    CategoryAddDialog.this.dismiss();
                }
            });
        }

    }
    private void PostaviSpinner(){
        final String []tipovi=new String[]{"Prihod","Trošak","Prijenos"};
        ArrayAdapter<String>adapter=new ArrayAdapter<>(getContext(),R.layout.spinner_single_item,tipovi);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (tipovi[position]){
                    case "Prihod":
                        tipTroska=1;
                        break;
                    case "Trošak":
                        tipTroska=2;
                        break;
                    case "Prijenos":
                        tipTroska=3;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setOnDialogCategoryResult(OnDialogCategoryResult onDialogCategoryResult) {
        this.onDialogCategoryResult = onDialogCategoryResult;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDialogKategorije:
                new OkListener().onClick(v);
                break;
        }
    }
    private void PostaviRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewKat);
        CustomAdapterKategorije adapter = new CustomAdapterKategorije(getContext(),VratiListuIkona());
        customAdapterKategorije=adapter;
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }
    private List<CategoryAddModel>VratiListuIkona(){

        arrayList = new ArrayList<>();
        for (int i = 0; i < ikone.length; i++) {
            CategoryAddModel categoryAddModel = new CategoryAddModel();
            categoryAddModel.setIkona(ikone[i]);
            arrayList.add(categoryAddModel);
        }
        return arrayList;
    }
    private class OkListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(kategorijaTransakcije==null) {
                if (onDialogCategoryResult != null) {
                    if (!nazivKategorije.getText().toString().equals("") && customAdapterKategorije.focusedItem >= 0 && tipTroska >= 0) {
                        Log.e("Kate", customAdapterKategorije.arrayList.get(customAdapterKategorije.focusedItem).getRawIkona());
                        KategorijaTransakcije kt = new KategorijaTransakcije();
                        kt.setNaziv(nazivKategorije.getText().toString());
                        kt.setIkonaKategorije(customAdapterKategorije.arrayList.get(customAdapterKategorije.focusedItem).getRawIkona());
                        kt.setTipTransakcije(tipTroska);
                        MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().UnosKategorijeTransakcije(kt);
                        Toast.makeText(getContext(), "Unesena nova kategorija " + kt.getNaziv(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_LONG).show();
                    }
                    onDialogCategoryResult.finish();
                }
                CategoryAddDialog.this.dismiss();
            }
            else{
                if (onDialogCategoryResult != null) {
                    if (!nazivKategorije.getText().toString().equals("") && customAdapterKategorije.focusedItem >= 0 && tipTroska >= 0) {
                        KategorijaTransakcije kt=MyDatabase.getInstance(context).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcijePremaNazivu(kategorijaTransakcije.getNaziv());
                        kt.setId(kt.getId());
                        kt.setNaziv(nazivKategorije.getText().toString());
                        kt.setIkonaKategorije(customAdapterKategorije.arrayList.get(customAdapterKategorije.focusedItem).getRawIkona());
                        kt.setTipTransakcije(tipTroska);
                        MyDatabase.getInstance(getContext()).getKategorijaTransakcijeDAO().AzurirajKategorijuTransakcije(kt);
                        Toast.makeText(getContext(), "Ažurirana kategorija " + kt.getNaziv(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Niste unijeli sve parametre!", Toast.LENGTH_LONG).show();
                    }

                    onDialogCategoryResult.finish();
                }
                CategoryAddDialog.this.dismiss();
            }

        }
    }
    public void oznaciKategoriju(String nazivKategorije){
        customAdapterKategorije.focusedItem=dohvatiIndexKategorije(nazivKategorije);
    }
    private int dohvatiIndexKategorije(String nazivKategorije){
        int returnme=0;
        for(int i=0;i<ikone.length;i++)
            if(ikone[i].equals(nazivKategorije)){
                returnme=i;
                break;
            }
            return returnme;
    }
}
