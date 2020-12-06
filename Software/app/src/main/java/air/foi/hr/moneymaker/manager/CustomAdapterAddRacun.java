package air.foi.hr.moneymaker.manager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import air.foi.hr.moneymaker.R;

public class CustomAdapterAddRacun extends RecyclerView.Adapter<CustomAdapterAddRacun.viewHolder> {
    private Context context;
    public List<RacunAddModel> arrayList;
    public int focusedItemRacun=0;

    public CustomAdapterAddRacun(Context context, List<RacunAddModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_racun_add_itemlist,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.ikonaRacuna.setImageResource(arrayList.get(position).getIkonaRacuna(context));
        holder.itemView.setBackgroundColor(focusedItemRacun==position? Color.LTGRAY:Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView ikonaRacuna;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ikonaRacuna=(ImageView) itemView.findViewById(R.id.imgViewCardIkonaRacuna);
            ikonaRacuna.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(focusedItemRacun);
                    focusedItemRacun=getAdapterPosition();
                    notifyItemChanged(focusedItemRacun);
                }
            });
        }
    }
}
