package com.example.bestqr.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bestqr.R;
import com.example.bestqr.databinding.FragmentMapBinding;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MapFragment extends Fragment {

    private MapViewModel mapViewModel;
    private FragmentMapBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
        NavigationUI.setupWithNavController(binding.toolbarMap, navController, appBarConfiguration);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}