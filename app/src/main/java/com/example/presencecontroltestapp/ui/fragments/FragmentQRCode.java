package com.example.presencecontroltestapp.ui.fragments;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentManager;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.database.MongoDatabase;
import com.example.presencecontroltestapp.databinding.FragmentQrcodeBinding;

public class FragmentQRCode extends BaseFragment<FragmentQrcodeBinding> {
    public static final String TAG = FragmentQRCode.class.getSimpleName();

    private static Context mContext;
    private static MongoDatabase mMongoDatabase;
    private static boolean isConnected = false;

    public FragmentQRCode() { super(R.layout.fragment_qrcode, FragmentQrcodeBinding::bind); }
    @Override
    public void onBindCreated(FragmentQrcodeBinding binding) {
        binding.btnBack.setOnClickListener(v -> popBackStack());
        setBackPressedCallback();
    }

    public static FragmentQRCode newInstance(Context context, MongoDatabase mongoDatabase,
                                                     boolean connected) {
        mContext = context;
        mMongoDatabase = mongoDatabase;
        isConnected = connected;
        return new FragmentQRCode();
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
}
