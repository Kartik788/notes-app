package com.ringolatechapps.ringonotes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Date;

public class MainActivity2 extends AppCompatActivity {
    TextView toolbarheading;
    EditText notesEditText;
    ImageView cancel;
    ImageView check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        toolbarheading = findViewById(R.id.toolbar_heading);
        notesEditText = findViewById(R.id.notes_edit_text);
        cancel = findViewById(R.id.cancel);
        check = findViewById(R.id.check);

        notesEditText.requestFocus();


        String file_name = getIntent().getStringExtra("file_name");
        String file_data = getIntent().getStringExtra("file_data");
        toolbarheading.setText(file_name);

        if (file_data != null) {
            notesEditText.setText(file_data);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                value_model mvalue_model = new value_model(notesEditText.getText().toString(), new Date());
                SharedPreferences sharedPreferences = getSharedPreferences("NOTES", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(mvalue_model, value_model.class);
                myEdit.putString(file_name, json);
                myEdit.commit();
                onBackPressed();


            }
        });


    }


}