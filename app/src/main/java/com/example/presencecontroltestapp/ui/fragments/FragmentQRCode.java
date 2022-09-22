package com.example.presencecontroltestapp.ui.fragments;

import androidx.activity.OnBackPressedCallback;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.databinding.FragmentQrcodeBinding;

public class FragmentQRCode extends BaseFragment<FragmentQrcodeBinding> {
    public static final String TAG = FragmentQRCode.class.getSimpleName();

    public FragmentQRCode() { super(R.layout.fragment_qrcode, FragmentQrcodeBinding::bind); }
    @Override
    public void onBindCreated(FragmentQrcodeBinding fragmentQrcodeBinding) {
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
