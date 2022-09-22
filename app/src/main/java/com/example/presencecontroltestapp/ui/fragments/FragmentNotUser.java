package com.example.presencecontroltestapp.ui.fragments;

import androidx.activity.OnBackPressedCallback;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.databinding.FragmentAddUserBinding;

public class FragmentNotUser extends BaseFragment<FragmentAddUserBinding>{
    public static final String TAG = FragmentNotUser.class.getSimpleName();

    public FragmentNotUser() { super(R.layout.fragment_add_user, FragmentAddUserBinding::bind); }

    @Override
    public void onBindCreated(FragmentAddUserBinding binding) {
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
