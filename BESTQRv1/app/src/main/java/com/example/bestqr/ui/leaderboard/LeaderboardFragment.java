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

import com.example.bestqr.CameraActivity;
import com.example.bestqr.R;
import com.example.bestqr.databinding.FragmentLeaderboardBinding;

public class LeaderboardFragment extends Fragment {

    private LeaderboardViewModel leaderboardViewModel;
    private FragmentLeaderboardBinding binding;

    /**
     * Creates and returns the root view of the fragment
     * @param inflater
     *      Instantiates a layout file into the view
     * @param container
     *      Defines structure from the view
     * @param savedInstanceState
     *      Contains data of fragment's previous activity
     * @return root
     *
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leaderboardViewModel =
                new ViewModelProvider(this).get(LeaderboardViewModel.class);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView profile_icon = binding.toolbarLeaderboardProfile;

        /**
         * OnClick Listener for the Sort button
         * Displays a PopupMenu with three sorting options:
         * Highest Total, Highest Code, # Of Codes
         * This sorts the stats of the local & global leaderboard
         */

        ImageButton sort_button = binding.toolbarLeaderboardSort;
        sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), sort_button);
                popup.getMenuInflater().inflate(R.menu.leaderboard_sort_menu, popup.getMenu());
                popup.show();
            }

        });

        /**
         * Disabled, as it introduces minor unintuitive navigation when opening non top-level tabs from user tab.
         * Consider re-enabling if this is fixed
         * however, this is non-essential for app functionality.
        // onClick Listener for the profile button on the toolbar
        // This button navigates to the UserFragment, which displays a list of the User's QR codes
        TextView user_profile = binding.toolbarLeaderboardProfile;
        user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_to_user);
            }

        });
        */

        return root;
    }

    /**
     *
     * Continues initialization of the fragment, called after onCreateView
     * @param view
     *      The view of the fragment
     * @param savedInstanceState
     *      Contains data of fragment's previous activity
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setup navigation for Fragment Top-Level destination toolbar
        // Top-Level Fragments need to pass an AppBarConfiguration to the toolbar
        // to function correctly.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(CameraActivity.getTopLevelDestinations()).build();
        NavController navController = NavHostFragment.findNavController(this);
        NavigationUI.setupWithNavController(binding.toolbarLeaderboard, navController, appBarConfiguration);

    }

    /**
     * Upon destroying the view, unbinds it allowing for binding of other views
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}