package com.example.itapp;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;


public class AudioListFragment extends Fragment implements AudioListAdapter.onItemListClick{

    private ConstraintLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    private RecyclerView audioList;

    private File[] allFiles;

    private AudioListAdapter audioListAdapter;

    private MediaPlayer mediaPlayer = null;
    private  boolean isPlaying = false;

    private File fileToPlay;

    //UI Elements
    private ImageButton playBtn;
    private TextView playerHeader;
    private TextView playerFilename;

    private SeekBar playerSeekBar;
    private Handler seekbarHandler;
    private Runnable updateSeekBar;

    public AudioListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerSheet = view.findViewById(R.id.player_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);
        audioList = view.findViewById(R.id.audio_list_view);

        playBtn = view.findViewById(R.id.player_play_btn);
        playerHeader = view.findViewById(R.id.player_header_title);
        playerFilename = view.findViewById(R.id.player_filename);


        playerSeekBar = view.findViewById(R.id.player_seekBar);

        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        allFiles = directory.listFiles();

        audioListAdapter = new AudioListAdapter(allFiles, this);

        audioList.setHasFixedSize(true);
        audioList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioList.setAdapter(audioListAdapter);



        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        playBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    pauseAudio();
                } else {
                    if (fileToPlay != null){
                        resumeAudio();
                    }

                }
            }
        });

        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (fileToPlay != null){
                int progress = seekBar.getProgress();
                mediaPlayer.seekTo(progress);
                resumeAudio();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClickListener(File file, int position) {
        fileToPlay = file;
        if (isPlaying){
            stopAudio();
            playAudio(fileToPlay);
        }else {
            playAudio(fileToPlay);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void pauseAudio(){
        mediaPlayer.pause();
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.record_btn_recording, null));
        isPlaying = false;
        seekbarHandler.removeCallbacks(updateSeekBar);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void resumeAudio(){
        mediaPlayer.start();
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.record_btn_stopped, null));
        isPlaying = true;
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekBar, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopAudio() {
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.record_btn_recording, null));
        playerHeader.setText("Stopped");
        isPlaying = false;
        mediaPlayer.stop();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void playAudio(File fileToPlay) {

        mediaPlayer = new MediaPlayer();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.record_btn_stopped, null));
        playerFilename.setText(fileToPlay.getName());
        playerHeader.setText("Playing");

        isPlaying = true;

        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                stopAudio();
                playerHeader.setText("finished");
            }
        });

        playerSeekBar.setMax(mediaPlayer.getDuration());

        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekBar, 0);

    }

    private void updateRunnable() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                playerSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this,500);
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStop() {
        super.onStop();
        if (isPlaying) {
            stopAudio();
        }
    }
}
