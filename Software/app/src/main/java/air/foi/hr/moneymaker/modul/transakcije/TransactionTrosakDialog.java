package air.foi.hr.moneymaker.modul.transakcije;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import air.foi.hr.core.modul.transakcije.OnDialogTransactionResult;
import air.foi.hr.moneymaker.R;

public class TransactionTrosakDialog extends Dialog implements android.view.View.OnClickListener{

    private OnDialogTransactionResult onDialogTransactionResult;

    public TransactionTrosakDialog(@NonNull Context context) {
        super(context);
    }

    public void SetOnDialogTransactionResult(OnDialogTransactionResult onDialogTransactionResult){
        this.onDialogTransactionResult=onDialogTransactionResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_trosak);
    }

    @Override
    public void onClick(View v) {

    }
    private class OkListener implements android.view.View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }
}