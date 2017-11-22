package ch.uzh.csg.nfcbridge;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
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
                                textAmount.getText().toString(),
                                textAddress.getText().toString(),
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
                                textAmount.getText().toString(),
                                textAddress.getText().toString(),
                                textPOSId.getText().toString()
                        );
                        NdefRecord urlRecord = new NdefRecord(encodedTransaction.getBytes());
                        
                        toBeSentDisplay.setText(encodedTransaction);
                    }
                }
        );

    }
    public void goToBazoPaymentPage(String URLencodedTransaction) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLencodedTransaction));
        startActivity(browserIntent);
    }
    public void parseIntentParameters(List<String> parameters) {
        String bazoAddress, amount, posid;

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
