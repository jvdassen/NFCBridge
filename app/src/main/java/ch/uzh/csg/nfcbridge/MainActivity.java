package ch.uzh.csg.nfcbridge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback{
    Button submitBtn, pushBtn;
    EditText textAmount;
    EditText textAddress;
    EditText textPOSId;
    TextView toBeSentDisplay;
    TextWatcher genericTextChangedWatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textAmount = findViewById(R.id.transaction_amount);
        textAddress = findViewById(R.id.transaction_address);
        textPOSId = findViewById(R.id.posid);

        submitBtn = (Button) findViewById(R.id.submit_btn);
        final NfcAdapter mNFCAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNFCAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        mNFCAdapter.setNdefPushMessageCallback(this, this);
        mNFCAdapter.setOnNdefPushCompleteCallback(this, this);

        try {
            Uri data = getIntent().getData();
            if (data != null){
                List<String> params = data.getPathSegments();
                if (params.size() > 0) {
                    parseIntentParameters(params);
                    storeTransactionData();
                }
            }
        } catch (Exception e){
            System.out.println(e.toString());
        }
        parseStoredTransactionData();


        submitBtn.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        String encodedTransaction = BazoURIScheme.encodeAsBazoTransactionURI(
                                textAddress.getText().toString(),
                                textAmount.getText().toString(),
                                textPOSId.getText().toString()
                                );
                        goToBazoPaymentPage(encodedTransaction);

                    }
                }
        );

        genericTextChangedWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                storeTransactionData();
            }
        };

        textAddress.addTextChangedListener(genericTextChangedWatcher);
        textAmount.addTextChangedListener(genericTextChangedWatcher);
        textPOSId.addTextChangedListener(genericTextChangedWatcher);


    }

    @SuppressLint("ResourceType")
    private void storeTransactionData() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String bazoaddress = textAddress.getText().toString();
        String amount = textAmount.getText().toString();
        String posid = textPOSId.getText().toString();

        editor.putString(getString(R.string.storage_address), bazoaddress);
        editor.putString(getString(R.string.storage_amount), amount);
        editor.putString(getString(R.string.storage_posid), posid);

        editor.commit();
    }
    private void parseStoredTransactionData() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        String bazoaddress = sharedPref.getString(getString(R.string.storage_address), "000000000");
        String amount = sharedPref.getString(getString(R.string.storage_amount), "0");
        String posid = sharedPref.getString(getString(R.string.storage_posid), "0");

        textAddress.setText(bazoaddress);
        textAmount.setText(amount);
        textPOSId.setText(posid);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        final String encodedTransaction = BazoURIScheme.encodeAsBazoTransactionURI(
                textAddress.getText().toString(),
                textAmount.getText().toString(),
                textPOSId.getText().toString()
        );
        NdefRecord urlRecord = new NdefRecord(
                NdefRecord.TNF_ABSOLUTE_URI,
                encodedTransaction.getBytes(Charset.defaultCharset()),
                new byte[0], new byte[0]
        );

        NdefMessage msg = new NdefMessage(urlRecord);
        return msg;
    }
    @Override
    public void onNdefPushComplete(NfcEvent event) {
        final Activity thisactivity = this;
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(thisactivity, "Transaction data beamed successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            //processIntent(getIntent());
        }
    }
    public void goToBazoPaymentPage(String URLencodedTransaction) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLencodedTransaction));
        startActivity(browserIntent);
    }
    public void parseIntentParameters(List<String> parameters) {
        String bazoAddress, amount, posid;
        Toast.makeText(this, "We have prefilled " + parameters.size() + " parameters for you.", Toast.LENGTH_LONG).show();

        try {
            bazoAddress = parameters.get(0);
        } catch (Exception e){
            bazoAddress = "";
        }

        try {
            amount = parameters.get(1);
        } catch (Exception e){
            amount = "0";
        }

        try {
            posid = parameters.get(2);
        } catch (Exception e){
            posid = "-";
        }

        if(bazoAddress.length() > 0){
            textAddress.setText(bazoAddress);
        }
        if(amount.length() > 0) {
            textAmount.setText(amount);
        }
        if(posid.length() > 0) {
            textPOSId.setText(posid);
        }
    }
}
