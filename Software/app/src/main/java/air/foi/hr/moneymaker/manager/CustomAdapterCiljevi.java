package air.foi.hr.moneymaker.manager;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.Ciljevi;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.moneymaker.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CustomAdapterCiljevi extends RecyclerView.Adapter<CustomAdapterCiljevi.viewHolder> {
    private Context context;
    public List<Ciljevi> arrayList;

    SimpleDateFormat formaterDate = new SimpleDateFormat("yyyy-MM-dd");

    public CustomAdapterCiljevi(Context context, List<Ciljevi> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public CustomAdapterCiljevi(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CustomAdapterCiljevi.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_ciljevi_itemlist, parent, false);
        return new viewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterCiljevi.viewHolder holder, int position) {
        List<Transakcija> sveTransakcijeCilja = MyDatabase.getInstance(context).getCiljeviDAO().DohvatiTransakcijeCilja(arrayList.get(position).getKategorija(), arrayList.get(position).getDatum());
        int progress = 0;
        for (Transakcija t : sveTransakcijeCilja) {
            Date datumCilja = null;
            Date datumTransakcije = null;
            try {
                datumCilja = formaterDate.parse(arrayList.get(position).getDatum());
                datumTransakcije = formaterDate.parse(t.getDatum());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (arrayList.get(position).getKategorija() == t.getKategorijaTransakcije() && datumCilja.before(datumTransakcije)) {
                progress += t.getIznos();
            }
        }
        holder.nazivCilja.setText(arrayList.get(position).getNaziv());
        holder.iznos.setMax((int) arrayList.get(position).getIznos());
        holder.iznos.setProgress(progress);

    }

    public Ciljevi getCiljAtPosition(final int position) {
        return arrayList.get(position);
    }

    public void removeCiljAtPosition(final int adapterPosition) {
        arrayList.remove(adapterPosition);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nazivCilja;
        ProgressBar iznos;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            nazivCilja = (TextView) itemView.findViewById(R.id.txtNazivCilja);
            iznos = itemView.findViewById(R.id.pBarIznosCilja);

        }
    }
}
