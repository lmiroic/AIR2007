package air.foi.hr.moneymaker.fragmenti;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import air.foi.hr.core.manager.FragmentName;
import air.foi.hr.core.modul.transakcije.ISinkronizacijaRacuna;
import air.foi.hr.moneymaker.R;
import air.foi.hr.moneymaker.manager.FragmentSwitcher;
import air.foi.hr.moneymaker.session.Sesija;

public class SinkronizacijaFragment extends Fragment {

    private View view;
    private ImageView qrCodeImage;
    private Button btnQrCodeSync;
    private Button btnEmailSync;
    private Button btnNatragSync;
    private ISinkronizacijaRacuna sinkronizacijaRacuna;


    public SinkronizacijaFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sinkronizacija, container, false);
        this.InicijalizacijaVarijabli();
        this.emailSync();
        this.qrCodeSync();
        this.qrCodeSetup();
        this.btnNatragSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitcher.ShowFragment(FragmentName.POSTAVKE, getFragmentManager());
            }
        });
        return view;
    }

    private void qrCodeSetup() {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(Sesija.getInstance().getKorisnik().getEmail(), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCodeImage.setImageBitmap(bmp);
        } catch (Exception ex) {
            Log.e("Exception", "Error during qr code generation!");
        }
    }

    private void qrCodeSync() {
        btnQrCodeSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sinkronizacijaRacuna = new BarkodFragment();
                switchFragments(sinkronizacijaRacuna.getFragment());
            }
        });
    }

    private void emailSync() {
        btnEmailSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sinkronizacijaRacuna = new EmailSinkronizacijaFragment();
                switchFragments(sinkronizacijaRacuna.getFragment());
            }
        });
    }

    private void InicijalizacijaVarijabli() {
        qrCodeImage = view.findViewById(R.id.ivQrCode);
        btnQrCodeSync = view.findViewById(R.id.btnQrCodeSync);
        btnEmailSync = view.findViewById(R.id.btnEmailSync);
        btnNatragSync = view.findViewById(R.id.btnSinkronizacija);
    }

    private void switchFragments(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.fmMain, fragment).commit();
    }

}