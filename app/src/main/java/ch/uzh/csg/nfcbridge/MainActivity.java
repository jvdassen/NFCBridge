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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textAmount = (EditText) findViewById(R.id.transaction_amount);
        submitBtn = (Button) findViewById(R.id.submit_btn);

        submitBtn.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        goToBazoPaymentPage(textAmount.getText().toString());
                    }
                }
        );

    }
    public void goToBazoPaymentPage(String amount) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bazopay2.surge.sh/#/auth/user/send/?paymentinfo=bazo%3A0003332221144%3Famount%3D" + amount));
        startActivity(browserIntent);
    }

}
