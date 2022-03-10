package com.example.bestqr.ui.leaderboard;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bestqr.R;
import com.example.bestqr.databinding.FragmentLeaderboardMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
        profile_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment user_fragment = new LeaderboardFragmentUser();
                FragmentTransaction fragment_transaction = getParentFragmentManager().beginTransaction();
                fragment_transaction.setReorderingAllowed(true);
                fragment_transaction.replace(R.id.nav_host_fragment_activity_main, user_fragment);
                fragment_transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragment_transaction.addToBackStack(null);
                fragment_transaction.commit();

                // navController.navigate(R.id.action_navigation_leaderboard_main_to_navigation_leaderboard_user);
            }
        });


        ImageButton sort_button = binding.toolbarLeaderboardMainSort;
        sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), sort_button);
                popup.getMenuInflater().inflate(R.menu.leaderboard_main_menu, popup.getMenu());
                popup.show();
            }

        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setup navigation for fragment-owned toolbar
        Set<Integer> topLevelDestinations = new HashSet<>(Arrays.asList(
                R.id.navigation_home, R.id.navigation_leaderboard, R.id.navigation_notifications));
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();
        NavController navController = NavHostFragment.findNavController(this);
        NavigationUI.setupWithNavController(binding.toolbarLeaderboardMain, navController, appBarConfiguration);
    }
}