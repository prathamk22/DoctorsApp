package com.example.doctorsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {


    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean check = getIntent().getBooleanExtra("doc",false);
        String phone = getIntent().getStringExtra("phone");

        if(check){

            DoctorsFragment doctorsFragment = new DoctorsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("phone",phone);
            doctorsFragment.setArguments(bundle);

            setFragment(doctorsFragment);
        }else{
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if(firebaseUser!=null){
                Intent it = new Intent(MainActivity.this,FirstActivity.class);
                startActivity(it);
                finish();
            }else {
                setFragment(new EmailFragment());
            }
        }
    }


    public void setFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame,fragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
