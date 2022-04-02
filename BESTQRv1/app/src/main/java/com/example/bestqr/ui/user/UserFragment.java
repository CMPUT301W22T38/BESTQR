package com.example.bestqr.ui.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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

import com.example.bestqr.ProfileViewModel;
import com.example.bestqr.UserViewModel;
import com.example.bestqr.adapters.profilelistAdapter;
import com.example.bestqr.CameraActivity;
import com.example.bestqr.R;
import com.example.bestqr.databinding.FragmentUserBinding;

import com.example.bestqr.models.Profile;
import com.example.bestqr.ui.qr.QrViewModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class UserFragment extends Fragment {

    private UserViewModel userViewModel;
    private FragmentUserBinding binding;
    private QrViewModel qrViewModel;
//    private ProfileViewModel profileViewModel;


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
//        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        qrViewModel = new ViewModelProvider(this).get(QrViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Profile userProfile = userViewModel.getUserProfile();
        binding.toolbarUserProfile.setText(userProfile.getUserName());
//        binding.toolbarUserProfile.setText(profileViewModel.getProfileMutableLiveData().getValue().getUserName());

        ListView qrCodes = binding.qrlist;
        profilelistAdapter myAdapter = new profilelistAdapter(requireContext() , userProfile.getQrScores(), userProfile.getQrTimestamps(), userProfile.getQrBitmaps());
        qrCodes.setAdapter(myAdapter);

        qrCodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userViewModel.setSelectedQrcode(userProfile.getScannedCodes().get(i));
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_from_user_to_qr);

            }
        });

        // onClick Listener for the QR button on the toolbar
        // This button navigates to QrFragment, which displays the Profile's unique QRCODE
        ImageButton qr_button = binding.toolbarUserQr;
        qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userViewModel.setSelectedQrcode(userProfile.getDeviceQrCode());
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_from_user_to_qr);

            }
        });

        // onClick Listener for the Sort button on the toolbar
        // when pressed, this button displays a PopupMenu containing 3 different sorting methods
        ImageButton sort_button = binding.toolbarUserSort;
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
                        profilelistAdapter myAdapter = new profilelistAdapter(getActivity() , userProfile.getQrScores(), userProfile.getQrTimestamps(), userProfile.getQrBitmaps());
                        qrCodes.setAdapter(myAdapter);
                        return true;
                    }
                });
            }
        });

        // onClick Listener for the Info Button on the toolbar
        // When pressed, a dialog fragment showing the user's info will be shown
        // The user will be able to edit their info, and have it updated in the db.
        ImageButton info_button = binding.toolbarUserInfo;
        info_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_user_to_navigation_user_info);
            }
        });

        // onClick Listener for the Delete Button on the toolbar
        // when pressed, the current user will be deleted, ideally after showing a confirmation dialog.
        ImageButton delete_button = binding.toolbarUserDelete;
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Delete current user from Database, and navigate to some "create new user" page.

                userViewModel.setUserProfile(null);
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                // navController.navigate(TODO: CREATE NEW USER PAGE, NAVIGATE TO IT HERE);
            }
        });


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


