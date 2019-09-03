package com.example.maadela;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class AdvanceOrder extends AppCompatActivity {

    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_order);

        addListenerOnSpinnerItemSelection();
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    public void onStart() {

        super.onStart();
        EditText txtDate = findViewById(R.id.editText3);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus){
                    DateDialog dialog;
                    dialog = new DateDialog(v);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    dialog.show(fragmentTransaction,"DatePicker");
                }
            }
        });

    }


}
