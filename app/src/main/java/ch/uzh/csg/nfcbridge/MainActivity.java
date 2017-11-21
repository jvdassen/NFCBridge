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
                        String first = params.get(0); // "status"
                        System.out.println("first parameter" + first);
                        textAddress.setText(first);
                    }
                }
        );

    }
    public void goToBazoPaymentPage(String URLencodedTransaction) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLencodedTransaction));
        startActivity(browserIntent);
    }
}
