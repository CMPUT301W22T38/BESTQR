package com.example.bestqr.ui.leaderboard;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bestqr.R;
import com.example.bestqr.databinding.FragmentLeaderboardMainBinding;

public class LeaderboardFragmentMain extends Fragment {

    private LeaderboardViewModelMain leaderboardViewModelMain;
    private FragmentLeaderboardMainBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leaderboardViewModelMain =
                new ViewModelProvider(this).get(LeaderboardViewModelMain.class);

        binding = FragmentLeaderboardMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Hide toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        ImageButton button = binding.toolbarLeaderboardMainSort;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), button);
                popup.getMenuInflater().inflate(R.menu.leaderboard_main_menu, popup.getMenu());

                popup.show();
            }

        });

        return root;
    }

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.leaderboard_main_menu, popup.getMenu());
        popup.show();
    }
}