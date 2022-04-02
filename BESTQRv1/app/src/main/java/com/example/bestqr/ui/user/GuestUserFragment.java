package com.example.bestqr.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import androidx.navigation.ui.NavigationUI;

import com.example.bestqr.models.Profile;
import com.example.bestqr.R;
import com.example.bestqr.UserViewModel;
import com.example.bestqr.databinding.FragmentGuestUserBinding;
import com.example.bestqr.adapters.profilelistAdapter;

public class GuestUserFragment extends Fragment {

    //    private Profile profile;
    private UserViewModel userViewModel;
    private GuestUserViewModel guestUserViewModel;
    private FragmentGuestUserBinding binding;

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
        guestUserViewModel = new ViewModelProvider(this).get(GuestUserViewModel.class);

        guestUserViewModel.setUserProfile(userViewModel.getGuestProfile());

        binding = FragmentGuestUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Profile guestProfile = guestUserViewModel.getUserProfile();

        binding.toolbarGuestUserProfile.setText(guestProfile.getUserName());

        ListView qrCodes = binding.guestUserQrlist;
        profilelistAdapter myAdapter = new profilelistAdapter(getActivity(), guestProfile.getQrScores(), guestProfile.getQrTimestamps(), guestProfile.getQrBitmaps());
        qrCodes.setAdapter(myAdapter);

        qrCodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userViewModel.setSelectedQrcode(guestProfile.getScannedCodes().get(i));
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_from_user_to_qr);

            }
        });


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
                                guestUserViewModel.sortListAscendingScore();
                                break;
                            case R.id.leaderboard_user_sort_descending:
                                guestUserViewModel.sortListDescending();
                                break;
                            case R.id.leaderboard_user_sort_chronological:
                                guestUserViewModel.sortListChronological();
                                break;
                        }
                        profilelistAdapter myAdapter = new profilelistAdapter(getActivity() , guestProfile.getQrScores(), guestProfile.getQrTimestamps(), guestProfile.getQrBitmaps());
                        qrCodes.setAdapter(myAdapter);
                        return true;
                    }
                });
            }
        });


        Button delete_button = binding.toolbarGuestUserOwnerDelete;
        // TODO: only show delete button if current user is an "owner"
        // delete_button.setVisibility(View.VISIBLE);
        // Delete button for other users is otherwise set to be invisible and unclickable.
        delete_button.setVisibility(View.GONE);

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: remove user from the database, update app's user list

                userViewModel.setGuestProfile(null);
                // Navigate back to parent fragment
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigateUp();
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
        // Setup navigation for Fragment-owned Toolbar
        // As this destination is not top-level, we don't need to pass an AppBarConfiguration
        // which allows the back button to appear.
        NavController navController = NavHostFragment.findNavController(this);
        NavigationUI.setupWithNavController(binding.toolbarGuestUser, navController);
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
