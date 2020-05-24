package com.example.itapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayRecords extends AppCompatActivity {


    ListView listView;
    TextView textView;
    private ArrayAdapter<String> adapter2 ;

    DatabaseReference databaseSamples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_records);
        listView = (ListView)findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("View which presented all records in realtime database.");

        final ArrayList<String> samplesy = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference();
        databaseSamples= FirebaseDatabase.getInstance().getReference("Samples");

        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,samplesy);
        listView.setAdapter(adapter2);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot sampleSnapshot : dataSnapshot.getChildren()){
                    String cont = sampleSnapshot.child("content").getValue(String.class);
                    String path = sampleSnapshot.child("path").getValue(String.class);
                    samplesy.add(cont);
                    samplesy.add(path);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });

    }







}
