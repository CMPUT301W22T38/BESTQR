package com.example.bestqr.ui.leaderboard;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bestqr.databinding.FragmentLeaderboardMainBinding;

public class LeaderboardFragmentQR extends Fragment {

    private LeaderboardViewModelMain leaderboardViewModelMain;
    private FragmentLeaderboardMainBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leaderboardViewModelMain =
                new ViewModelProvider(this).get(LeaderboardViewModelMain.class);

        binding = FragmentLeaderboardMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Hide toolbar
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    // Handle hiding the toolbar for situations below, as we use a custom top bar for the leaderboard:
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onDestroyView() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        super.onDestroyView();
        binding = null;
    }
}