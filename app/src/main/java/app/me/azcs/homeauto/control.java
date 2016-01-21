package app.me.azcs.homeauto;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class control extends AppCompatActivity {
    Button btnLamp, btnFan, btnDis, btnOut;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myuuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Intent newint = getIntent();
        address = newint.getStringExtra("address"); //receive the address of the bluetooth device


        //view
        btnLamp=(Button)findViewById(R.id.lampbtn);
        btnFan=(Button)findViewById(R.id.fanbtn);
        btnOut=(Button)findViewById(R.id.outbtn);
        btnDis=(Button)findViewById(R.id.disconnection);


        new BtConnected().execute(); //Call the class to connect

        //buttons function
        btnLamp.setOnClickListener(new View.OnClickListener() { //Lamp control
            @Override
            public void onClick(View v) {
                if(btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("2".getBytes());
                    } catch (IOException e) {
                        msg("ERROR");
                    }
                }
            }
        });

        btnFan.setOnClickListener(new View.OnClickListener() { //Fan control
            @Override
            public void onClick(View v) {
                if(btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("1".getBytes());
                    } catch (IOException e) {
                        msg("ERROR");
                    }
                }
            }
        });

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("3".getBytes());
                    } catch (IOException e) {
                        msg("ERROR");
                    }
                }
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btSocket != null){
                    try {
                        btSocket.close();
                        finish();
                    } catch (IOException e) {
                        msg("ERROR");
                    }
                }
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//
//            }
//        });
    }

    void msg (String s ){
        Toast.makeText(getApplicationContext() , s , Toast.LENGTH_SHORT).show();
    }

    private class BtConnected extends AsyncTask<Void, Void, Void>//UI Thread
    {
        private boolean connectedSecces = true ;

        @Override
        protected void onPreExecute (){
            progress = ProgressDialog.show(control.this , "Connecting...", "Please wait!!!");
        }



        @Override
        protected Void doInBackground(Void... devices) {
            try{
                if (btSocket == null || !isBtConnected){
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myuuid);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                connectedSecces = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void reslut){
            super.onPostExecute(reslut);
            if(!connectedSecces){
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

}