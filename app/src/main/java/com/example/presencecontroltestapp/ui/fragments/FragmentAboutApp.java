package com.example.presencecontroltestapp.ui.fragments;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentManager;

import com.example.presencecontroltestapp.BuildConfig;
import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.databinding.FragmentAboutAppBinding;

public class FragmentAboutApp extends BaseFragment<FragmentAboutAppBinding>{
    public static final String TAG = FragmentAboutApp.class.getSimpleName();

    public FragmentAboutApp() { super(R.layout.fragment_about_app, FragmentAboutAppBinding::bind); }

    public static FragmentAboutApp newInstance() {
        return new FragmentAboutApp();
    }

    @Override
    public void onBindCreated(FragmentAboutAppBinding binding) {
        binding.tvVersion.setText(getVersion());
        binding.btnBack.setOnClickListener(v -> popBackStack());
        hideKeyboard();
        setBackPressedCallback();
    }

    protected void popBackStack() {
        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack();
    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            View view = getActivity().getCurrentFocus();
            if (view == null) {
                view = new View(getActivity());
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException while hiding keyboard!");
        }
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
