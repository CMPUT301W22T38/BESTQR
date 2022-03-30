package com.example.bestqr.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.bestqr.R;
import com.example.bestqr.UserViewModel;
import com.example.bestqr.databinding.FragmentUserInfoBinding;
import com.example.bestqr.models.Profile;

public class UserInfoFragment extends DialogFragment {

    private FragmentUserInfoBinding binding;
    private UserViewModel userViewModel;
    private Profile userProfile;

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
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        binding = FragmentUserInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // Setup navigation for Fragment-owned Toolbar
        // As this destination is not top-level, we don't need to pass an AppBarConfiguration
        // which allows the back button to appear.
        NavController navController = NavHostFragment.findNavController(this);
        NavigationUI.setupWithNavController(binding.toolbarUserInfo, navController);

        // TEMP: Add User's info to the textboxes
        // TODO: Integrate firebase
        userProfile = userViewModel.getUserProfile();
        binding.userInfoUsername.setText(userProfile.getUserName());
        binding.userInfoEmail.setText(userProfile.getEmailAddress());
        binding.userInfoPhoneNumber.setText(userProfile.getPhoneNumber());
    }

    /**
     * Upon destroying the view, unbinds it allowing for binding of other views
     */
    @Override
    public void onDestroyView() {
        String name = binding.userInfoUsername.getText().toString();
        String email = binding.userInfoEmail.getText().toString();
        String phone = binding.userInfoPhoneNumber.getText().toString();


        userViewModel.getUserProfile().ChangeUserName(name);
        userViewModel.getUserProfile().ChangeEmailAddress(email);
        userViewModel.getUserProfile().ChangePhoneNumber(phone);


        userViewModel.setUserProfile(userProfile);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_navigation_to_user);
        super.onDestroyView();
        binding = null;
    }


}
