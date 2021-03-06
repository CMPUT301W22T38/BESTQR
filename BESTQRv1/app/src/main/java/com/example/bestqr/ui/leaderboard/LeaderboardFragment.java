package com.example.bestqr.ui.leaderboard;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
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
import com.example.bestqr.Database.Database;
import com.example.bestqr.adapters.LeaderboardListAdapter;
import com.example.bestqr.models.Profile;
import com.example.bestqr.R;
import com.example.bestqr.databinding.FragmentLeaderboardBinding;
import com.example.bestqr.UserViewModel;

public class LeaderboardFragment extends Fragment {

    private LeaderboardViewModel leaderboardViewModel;
    private FragmentLeaderboardBinding binding;
    private UserViewModel userViewModel;
    private LeaderboardListAdapter myAdapter;


    /**
     * This method initializes a search on the listAdapter,
     * and changes the ui elements accordingly.
     * @param searchText : The EditText, where users input a username to search.
     * @param popupWindow : The search popupWindow. Contains EditText, and confirmation button.
     */
    private void doSearch(EditText searchText, PopupWindow popupWindow){
        binding.toolbarLeaderboardSearch.setActivated(true);
        myAdapter.getFilter().filter(searchText.getText());
        popupWindow.dismiss();
    }

    private void cancelSearch(){
        binding.toolbarLeaderboardSearch.setActivated(false);
        myAdapter.getFilter().filter("");
    }

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

//        // Fetch all the scores from the database for display
//        // TODO: This should be threaded.
//        if(!leaderboardViewModel.scoresInitialized) {
//            leaderboardViewModel.updateScoreBlocks(userViewModel.getDb());
////             Sort scores after fetching. This is the default sorting method when the fragment is created.
//            leaderboardViewModel.sortScoresByTotalSum();
//        }

        // Fetch all the scores from the database for display
        leaderboardViewModel.updateScoreBlocks();
        leaderboardViewModel.sortScoresByTotalSum();

        myAdapter = new LeaderboardListAdapter(getContext(), R.layout.leaderboardlist_item,
                leaderboardViewModel.getScoreBlocks(), 0);
        binding.leaderboardList.setAdapter(myAdapter);

        // Setup user profile name
        binding.toolbarLeaderboardProfile.setText(userViewModel.getUserProfile().getUserName());

        // Setup listener for search button clicks
        // Opens popup window with a search interface.
        // Toggles between the search icon, and a large "X" depending on whether the
        // listView is currently filtered by the search term, or not.
        binding.toolbarLeaderboardSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make sure there isn't a current search.
                // Active = "cancel" icon is showing
                if(!binding.toolbarLeaderboardSearch.isActivated()) {
                    View popupView = inflater.inflate(R.layout.search_popup, null);
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    ImageButton enterButton = popupView.findViewById(R.id.search_popup_enter);
                    EditText searchField = popupView.findViewById(R.id.search_popup_edittext);
                    // Set width to parent's width (Phone screen width), and height to wrap content.
                    final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    // Have popupWindow appear below the toolbar.
                    popupWindow.showAsDropDown(binding.toolbarLeaderboard);
                    // Below, we have two ways of confirming the search terms
                    // Either the checkmark button next to the editText, or the enter button on the keyboard

                    // Listener for the enter button on the keyboard. (Setup as a "search" button in layout)
                    searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                            doSearch(searchField, popupWindow);
                            return true;
                        }
                    });
                    // Listener for the checkmark button next to the EditText
                    enterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doSearch(searchField, popupWindow);
                        }
                    });
                }
                else{
                    // If the previous search is still active, the search button will appear as a large "X"
                    // Pressing this button will toggle back to showing all users
                    cancelSearch();
                }
            }
        });


        /**
         * Listener for clicks on users in ArrayAdapter
         * Upon clicking on a user, navigate to lower-level GuestUserFragment
         */
        binding.leaderboardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LeaderboardScoreBlock clickedScore = myAdapter.getItem(position);
                String guestId = clickedScore.getAndroidId();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);

                // Check if clicked user is the current user
                if(guestId.equals(userViewModel.getUserProfile().getAndroidId())){
                    // Navigate to Main User fragment rather than guest user fragment
                    navController.navigate(R.id.action_navigation_leaderboard_to_navigation_user);
                }
                else{
                    Profile guestProfile = Database.getUser(guestId);
                    userViewModel.setGuestProfile(guestProfile);
                    navController.navigate(R.id.action_navigation_leaderboard_to_navigation_guest_user);
                }
            }
        });

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
                        myAdapter.setSortingMethod(leaderboardViewModel.getSortingMethod());
                        myAdapter.clear();
                        myAdapter.addAll(leaderboardViewModel.getScoreBlocks());
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

        updateUserScores();
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

    /* possible skeleton code for threaded operation.
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