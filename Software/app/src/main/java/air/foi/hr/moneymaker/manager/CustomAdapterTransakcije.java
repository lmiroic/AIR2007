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

import java.util.List;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.KategorijaTransakcije;
import air.foi.hr.core.entiteti.TipTransakcije;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.modul.transakcije.OnDialogTransactionResult;
import air.foi.hr.core.modul.transakcije.TransactionImplementor;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.modul.transakcije.TransactionPrihodDialog;
import air.foi.hr.moneymaker.modul.transakcije.TransactionPrijenosDialog;
import air.foi.hr.moneymaker.modul.transakcije.TransactionTrosakDialog;


public class CustomAdapterTransakcije extends RecyclerView.Adapter<CustomAdapterTransakcije.viewHolder> {
    private Context context;
    public List<TransactionImplementor> arrayList;


    public CustomAdapterTransakcije(Context context, List<TransactionImplementor> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public CustomAdapterTransakcije(Context context) {
        this.context = context;
    }

    public Transakcija getTransactionAtPosition(final int position) {
        return (Transakcija) arrayList.get(position);
    }

    @NonNull
    @Override
    public CustomAdapterTransakcije.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_transakcija_itemlist, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterTransakcije.viewHolder holder, int position) {
        KategorijaTransakcije kategorijaTransakcije = MyDatabase.getInstance(context).getKategorijaTransakcijeDAO().DohvatiKategorijuTransakcije(((Transakcija) arrayList.get(position)).getKategorijaTransakcije());
        Transakcija transakcija = MyDatabase.getInstance(context).getTransakcijaDAO().DohvatiTransakciju(((Transakcija) arrayList.get(position)).getId());
        if (kategorijaTransakcije != null) {
            holder.nazivTransakcije.setText(kategorijaTransakcije.getNaziv());
            holder.sumaTransakcije.setText(String.valueOf(((Transakcija) arrayList.get(position)).getIznos()));
            holder.nazivRacuna.setText(transakcija.getOpis());
            holder.ikonaTransakcije.setImageResource(kategorijaTransakcije.getCategoryIcon(context));
            holder.cardViewTransakcije.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((Transakcija) arrayList.get(position)).getTipTransakcije() == TipTransakcije.TROSAK) {
                        TransactionTrosakDialog transactionTrosakDialog = new TransactionTrosakDialog(context, (Transakcija) arrayList.get(position));
                        transactionTrosakDialog.SetOnDialogTransactionResult(new OnDialogTransactionResult() {
                            @Override
                            public void finish() {

                            }
                        });
                        transactionTrosakDialog.show();
                    } else if (((Transakcija) arrayList.get(position)).getTipTransakcije() == TipTransakcije.PRIHOD) {
                        TransactionPrihodDialog transactionPrihodDialog = new TransactionPrihodDialog(context, (Transakcija) arrayList.get(position));
                        transactionPrihodDialog.SetOnDialogTransationResult(new OnDialogTransactionResult() {
                            @Override
                            public void finish() {

                            }
                        });
                        transactionPrihodDialog.show();
                    } else if (((Transakcija) arrayList.get(position)).getTipTransakcije() == TipTransakcije.PRIJENOS) {
                        TransactionPrijenosDialog transactionPrijenosDialog = new TransactionPrijenosDialog(context, (Transakcija) arrayList.get(position));
                        transactionPrijenosDialog.SetOnDialogTransactionResult(new OnDialogTransactionResult() {
                            @Override
                            public void finish() {

                            }
                        });
                        transactionPrijenosDialog.show();
                    }

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void removeTransactionAtPosition(final int adapterPosition) {
        arrayList.remove(adapterPosition);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nazivTransakcije;
        TextView sumaTransakcije;
        ImageView ikonaTransakcije;
        TextView nazivRacuna;
        CardView cardViewTransakcije;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            nazivTransakcije = (TextView) itemView.findViewById(R.id.nazivKategorije);
            sumaTransakcije = (TextView) itemView.findViewById(R.id.iznosTransakcije);
            ikonaTransakcije = (ImageView) itemView.findViewById(R.id.icon);
            nazivRacuna = (TextView) itemView.findViewById(R.id.txtNazivRacunaTransakcije);
            cardViewTransakcije = (CardView) itemView.findViewById(R.id.cardViewTransakcija);


        }
    }
}
