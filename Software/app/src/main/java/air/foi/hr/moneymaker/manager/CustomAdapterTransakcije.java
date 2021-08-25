package air.foi.hr.moneymaker.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.TipTransakcije;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.modul.transakcije.TransactionImplementor;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.modul.transakcije.ConcreteTransakcija;


public class CustomAdapterTransakcije extends RecyclerView.Adapter<CustomAdapterTransakcije.viewHolder> {
    private Context context;
    public List<TransactionImplementor>arrayList;


    public CustomAdapterTransakcije(Context context, List<TransactionImplementor> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public CustomAdapterTransakcije(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public CustomAdapterTransakcije.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.fragment_transakcija_itemlist,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterTransakcije.viewHolder holder, int position) {
        KategorijaTransakcije kategorijaTransakcije= MyDatabase.getInstance(context).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcije(((Transakcija) arrayList.get(position)).getKategorijaTransakcije());
        if(kategorijaTransakcije!=null){
            holder.nazivTransakcije.setText(kategorijaTransakcije.getNaziv());
            holder.sumaTransakcije.setText(String.valueOf(((Transakcija) arrayList.get(position)).getIznos()));
            holder.nazivRacuna.setText(arrayList.get(position).getImeRacuna(context));
            holder.ikonaTransakcije.setImageResource(kategorijaTransakcije.getCategoryIcon(context));
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nazivTransakcije;
        TextView sumaTransakcije;
        ImageView ikonaTransakcije;
        TextView nazivRacuna;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            nazivTransakcije=(TextView) itemView.findViewById(R.id.nazivKategorije);
            sumaTransakcije=(TextView) itemView.findViewById(R.id.iznosTransakcije);
            ikonaTransakcije=(ImageView) itemView.findViewById(R.id.icon);
            nazivRacuna=(TextView) itemView.findViewById(R.id.nazivRacuna);


        }
    }
}
