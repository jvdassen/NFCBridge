package ch.uzh.csg.nfcbridge;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textAmount = (EditText) findViewById(R.id.transaction_amount);
        textAddress = (EditText) findViewById(R.id.transaction_address);
        textPOSId = (EditText) findViewById(R.id.posid);
        toBeSentDisplay = (TextView) findViewById(R.id.message_to_send);

        submitBtn = (Button) findViewById(R.id.submit_btn);
        pushBtn = (Button) findViewById(R.id.push_btn);
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

            List<String> params = data.getPathSegments();
            if (params.size() > 0) {
                parseIntentParameters(params);
            }
        } catch (Exception e){
            System.out.println(e.toString());
        }


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
        pushBtn.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        String encodedTransaction = BazoURIScheme.encodeAsBazoTransactionURI(
                                textAddress.getText().toString(),
                                textAmount.getText().toString(),
                                textPOSId.getText().toString()
                        );
                        //mNFCAdapter.setNdefPushMessage(createNdefMessage());

                        toBeSentDisplay.setText(encodedTransaction);
                    }
                }
        );

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String encodedTransaction = BazoURIScheme.encodeAsBazoTransactionURI(
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
        System.out.println(msg.toString());
        return msg;
    }
    @Override
    public void onNdefPushComplete(NfcEvent event) {
        Toast.makeText(this, "NFC message beamed successfully", Toast.LENGTH_LONG).show();

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
            amount = "-";
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
        // pushBtn.callOnClick();
    }
}
