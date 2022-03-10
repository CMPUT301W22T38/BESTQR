package com.example.bestqr.ui.leaderboard;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.bestqr.databinding.FragmentLeaderboardMainBinding;
import com.example.bestqr.databinding.FragmentLeaderboardQrBinding;

public class LeaderboardFragmentQR extends Fragment {

    private LeaderboardViewModelQR leaderboardViewModelMain;
    private FragmentLeaderboardQrBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leaderboardViewModelMain =
                new ViewModelProvider(this).get(LeaderboardViewModelQR.class);

        binding = FragmentLeaderboardQrBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        NavController navController = NavHostFragment.findNavController(this);
        NavigationUI.setupWithNavController(binding.toolbarLeaderboardQr, navController);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}