package air.foi.hr.moneymaker.fragmenti;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;
import java.util.stream.Collectors;

import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import info.androidhive.barcode.BarcodeReader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarkodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarkodFragment extends Fragment implements BarcodeReader.BarcodeReaderListener {

    private View view;
    private Barcode scannedBarcode;
    private BarcodeReader barcodeReader;

    public BarkodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_barkod, container, false);
        barcodeReader = (BarcodeReader) getChildFragmentManager().findFragmentById(R.id.barcode_fragment);
        barcodeReader.setListener(this);
        return view;
    }

    @Override
    public void onScanned(Barcode barcode) {
        scannedBarcode = barcode;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(getContext())
                        .setTitle("Sinkronizacija računa")
                        .setMessage("Želite li sinkronizirati račune s navedenim računom " + scannedBarcode.displayValue+"?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /* SINRKONIZACIJA RAČUNA */
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FragmentSwitcher.ShowFragment(FragmentName.HOME, getFragmentManager());
                            }
                        }).show();
            }
        });


    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Log.e("Barcode", "onScannedMultiple: " + barcodes.size());

        final String codes = barcodes.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Barcodes: " + codes, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Log.e("Barcode", "onScanError: " + errorMessage);
    }

    @Override
    public void onCameraPermissionDenied() {

    }
}