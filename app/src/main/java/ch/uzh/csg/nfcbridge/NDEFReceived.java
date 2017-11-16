    package ch.uzh.csg.nfcbridge;

    import android.content.Intent;
    import android.net.Uri;
    import android.nfc.NdefMessage;
    import android.nfc.NdefRecord;
    import android.nfc.NfcAdapter;
    import android.nfc.Tag;
    import android.os.Parcelable;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.widget.EditText;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.net.URI;

    public class NDEFReceived extends AppCompatActivity {
        Intent NFCintent;
        Uri uri;
        String dataRepresenation;
        EditText NFCMessageDisplay;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ndefreceived);
            NFCintent = getIntent();


            /*NFCMessageDisplay = (EditText) findViewById(R.id.nfc_message_display);
            NFCMessageDisplay.setText(dataRepresenation);
            */

            if (getIntent() != null && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
                Parcelable[] rawMessages =
                        getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (rawMessages != null) {
                    NdefMessage[] messages = new NdefMessage[rawMessages.length];
                    for (int i = 0; i < rawMessages.length; i++) {
                        messages[i] = (NdefMessage) rawMessages[i];
                        System.out.println(messages[i].getRecords()[0].toMimeType());
                        System.out.println(messages[i].getRecords()[0].getPayload().length);
                    }
                }
            }
        }
    }
