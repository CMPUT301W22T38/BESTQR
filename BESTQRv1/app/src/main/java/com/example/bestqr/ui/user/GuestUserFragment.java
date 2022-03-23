package com.example.bestqr.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

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
import com.example.bestqr.Profile;
import com.example.bestqr.R;
import com.example.bestqr.UserViewModel;
import com.example.bestqr.databinding.FragmentGuestUserBinding;
import com.example.bestqr.databinding.FragmentUserBinding;
import com.example.bestqr.qrlistAdapter;

public class GuestUserFragment extends Fragment {

    //    private Profile profile;
    private UserViewModel userViewModel;
    private FragmentGuestUserBinding binding;
    private Profile guestProfile;

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

        // Get Activity-Owned UserViewModel (global to all fragments)
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        binding = FragmentGuestUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Profile guestProfile = userViewModel.getGuestProfile();
        binding.toolbarGuestUserProfile.setText(guestProfile.getUserName());

        ListView qrCodes = binding.qrlist;
        qrlistAdapter myAdapter = new qrlistAdapter(getActivity() , guestProfile.getQrScores(), guestProfile.getQrTimestamps(), guestProfile.getQrBitmaps());
        qrCodes.setAdapter(myAdapter);

        // onClick Listener for the Sort button on the toolbar
        // when pressed, this button displays a PopupMenu containing 3 different sorting methods
        ImageButton sort_button = binding.toolbarGuestUserSort;
        sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), sort_button);
                popup.getMenuInflater().inflate(R.menu.user_sort_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch(id) {
                            case R.id.leaderboard_user_sort_ascending:
                                userViewModel.sortListAscendingScore();
                                break;
                            case R.id.leaderboard_user_sort_descending:
                                userViewModel.sortListDescending();
                                break;
                            case R.id.leaderboard_user_sort_chronological:
                                userViewModel.sortListChronological();
                                break;
                        }
                        qrlistAdapter myAdapter = new qrlistAdapter(getActivity() , guestProfile.getQrScores(), guestProfile.getQrTimestamps(), guestProfile.getQrBitmaps());
                        qrCodes.setAdapter(myAdapter);
                        return true;
                    }
                });
            }
        });

        // onClick Listener for the Info Button on the toolbar
        // When pressed, a dialogfragment showing the user's info will be shown
        // The user will be able to edit their info, and have it updated in the db.
        ImageButton info_button = binding.toolbarUserInfo;
        info_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_user_to_navigation_user_info);
            }
        });



//        ImageButton delete_button = binding.toolbarUserDelete;
//        delete_button.setOnClickListener(new View.OnClickListener() {
//            boolean checkbox_visible = false;
//            @Override
//            public void onClick(View view) {
//                ListView listview = (ListView) getView().findViewById(R.id.qrlist);
//                ArrayList<CheckBox> checkBoxes = new ArrayList<>();
//
//                for (int i = 0; i < listview.getChildCount(); i++) {
//                    View v = listview.getChildAt(i);
//                    CheckBox checkbox = (CheckBox) v.findViewById(R.id.checkBox);
//                    checkBoxes.add(checkbox);
//                }
//
//                if (checkbox_visible) {
//                    for (CheckBox checkBox: checkBoxes) {
//                        checkBox.setVisibility(View.VISIBLE);
//                    }
//                }
//                else {
//
//                }
//
//                checkbox_visible = (checkbox_visible) ? false : true;
//            }
//
////                ListView listview = (ListView) getView().findViewById(R.id.qrlist);
////                listview.getView
////                System.out.println(myAdapter.getCount());
////                View v = myAdapter.getView(1);
////                CheckBox box = (CheckBox) v.findViewById(R.id.checkbox);
////                myAdapter.getCount()
//        });
//
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
        NavigationUI.setupWithNavController(binding.toolbarUser, navController, appBarConfiguration);
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