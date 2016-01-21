package app.me.azcs.homeauto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice ;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    //views
    Button btnPaired ;
    ListView devicesList ;

    //bluetooth
    private BluetoothAdapter mybluetooth = null ;
    private Set<BluetoothDevice> pairedDevices ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //match views with java
        btnPaired = (Button)findViewById(R.id.pairdbtn);
        devicesList = (ListView)findViewById(R.id.listView);

        mybluetooth = BluetoothAdapter.getDefaultAdapter();

        if (mybluetooth == null ){ // there is no adapter
            //show massage then close apk
            Toast.makeText(getApplicationContext() ,
                    "your device dosen't have blutooth adapter" , Toast.LENGTH_SHORT).show();
            finish();
        }else {
            if (mybluetooth.isEnabled())//if bluetooth enabled
            {} // do nothing
            else {
                Intent open = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(open,1);
            }
        }

        //on click button to paired devices
        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });


//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void pairedDevicesList() {
        pairedDevices = mybluetooth.getBondedDevices(); // return the set of bonded (paired) devices on the local adapter
        ArrayList arrayList = new ArrayList();
        if (pairedDevices.size() > 0 ) {
            for (BluetoothDevice bt : pairedDevices)
                arrayList.add(bt.getName() + "/n" + bt.getAddress());

            }else {
            Toast.makeText(getApplicationContext() ,
                    "there is no paired device" , Toast.LENGTH_SHORT).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this , R.layout.devlist , arrayList);
        devicesList.setAdapter(adapter);
        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String info = ((TextView)view ).getText().toString();
                String address = info.substring(info.length() - 17);
                Intent intent = new Intent(getApplicationContext() , control.class);
                intent.putExtra("address" ,address);
                startActivity(intent);
            }
        });
    }
}
