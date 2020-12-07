package air.foi.hr.moneymaker.manager;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.moneymaker.R;


public class CustomAdapterTransakcije extends RecyclerView.Adapter<CustomAdapterTransakcije.viewHolder> {
    private Context context;
    public List<Transakcija>arrayList;

    public CustomAdapterTransakcije(Context context, List<Transakcija> arrayList) {
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

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        public viewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public void PostaviTransakcije(List<Transakcija>transakcijaList){

    }

}
