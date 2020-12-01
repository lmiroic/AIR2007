package air.foi.hr.moneymaker.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.core.modul.racuni.RacuniImplementor;
import air.foi.hr.moneymaker.R;

public class CustomAdapterRacun extends RecyclerView.Adapter<CustomAdapterRacun.viewHolder> {

    Context context;
    List<RacuniImplementor> arrayList;

    public CustomAdapterRacun(Context context, List<RacuniImplementor> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView imgViewIkonaRacuna;
        TextView txtViewImeRacuna;
        TextView txtViewRacunStanje;

        public viewHolder(View itemView){
            super(itemView);
            imgViewIkonaRacuna=(ImageView) itemView.findViewById(R.id.imgViewIkonaRacuna);
            txtViewImeRacuna=(TextView) itemView.findViewById(R.id.txtViewImeRacuna);
            txtViewRacunStanje=(TextView) itemView.findViewById(R.id.txtViewRacunStanje);

        }

    }
}

