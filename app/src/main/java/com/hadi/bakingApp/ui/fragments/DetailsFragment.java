package com.hadi.bakingApp.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.hadi.bakingApp.R;
import com.hadi.bakingApp.databinding.FragmentDetailsBinding;
import com.hadi.bakingApp.models.Recipe;
import com.hadi.bakingApp.ui.activities.Ingredients;
import com.hadi.bakingApp.ui.activities.StepsActivity;

import java.util.Objects;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class DetailsFragment extends Fragment
        implements View.OnClickListener, ExoPlayer.EventListener {

    private static final String EXO_PLAYER_STATE = "playbackPosition";
    private Recipe recipe;
    private int mPosition;


    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

    private long playbackPosition;

    private FragmentDetailsBinding detailsBinding;
    private SimpleExoPlayer simpleExoPlayer;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(int position, Recipe recipe1) {

        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt("p", position);
        args.putSerializable(StepsActivity.RECIPE, recipe1);
        detailsFragment.setArguments(args);
        return detailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("p")
                && getArguments().containsKey(StepsActivity.RECIPE)) {

            mPosition = getArguments().getInt("p");
            recipe = (Recipe) getArguments().getSerializable(StepsActivity.RECIPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        detailsBinding = FragmentDetailsBinding.inflate(inflater, container, false);


        detailsBinding.fab.setOnClickListener(this);
        detailsBinding.previous.setOnClickListener(this);
        detailsBinding.next.setOnClickListener(this);

        return detailsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playbackPosition = C.TIME_UNSET;
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(EXO_PLAYER_STATE, C.TIME_UNSET);
        }

        initializePlayer();
        initializeMediaSession();
    }

    private void initializePlayer() {
        if (simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            detailsBinding.exoID.setPlayer(simpleExoPlayer);
            initializeVideo();
        }
    }

    private void initializeVideo() {
        detailsBinding.stepDescription.setText(recipe.getSteps().get(mPosition).getDescription());

        String videoUrl = recipe.getSteps().get(mPosition).getVideoURL();
        if (videoUrl.isEmpty()) {
            if (isConnected()) {
                Toast.makeText(getContext(), "No Vedio", Toast.LENGTH_LONG).show();
            }
        }
        Uri uri = Uri.parse(videoUrl);

        String userAgent = Util.getUserAgent(getContext(), "Baking");

        MediaSource mediaSource = new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(Objects.requireNonNull(getContext()), userAgent),
                new DefaultExtractorsFactory(), null, null);

        if (isConnected()) {
            if (playbackPosition != C.TIME_UNSET) {
                simpleExoPlayer.seekTo(playbackPosition);
            }

            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(Objects.requireNonNull(getContext()), "MusicService");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);

        mediaSession.setPlaybackState(stateBuilder.build());

        mediaSession.setCallback(new MySessionCallback());

        mediaSession.setActive(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.fab):

                Intent intent = new Intent(getContext(), Ingredients.class);
                intent.putExtra("recipe", recipe);
                Objects.requireNonNull(getContext()).startActivity(intent);

                break;
            case (R.id.previous):
                if (mPosition > 0) {
                    playbackPosition = C.TIME_UNSET;
                    mPosition--;
                    initializeVideo();
                }
                break;
            case (R.id.next):
                if (mPosition < recipe.getSteps().size() - 1) {
                    playbackPosition = C.TIME_UNSET;
                    mPosition++;
                    initializeVideo();
                }
                break;
        }
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            playbackPosition = simpleExoPlayer.getCurrentPosition();

            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
            mediaSession.release();
            mediaSession = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (Build.VERSION.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT > 23) {
            initializePlayer();
            initializeMediaSession();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT <= 23 || simpleExoPlayer == null) {
            initializePlayer();
            initializeMediaSession();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(EXO_PLAYER_STATE, playbackPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object o) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {

    }

    @Override
    public void onLoadingChanged(boolean b) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {

            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    simpleExoPlayer.getCurrentPosition(),
                    1f);

        } else if ((playbackState == ExoPlayer.STATE_READY)) {

            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    simpleExoPlayer.getCurrentPosition(),
                    1f);

        }

        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onPositionDiscontinuity() {

    }


    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();
            simpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onPause() {
            super.onPause();
            simpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();

            simpleExoPlayer.seekTo(0);
        }
    }

    public class MediaReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSession, intent);
        }
    }

}
