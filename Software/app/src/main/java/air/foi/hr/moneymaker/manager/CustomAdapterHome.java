package air.foi.hr.moneymaker.manager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.core.modul.kategorije.OnDialogCategoryResult;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.modul.kategorije.CategoryAddDialog;
import air.foi.hr.moneymaker.modul.kategorije.ConcreteCategory;

public class CustomAdapterHome extends RecyclerView.Adapter<CustomAdapterHome.viewHolder> {
    Context context;
    List<CategoryImplementor> arrayList;
    public CustomAdapterHome(Context context, List<CategoryImplementor> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public CustomAdapterHome(Context context) {
        this.context = context;
    }
    public void SetKategorije(List<KategorijaTransakcije>kategorijaTransakcijes){
        List<CategoryImplementor>sveKategorije=new ArrayList<>();
        for(KategorijaTransakcije kt: kategorijaTransakcijes){
            sveKategorije.add(kt);
        }
        ConcreteCategory dodajTransakciju=new ConcreteCategory("Add","ic_add");
        sveKategorije.add(dodajTransakciju);
        arrayList=sveKategorije;
    }

    @Override
    public CustomAdapterHome.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_home_itemlist, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomAdapterHome.viewHolder viewHolder, final int position) {
        if(arrayList.get(position) instanceof KategorijaTransakcije){
            viewHolder.iconName.setText(arrayList.get(position).getCategoryName());
            try {
                viewHolder.icon.setImageResource(arrayList.get(position).getCategoryIcon(context));
            }
            catch(Exception e){
                Log.e("Kategorija greska",arrayList.get(position).getCategoryName()+" "+arrayList.get(position).getCategoryIcon(context));
            }
            viewHolder.iconSum.setText(String.valueOf(arrayList.get(position).getCategorySum()));
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CategoryAddDialog categoryAddDialog=new CategoryAddDialog(context,((KategorijaTransakcije) arrayList.get(position)));
                    categoryAddDialog.setOnDialogCategoryResult(new OnDialogCategoryResult() {
                        @Override
                        public void finish() {

                        }
                    });
                    categoryAddDialog.show();
                }
            });
        }
        else{
            viewHolder.iconName.setText(arrayList.get(position).getCategoryName());
            viewHolder.icon.setImageResource(arrayList.get(position).getCategoryIcon(context));
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.get(position).executeAction(context);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView iconName;
        TextView iconSum;
        public viewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            iconName = (TextView) itemView.findViewById(R.id.nazivKategorije);
            iconSum=(TextView) itemView.findViewById(R.id.iznosTransakcije);
        }
    }
}
