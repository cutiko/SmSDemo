package cl.cutiko.smsdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private TextInputLayout phoneHolder, msgHolder;
    private TextInputEditText phone, smsMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        phoneHolder = (TextInputLayout) findViewById(R.id.phoneHolder);
        msgHolder = (TextInputLayout) findViewById(R.id.msgHolder);

        phone = (TextInputEditText) findViewById(R.id.phoneEt);
        smsMsg = (TextInputEditText) findViewById(R.id.smsMsg);

        Button directBtn = (Button) findViewById(R.id.directBtn);
        Button indirectBtn = (Button) findViewById(R.id.indirectBtn);
        setBtns(directBtn, true);
        setBtns(indirectBtn, false);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                Log.d("SIGNAL", String.valueOf(signalStrength.getGsmSignalStrength()));
            }
        };

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);


    }

    private void setBtns(Button btn, final boolean isDirect){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    String number = "+569" + phone.getText().toString().replace(" ", "");
                    String msg = smsMsg.getText().toString();
                    if (isDirect) {
                        directSend(number, msg);
                    } else {
                        indirectSend(number, msg);
                    }
                }
            }
        });
    }

    private boolean validation(){
        if (phone.getText().toString().trim().length() > 0) {
            if (smsMsg.getText().toString().trim().length() > 0) {
                return true;
            } else {
                msgHolder.setError(getString(R.string.required_field));
                return false;
            }
        } else {
            phoneHolder.setError(getString(R.string.required_field));
            return false;
        }
    }

    private void directSend(String number, String msg){
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(number, null, msg, null, null);
    }

    private void indirectSend(String number, String msg){
        Uri sms_uri = Uri.parse("smsto:" + number);
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        sms_intent.putExtra("sms_body", msg);
        startActivity(sms_intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutCutiko:
                String url = "http://www.cutiko.cl";
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(url));
                startActivity(webIntent);
                return true;
            default:
                return false;
        }
    }
}
