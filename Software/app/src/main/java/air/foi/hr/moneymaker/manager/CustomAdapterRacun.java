package air.foi.hr.moneymaker.manager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.Racun;
import air.foi.hr.core.modul.kategorije.CategoryImplementor;
import air.foi.hr.core.modul.kategorije.OnDialogCategoryResult;
import air.foi.hr.core.modul.racuni.OnDialogRacunResult;
import air.foi.hr.core.modul.racuni.RacuniImplementor;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.ViewModel.RacunViewModel;
import air.foi.hr.moneymaker.modul.kategorije.CategoryAddDialog;
import air.foi.hr.moneymaker.modul.racun.RacunAddDialog;

public class CustomAdapterRacun extends RecyclerView.Adapter<CustomAdapterRacun.viewHolder> {

    Context context;
    public List<RacuniImplementor> arrayList;

    public CustomAdapterRacun(Context context, List<RacuniImplementor> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public CustomAdapterRacun(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_racun_itemlist, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomAdapterRacun.viewHolder viewHolder, final int position) {
        if(arrayList.get(position) instanceof Racun){
            viewHolder.txtViewImeRacuna.setText(arrayList.get(position).getImeRacuna());
            try {
                viewHolder.imgViewIkonaRacuna.setImageResource(arrayList.get(position).getIkonaRacuna(context));
            }
            catch(Exception e){
                Log.e("Racun",arrayList.get(position).getImeRacuna()+" "+arrayList.get(position).getIkonaRacuna(context));
            }
            viewHolder.txtViewRacunStanje.setText(String.valueOf(arrayList.get(position).getStanjeRacuna())+ " "+String.valueOf(((Racun) arrayList.get(position)).getValuta()));
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RacunAddDialog racunAddDialog=new RacunAddDialog(context,((Racun) arrayList.get(position)));
                    racunAddDialog.setOnDialogRacunResult(new OnDialogRacunResult() {
                        @Override
                        public void finish() {

                        }
                    });
                    racunAddDialog.show();
                }
            });
        }
        else{
            viewHolder.txtViewImeRacuna.setText(arrayList.get(position).getImeRacuna());
            viewHolder.imgViewIkonaRacuna.setImageResource(arrayList.get(position).getIkonaRacuna(context));
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
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


    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView imgViewIkonaRacuna;
        TextView txtViewImeRacuna;
        TextView txtViewRacunStanje;
        CardView cardView;

        public viewHolder(View itemView){
            super(itemView);
            imgViewIkonaRacuna=(ImageView) itemView.findViewById(R.id.imgViewIkonaRacuna);
            txtViewImeRacuna=(TextView) itemView.findViewById(R.id.txtViewImeRacuna);
            txtViewRacunStanje=(TextView) itemView.findViewById(R.id.txtViewRacunStanje);
            cardView=(CardView) itemView.findViewById(R.id.cardViewRacuni);
        }
    }
}

