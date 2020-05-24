package com.example.itapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RecordFragment extends Fragment implements View.OnClickListener {

    private int PERMISSION_CODE = 21;
    private NavController navController;

    private ImageButton listBtn;
    private ImageButton recordBtn;
    private TextView filenameText;

    private  boolean isRecording = false;

    private MediaRecorder mediaRecorder;
    private String recordFile;

    private Chronometer timer;


    private String recordPermission = Manifest.permission.RECORD_AUDIO;

    private StorageReference mStorage;
    private  String mFileName;


    public RecordFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mStorage = FirebaseStorage.getInstance().getReference();
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        listBtn = view.findViewById(R.id.record_list_btn);
        recordBtn = view.findViewById(R.id.record_btn);
        timer = view.findViewById(R.id.record_timer);
        filenameText = view.findViewById(R.id.record_filename);

        listBtn.setOnClickListener(this);
        recordBtn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_list_btn:
                if (isRecording){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                            isRecording = false;
                        }
                    });
                    alertDialog.setNegativeButton("CANCEL", null);
                    alertDialog.setTitle("Audio Still Recording");
                    alertDialog.setMessage("Are you sure, you want to stop recording?");
                    alertDialog.create().show();
                } else {
                    navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                }
                break;

            case R.id.record_btn:
                if (isRecording){
                    //Stop recording
                    stopRecording();

                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    isRecording = false;
                }else  {
                    //Start Recording
                    if (checkPermissions()){
                        startRecording();
                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording, null));
                    isRecording = true;
                    }
                }
                break;
        }
    }

    private void startRecording() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now = new Date();

<<<<<<< HEAD
        recordFile = "Recording_" + formatter.format(now) + ".mp4";
=======
        recordFile = "Recording_" + formatter.format(now) + ".wav";
>>>>>>> bc71cbd0426584ae0ce121659da5625941d04bda

        filenameText.setText("Recording, File Name: " + recordFile);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mFileName=(recordPath + "/" + recordFile);


        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    private void stopRecording() {
        timer.stop();

        filenameText.setText("Recording Stopped, File Saved: " + recordFile);
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        
        uploadAudio();


    }

    private void uploadAudio() {
<<<<<<< HEAD
        StorageReference filepath = mStorage.child("new_audio_3.mp4");
=======
        StorageReference filepath = mStorage.child("new_audio.wav");
>>>>>>> bc71cbd0426584ae0ce121659da5625941d04bda

        Uri uri=Uri.fromFile(new File(mFileName));

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });


    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), recordPermission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (isRecording){
            stopRecording();
        }

    }
}
