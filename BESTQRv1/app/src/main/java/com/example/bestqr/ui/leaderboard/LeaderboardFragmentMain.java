package com.example.bestqr.ui.leaderboard;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bestqr.R;
import com.example.bestqr.databinding.FragmentLeaderboardMainBinding;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LeaderboardFragmentMain extends Fragment {

    private LeaderboardViewModelMain leaderboardViewModelMain;
    private FragmentLeaderboardMainBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leaderboardViewModelMain =
                new ViewModelProvider(this).get(LeaderboardViewModelMain.class);

        binding = FragmentLeaderboardMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView profile_icon = binding.toolbarLeaderboardMainProfile;

        // OnClick Listener for User Profile Icon
        // This button navigates you from the Main Leaderboard to the User tab
        profile_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_to_leaderboard_user);
            }
        });

        // OnClick Listener for the Sort button
        // Displays a PopupMenu with three sorting options:
        // Highest Total, Highest Code, # Of Codes
        // This sorts the stats of the local & global leaderboard
        ImageButton sort_button = binding.toolbarLeaderboardMainSort;
        sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), sort_button);
                popup.getMenuInflater().inflate(R.menu.leaderboard_main_sort, popup.getMenu());
                popup.show();
            }

        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setup navigation for Fragment Top-Level destination toolbar
        // Top-Level Fragments need to pass an AppBarConfiguration to the toolbar
        // to function correctly.
        Set<Integer> topLevelDestinations = new HashSet<>(Arrays.asList(
                R.id.navigation_home, R.id.navigation_leaderboard, R.id.navigation_notifications));
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();
        NavController navController = NavHostFragment.findNavController(this);
        NavigationUI.setupWithNavController(binding.toolbarLeaderboardMain, navController, appBarConfiguration);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}