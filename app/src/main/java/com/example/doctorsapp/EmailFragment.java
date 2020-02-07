package com.example.doctorsapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmailFragment extends Fragment {


    public EmailFragment() {

    }

    private static final int RC_SIGN_IN = 123;

    TextView email,phoneNumber;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Email Login and Phone login

        View view = inflater.inflate(R.layout.fragment_email, container, false);


        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phoneNumber);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailLogin();
            }
        });

        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInIntent();
            }
        });

        return view;
    }

    public void emailLogin(){

        //Email login work should be done here.

    }

    public void signInIntent() {
        List<AuthUI.IdpConfig> providers =
                Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                    Log.e("phoneNo:",user.getPhoneNumber()+"");

                Bundle bundle = new Bundle();
                bundle.putString("phone",user.getPhoneNumber());

                DetailsFragment detailsFragment = new DetailsFragment();
                detailsFragment.setArguments(bundle);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame,detailsFragment)
                        .commit();
            } else {
                Log.e("phoneNo:","sign in failed");
            }
        }
    }
}
