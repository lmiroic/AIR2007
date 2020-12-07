package air.foi.hr.moneymaker.manager;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.List;

import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.moneymaker.R;

public class CustomAdapterKategorije extends RecyclerView.Adapter<CustomAdapterKategorije.viewHolder> {
    private Context context;
    public List<CategoryAddModel>arrayList;
    public int focusedItem=0;


    public CustomAdapterKategorije(Context context, List<CategoryAddModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomAdapterKategorije.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.kategorije_itemlist, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterKategorije.viewHolder holder, int position) {
        holder.ikonaKategorije.setImageResource(arrayList.get(position).getIkona(context));
        holder.itemView.setBackgroundColor(focusedItem == position ? Color.LTGRAY : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView ikonaKategorije;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ikonaKategorije=(ImageView) itemView.findViewById(R.id.ikonaKat);
            ikonaKategorije.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(focusedItem);
                    focusedItem = getAdapterPosition();
                    notifyItemChanged(focusedItem);
                }
            });
        }
    }
}
