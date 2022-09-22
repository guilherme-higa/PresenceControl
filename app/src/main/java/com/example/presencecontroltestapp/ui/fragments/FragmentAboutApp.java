package com.example.presencecontroltestapp.ui.fragments;

import androidx.activity.OnBackPressedCallback;

import com.example.presencecontroltestapp.BuildConfig;
import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.databinding.FragmentAboutAppBinding;

public class FragmentAboutApp extends BaseFragment<FragmentAboutAppBinding>{
    public static final String TAG = FragmentAboutApp.class.getSimpleName();

    public FragmentAboutApp() { super(R.layout.fragment_about_app, FragmentAboutAppBinding::bind); }

    @Override
    public void onBindCreated(FragmentAboutAppBinding binding) {
        binding.tvVersion.setText(getVersion());
        binding.btnBack.setOnClickListener(v -> popBackStack());
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

    private String getVersion() {
        return getString(R.string.tv_version_number, BuildConfig.VERSION_NAME);
    }
}
