package com.example.bestqr.ui.leaderboard;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.bestqr.R;
import com.example.bestqr.databinding.FragmentLeaderboardUserBinding;

public class LeaderboardFragmentUser extends Fragment {

    private LeaderboardViewModelUser leaderboardViewModelUser;
    private FragmentLeaderboardUserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leaderboardViewModelUser =
                new ViewModelProvider(this).get(LeaderboardViewModelUser.class);

        binding = FragmentLeaderboardUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageButton qr_button = binding.toolbarLeaderboardUserQr;
        qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_to_leaderboard_qr);
            }

        });

        ImageButton sort_button = binding.toolbarLeaderboardUserSort;
        sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), sort_button);
                popup.getMenuInflater().inflate(R.menu.leaderboard_user_sort, popup.getMenu());
                popup.show();
            }

        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = NavHostFragment.findNavController(this);
        NavigationUI.setupWithNavController(binding.toolbarLeaderboardUser, navController);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}