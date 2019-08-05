package com.example.maadela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, SearchNavi.class);
        // EditText editText = (EditText) findViewById(R.id.editText);
        // String message = editText.getText().toString();
        //   intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
