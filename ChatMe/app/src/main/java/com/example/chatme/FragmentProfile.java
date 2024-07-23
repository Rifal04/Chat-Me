package com.example.chatme;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FragmentProfile extends Fragment {

    private static final int ubahGambar = 1;

    Button logoutButton, btnUbah;
    TextView user, bio;
    ImageView proImageIMG;
    Uri imageUri;

    FirebaseStorage storage;
    StorageReference storageReference;

    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logoutButton = view.findViewById(R.id.btnLogout);
        user = view.findViewById(R.id.Username);
        bio = view.findViewById(R.id.Bio);
        btnUbah = view.findViewById(R.id.btnUbahimg);
        proImageIMG = view.findViewById(R.id.imgProfil);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        SharedPreferences prefs = getActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "");
        String email = prefs.getString("email", "");

        user.setText(username);
        bio.setText(email);

        proImageIMG.setImageURI(imageUri);

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("LOGIN", getActivity().MODE_PRIVATE).edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), ActivityLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //noinspection deprecation
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), ubahGambar);
    }

    /**
     * @noinspection deprecation
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ubahGambar && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            uploadImg();
        }
    }

    private void uploadImg() {
        if (imageUri != null) {
            StorageReference ref = storageReference.child("images/" + user.getText().toString() + "/profile.jpg");

            ref.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {

                            Glide.with(requireContext())
                                    .load(uri)
                                    .apply(new RequestOptions().circleCrop())
                                    .into(proImageIMG);
                            proImageIMG.setImageURI(imageUri);

                        });


                        Toast.makeText(getActivity(), "Gambar Berhasil Diupload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(getActivity(), "Gagal " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
