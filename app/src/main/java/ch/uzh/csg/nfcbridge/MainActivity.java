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

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button submitBtn, pushBtn;
    EditText textAmount;
    EditText textAddress;
    EditText textPOSId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textAmount = (EditText) findViewById(R.id.transaction_amount);
        textAddress = (EditText) findViewById(R.id.transaction_address);
        textPOSId = (EditText) findViewById(R.id.posid);

        submitBtn = (Button) findViewById(R.id.submit_btn);
        pushBtn = (Button) findViewById(R.id.push_btn);

        Uri data = getIntent().getData();
        List<String> params = data.getPathSegments();
        if (params.size() > 0) {
            parseIntentParameters(params);
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
                        Uri data = getIntent().getData();
                        List<String> params = data.getPathSegments();
                        if (params.size() > 0) {
                            parseIntentParameters(params);
                        }

                    }
                }
        );

    }
    public void goToBazoPaymentPage(String URLencodedTransaction) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLencodedTransaction));
        startActivity(browserIntent);
    }
    public void parseIntentParameters(List<String> parameters) {
        System.out.println(parameters.toString());
        String bazoAddress = parameters.get(0);
        String amount = parameters.get(1);
        String posid = parameters.get(2); // "status"


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
