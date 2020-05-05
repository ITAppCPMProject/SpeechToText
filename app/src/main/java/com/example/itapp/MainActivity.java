package com.example.itapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final int MY_REQUEST_CODE = 7117;
    List<AuthUI.IdpConfig> providers;
    Button btn_sign_out, btn_goToRecord, btn_goToStorage, btn_goToDescp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sign_out = (Button)findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn_sign_out.setEnabled(false);
                                showSignInOptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btn_goToRecord = (Button)findViewById(R.id.btn_GoToRecord);
        btn_goToRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        btn_goToStorage = (Button)findViewById(R.id.btn_GoToStorage);
        btn_goToStorage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w){
                Intent intent = new Intent(MainActivity.this, DisplayRecords.class);
                startActivity(intent);
            }
        });

        btn_goToDescp = (Button)findViewById(R.id.btn_GoToDescp);
        btn_goToDescp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, DisplayDescription.class);
                startActivity(intent);
            }
        });


        //init provider
        /*
        providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(), // Email
            new AuthUI.IdpConfig.PhoneBuilder().build(), // Phone
            new AuthUI.IdpConfig.FacebookBuilder().build(), // Facebook
            new AuthUI.IdpConfig.GoogleBuilder().build()// Google
        );

        showSignInOptions();

         */
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.myTheme)
                .build(),MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK)
            {
                //Get user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Show email on toast
                Toast.makeText(this,""+user.getEmail(), Toast.LENGTH_SHORT).show();
                //set button signout
                btn_sign_out.setEnabled(true);
            }
            else {
                Toast.makeText(this,""+response.getError().getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
