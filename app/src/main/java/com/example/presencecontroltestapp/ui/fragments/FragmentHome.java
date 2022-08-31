package com.example.presencecontroltestapp.ui.fragments;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.databinding.FragmentHomeBinding;

public class FragmentHome extends BaseFragment<FragmentHomeBinding> {
    public static final String TAG = FragmentHome.class.getSimpleName();

    public FragmentHome() { super(R.layout.fragment_home, FragmentHomeBinding::bind); }

    @Override
    public void onBindCreated(FragmentHomeBinding binding) {
        binding.btnCleanEdit.setOnClickListener(v -> binding.textRa.setText(""));
        binding.btnCleanEdit.setOnClickListener(v -> binding.textPassword.setText(""));
    }
}
