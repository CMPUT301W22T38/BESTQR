package com.example.bestqr.ui.qr;


import android.graphics.Bitmap;

import com.example.bestqr.QRCODE;
import com.example.bestqr.R;
import com.example.bestqr.UserViewModel;
import com.example.bestqr.ui.leaderboard.LeaderboardViewModel;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;

import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
    private QRCODE qr;
    private FragmentQrBinding binding;
    private Profile userProfile;
    private ImageView image;
    private ImageButton commentButton;
    private ImageButton deleteButton;
    private Bitmap bitmap;
    private EditText addComments;
    private TextView allComments;
    private String qrComments;
    private Button addButton;

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
        qrViewModel = new ViewModelProvider(requireActivity()).get(QrViewModel.class);

        // Activity-owned ViewModel, (global to all fragments)
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        binding = FragmentQrBinding.inflate(inflater, container, false);
        addComments = binding.addComment;
        addButton = binding.addButton;
        View root = binding.getRoot();

        userProfile = userViewModel.getUserProfile();
        qr = userViewModel.getSelectedQrcode();

        if (qr != null) {
            bitmap = qr.getCode();
        }

        image = binding.imageView;
        image.setImageBitmap(bitmap);

        commentButton =  binding.toolbarQrComments;
        allComments = binding.comments;

        deleteButton = binding.toolbarUserDelete;

        qrComments = String.join("\n",qr.getComments());
        allComments.setText(qrComments);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComments.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(addComments.getText().toString()) == false){
                   qr.addComments(addComments.getText().toString());
                }
                addComments.setText("");
                addComments.setVisibility(View.INVISIBLE);
                addButton.setVisibility(View.INVISIBLE);
                qrComments = String.join("\n",qr.getComments());
                allComments.setText(qrComments);

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfile.removeCodebyPosition(userProfile.getScannedCodes().indexOf(qr));
                userViewModel.setSelectedQrcode(null);
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_to_user);
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