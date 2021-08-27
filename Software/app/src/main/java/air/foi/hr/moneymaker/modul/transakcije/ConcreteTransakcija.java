package air.foi.hr.moneymaker.modul.transakcije;

import android.content.Context;

import air.foi.hr.core.database.MyDatabase;
import air.foi.hr.core.entiteti.TipTransakcije;
import air.foi.hr.core.entiteti.Transakcija;
import air.foi.hr.core.modul.transakcije.OnDialogTransactionResult;
import air.foi.hr.core.modul.transakcije.TransactionImplementor;

public class ConcreteTransakcija implements TransactionImplementor {

    private String nazivRacuna;
    private int ikonaTransakcije;
    private float iznosTransakcije;


    public ConcreteTransakcija() {
    }

    public ConcreteTransakcija(String nazivRacuna, int ikonaTransakcije, float iznosTransakcije) {
        this.nazivRacuna = nazivRacuna;
        this.ikonaTransakcije = ikonaTransakcije;
        this.iznosTransakcije = iznosTransakcije;
    }

    @Override
    public String getImeRacuna(Context context) {
        return  this.nazivRacuna;
    }

    @Override
    public int getIkonaTransakcije(Context context) {
        return context.getResources().getIdentifier("ic_placa","drawable",context.getPackageName());
    }

    @Override
    public float getIznosTransakcije() {
        return this.iznosTransakcije;
    }

    @Override
    public void executeAction(Context context, Transakcija transakcija) {
        if (transakcija.getTipTransakcije() == TipTransakcije.PRIHOD) {
            TransactionPrihodDialog transactionPrihodDialog=new TransactionPrihodDialog(context);
            transactionPrihodDialog.SetOnDialogTransationResult(new OnDialogTransactionResult() {
                @Override
                public void finish() {

                }
            });
            transactionPrihodDialog.show();

        } else if (transakcija.getTipTransakcije() == TipTransakcije.TROSAK) {
            TransactionTrosakDialog transactionTrosakDialog=new TransactionTrosakDialog(context);
            transactionTrosakDialog.SetOnDialogTransactionResult(new OnDialogTransactionResult() {
                @Override
                public void finish() {

                }
            });
            transactionTrosakDialog.show();

        } else if(transakcija.getTipTransakcije() == TipTransakcije.PRIJENOS) {
            TransactionPrijenosDialog transactionPrijenosDialog=new TransactionPrijenosDialog(context);
            transactionPrijenosDialog.SetOnDialogTransactionResult(new OnDialogTransactionResult() {
                @Override
                public void finish() {

                }
            });
            transactionPrijenosDialog.show();
        }
    }
}
