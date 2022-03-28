package com.example.bestqr.ui.leaderboard;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bestqr.CameraActivity;
import com.example.bestqr.Database;
import com.example.bestqr.LeaderboardListAdapter;
import com.example.bestqr.Profile;
import com.example.bestqr.R;
import com.example.bestqr.databinding.FragmentLeaderboardBinding;
import com.example.bestqr.UserViewModel;
import com.example.bestqr.qrlistAdapter;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {

    private LeaderboardViewModel leaderboardViewModel;
    private FragmentLeaderboardBinding binding;
    private UserViewModel userViewModel;
    private ListView listview;
    private LeaderboardListAdapter myAdapter;

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
                new ViewModelProvider(requireActivity()).get(LeaderboardViewModel.class);

        // Get Activity-Owned UserViewModel (global to all fragments)
        userViewModel =
                new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView profile_icon = binding.toolbarLeaderboardProfile;

        // Fetch all the scores from the database for display
        // TODO: This should be threaded.
        if(!leaderboardViewModel.scoresInitialized) {
            leaderboardViewModel.updateScoreBlocks(userViewModel.getDb());
            // Sort scores after fetching. This is the default sorting method when the fragment is created.
            leaderboardViewModel.sortScoresByTotalSum();
        }

        myAdapter = new LeaderboardListAdapter(getContext(),
                leaderboardViewModel.getScoreBlocks(), 0);
        binding.leaderboardList.setAdapter(myAdapter);


        /**
         * Listener for clicks on users in ArrayAdapter
         * Upon clicking on a user, navigate to lower-level GuestUserFragment
         */
        binding.leaderboardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                // Log.d("LeaderboardFragment", "Clicked on object!" + position);
                // Profile clickedProfile = (Profile) parent.getItemAtPosition(position);
                // userViewModel.setGuestProfile(clickedProfile);
                // NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                // navController.navigate(R.id.action_navigation_leaderboard_to_navigation_guest_user);
            }
        });



        // TODO: Implement putting user scores into highlighted bar
        // ArrayList<Integer> scoreRank = leaderboardViewModel.getUserScoreAndRank(userViewModel.getUserProfile().getDeviceID());
        // binding.leaderboardUserBlockValue.setText(scoreRank.get(0));
        // binding.leaderboardUserBlockRank.setText(scoreRank.get(1));

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
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch(id) {
                            case R.id.leaderboard_menu_code:
                                binding.leaderboardListLabelValue.setText("Highest unique");
                                leaderboardViewModel.sortScoresByHighestUnique();
                                break;
                            case R.id.leaderboard_menu_numcodes:
                                leaderboardViewModel.sortScoresByTotalScanned();
                                break;
                            case R.id.leaderboard_menu_total:
                                binding.leaderboardListLabelValue.setText("Total score");
                                leaderboardViewModel.sortScoresByTotalSum();
                                break;
                        }
                        // LeaderboardListAdapter newAdapter = new LeaderboardListAdapter(getActivity(),
                        // leaderboardViewModel.getScoreBlocks(), leaderboardViewModel.getSortingMethod());
                        myAdapter.setSortingMethod(leaderboardViewModel.getSortingMethod());
                        myAdapter.clear();
                        myAdapter.addAll(leaderboardViewModel.getScoreBlocks());


                        // binding.leaderboardList.setAdapter(newAdapter);
                        return true;
                    }
                });
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

        //updateUserScores();
		
    }

    /**
     * Updates the top bar with user's total points, Highest-Point code, and # of codes scanned.
     */
    public void updateUserScores(){
        Profile profile = userViewModel.getUserProfile();
        int num_scanned = profile.getNumberCodesScanned();
        int total_score = profile.getScore();
        int highest_score = profile.getHighestScore();
        // Get the platform's line break character, as it isn't platform-independent
        String lb = System.getProperty("line.separator");
        String score_string = String.format("Total Points: %d%sHighest-Point Score: %d%s# Codes Scanned: %d", total_score, lb, highest_score, lb, num_scanned);
        binding.toolbarLeaderboardStats.setText(score_string);
    }

    /**
     * Upon destroying the view, unbinds it allowing for binding of other views
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*
    class FetchAndSort implements Runnable {
        LeaderboardViewModel leaderboardViewModel;
        Database db;
        FetchAndSort(LeaderboardViewModel leaderboardViewModel, Database db){
            this.leaderboardViewModel = leaderboardViewModel;
            this.db = db;
        }

        public void run(){
            leaderboardViewModel.updateScoreBlocks(this.db);
            leaderboardViewModel.sortScoresByTotalSum();
        }


    }
    */
}