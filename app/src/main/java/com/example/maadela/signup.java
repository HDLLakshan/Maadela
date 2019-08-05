package com.example.maadela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class signup extends AppCompatActivity {

    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
       btn = (Button)findViewById( R.id.button );
    }

    public void sendMessage1(View view) {
        Intent intent = new Intent(this, MapLoc.class);
        // EditText editText = (EditText) findViewById(R.id.editText);
        // String message = editText.getText().toString();
        //   intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
