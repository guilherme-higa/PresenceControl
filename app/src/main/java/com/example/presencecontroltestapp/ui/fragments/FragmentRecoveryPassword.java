package com.example.presencecontroltestapp.ui.fragments;

import androidx.activity.OnBackPressedCallback;

import com.example.presencecontroltestapp.R;

import com.example.presencecontroltestapp.databinding.FragmentRecoveryPasswordBinding;

public class FragmentRecoveryPassword extends BaseFragment<FragmentRecoveryPasswordBinding>{
    public static final String TAG = FragmentRecoveryPassword.class.getSimpleName();

    public FragmentRecoveryPassword() { super(R.layout.fragment_recovery_password, FragmentRecoveryPasswordBinding::bind); }

    @Override
    public void onBindCreated(FragmentRecoveryPasswordBinding fragmentRecoveryPasswordBinding) {
        setBackPressedCallback();
    }

    private void setBackPressedCallback() {
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        popBackStack();
                    }
                });
    }
}
