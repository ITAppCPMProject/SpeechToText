package com.example.itapp;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;


public class DisplayDescription extends AppCompatActivity {


    ListView listView;
    TextView textView;
    private ArrayAdapter<String> adapter2 ;

    DatabaseReference databaseSamples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_description);

        textView = (TextView) findViewById(R.id.textView2);
        textView.setText("View which presented all description in realtime database.");
        listView = (ListView)findViewById(R.id.listView);

//        String samples[]={"Jeden", "Dwa", "Trzy"};

        final ArrayList<String> samplesy = new ArrayList<>();

//        samplesy.add(samples[0]);
//        samplesy.add(samples[1]);


//        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,samplesy);
//        listView.setAdapter(adapter2);

     //   Samples sample2= new Samples("audio1","is Mesa from more pragmatic and political","gs://it-app-cmp.appspot.com/audio1.wav");


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference();
        databaseSamples= FirebaseDatabase.getInstance().getReference("Samples");

        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,samplesy);
        listView.setAdapter(adapter2);
//        String id = ref.push().getKey();
//        ref.child("Samples").setValue(sample2);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot sampleSnapshot : dataSnapshot.getChildren()){
                        String cont = sampleSnapshot.child("name").getValue(String.class);
                        samplesy.add(cont);

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });

    }







}
