package com.example.presencecontroltestapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.presencecontroltestapp.ui.fragments.FragmentHome;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Realm.init(this);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.containerMain, FragmentHome.class, null).commit();
    }
}