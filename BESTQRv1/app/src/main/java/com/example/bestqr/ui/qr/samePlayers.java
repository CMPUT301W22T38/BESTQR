package com.example.bestqr.ui.qr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.example.bestqr.Database;
import com.example.bestqr.models.Profile;
import com.example.bestqr.R;
import com.example.bestqr.UserViewModel;
import com.example.bestqr.databinding.FragmentSameplayerlistBinding;
import com.example.bestqr.adapters.profilelistAdapter;
import com.example.bestqr.models.QRCODE;
import com.example.bestqr.Database;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class samePlayers extends Fragment {
    //    private Profile profile;
    private UserViewModel userViewModel;
    private FragmentSameplayerlistBinding binding;

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


        binding = FragmentSameplayerlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        QRCODE qr = userViewModel.getSelectedQrcode();

        ArrayList sameplayer_list = Database.getAssociatedUsers(qr.getHash());

        ListView sameplayers = binding.sameplayerlist;
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),R.layout.sameplayer_listview,R.id.sameplayer_textview,sameplayer_list);
        sameplayers.setAdapter(myAdapter);
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
        NavigationUI.setupWithNavController(binding.toolbarPlayerlist, navController);
    }

    /**
     * Resumes upon returning to the fragment
     */
    @Override
    public void onResume() {
        super.onResume();
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