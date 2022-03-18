package com.example.bestqr.ui.qr;


import android.graphics.Bitmap;

import com.example.bestqr.UserViewModel;
import com.example.bestqr.ui.leaderboard.LeaderboardViewModel;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.bestqr.Profile;
import com.example.bestqr.databinding.FragmentQrBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrFragment extends Fragment {

    private QrViewModel qrViewModel;
    private UserViewModel userViewModel;
    private FragmentQrBinding binding;
    private Profile userProfile;
    private ImageView image;
    private Bitmap bitmap;

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
        // ViewModel owned by this fragment
        qrViewModel = new ViewModelProvider(this).get(QrViewModel.class);
        // Activity-owned ViewModel, (global to all fragments)
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        binding = FragmentQrBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userProfile = userViewModel.getUserProfile();

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode("hello", BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        image = binding.imageView;
        image.setImageBitmap(bitmap);

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
        NavigationUI.setupWithNavController(binding.toolbarQr, navController);
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