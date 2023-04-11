package com.example.cs480_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import java.util.AbstractCollection;

public class MainActivity extends AppCompatActivity {

    private Button addExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get references to button (test)
        // get reference to the button
        addExpense = (Button) findViewById(R.id.addExpense);

        // set up click listener for the button
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddingExpenses.class);
                startActivity(intent);
            }
        });
    }
}
