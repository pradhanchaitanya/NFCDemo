package com.chaitanya.nfcdemo;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    TextView mInfoText;
    NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfoText = (TextView) findViewById(R.id.info);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC unavailable", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "NFC available", Toast.LENGTH_LONG).show();
            mNfcAdapter.setNdefPushMessageCallback(this, this);
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        NdefRecord rtdUriRecord = NdefRecord.createUri("http://google.com");

        NdefMessage outNdefMessage = new NdefMessage(rtdUriRecord);
        return outNdefMessage;
    }

    @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {
        final String event = "onNdefPushComplete\n" + nfcEvent.toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), event, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String action = intent.getAction();
        if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Parcelable[] parcelables =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecord = inNdefMessage.getRecords();
            NdefRecord ndefRecord = inNdefRecord[0];
            String inMessage = new String(ndefRecord.getPayload());
            mInfoText.setText(inMessage);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }
}
