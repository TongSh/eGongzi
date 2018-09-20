package tong.lan.com.hyperledger.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tong.lan.com.hyperledger.R;

public class SpalshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        Intent intent = new Intent(SpalshActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
