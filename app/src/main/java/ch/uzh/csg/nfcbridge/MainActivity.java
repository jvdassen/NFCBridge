package ch.uzh.csg.nfcbridge;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button submitBtn;
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

        submitBtn.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        String encodedTransaction = encodeAsBazoTransactionURI(
                                textAmount.getText().toString(),
                                textAddress.getText().toString(),
                                textPOSId.getText().toString()
                                );
                        goToBazoPaymentPage(encodedTransaction);

                    }
                }
        );

    }
    public void goToBazoPaymentPage(String URLencodedTransaction) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLencodedTransaction));
        startActivity(browserIntent);
    }
    public String encodeAsBazoTransactionURI(String amount, String bazoaddress, String posid) {
        String baseString = "https://bazopay2.surge.sh/#/auth/user/send?";
        String result = baseString;

        if (posid.length() > 0) {
            result += "posid=" + posid + "&";
        }

        result += "paymentinfo=" + "bazo:" + bazoaddress + "?amount=" + amount;
        System.out.println("trying to access: " + result);
        System.out.println("encoded: " + Uri.encode(result));
        return result;
    }

}
