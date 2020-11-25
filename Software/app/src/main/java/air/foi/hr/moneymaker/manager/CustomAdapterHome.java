package air.foi.hr.moneymaker.manager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.moneymaker.R;

public class CustomAdapterHome extends RecyclerView.Adapter<CustomAdapterHome.viewHolder> {
    Context context;
    List<CategoryImplementor> arrayList;
    public CustomAdapterHome(Context context, List<CategoryImplementor> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
                viewHolder.icon.setImageResource(arrayList.get(position).getCategoryIcon());
            }
            catch(Exception e){
                Log.e("Kategorija greska",arrayList.get(position).getCategoryName()+" "+arrayList.get(position).getCategoryIcon());
            }
            viewHolder.iconSum.setText(String.valueOf(arrayList.get(position).getCategorySum()));
        }
        else{
            viewHolder.iconName.setText(arrayList.get(position).getCategoryName());
            viewHolder.icon.setImageResource(arrayList.get(position).getCategoryIcon());
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.get(position).executeAction();
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
            iconName = (TextView) itemView.findViewById(R.id.icon_name);
            iconSum=(TextView) itemView.findViewById(R.id.icon_sum);
        }
    }
}
