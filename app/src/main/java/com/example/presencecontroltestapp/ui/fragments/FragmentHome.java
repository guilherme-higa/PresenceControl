package com.example.presencecontroltestapp.ui.fragments;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.databinding.FragmentHomeBinding;

public class FragmentHome extends BaseFragment<FragmentHomeBinding> {
    public static final String TAG = FragmentHome.class.getSimpleName();

    private Button mBtnLogin;
    private Button mBtnClean;
    private EditText mEdRA;
    private EditText mEdPassword;

    public FragmentHome() { super(R.layout.fragment_home, FragmentHomeBinding::bind); }

    @Override
    public void onBindCreated(FragmentHomeBinding binding) {
        Log.d(TAG, "<---Higa---> binding");
        mBtnLogin = binding.btnLogin;
        mBtnClean = binding.btnCleanEdit;

        binding.btnLogin.setOnClickListener(v -> onClick(mBtnLogin));
        binding.btnCleanEdit.setOnClickListener(v -> onClick(mBtnClean));
    }

    private boolean isTxtValid(EditText string) {
        boolean result = false;

        if(!TextUtils.isEmpty(string.getText())) {
            result = true;
        }
        Log.d(TAG, "<---Higa---> result : " + result);
        return result;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                Log.d(TAG, "<---Higa--> btnLogin");
                mEdRA = getBinding().textRa;
                mEdPassword = getBinding().textPassword;
                Log.d(TAG, "<---Higa---> edRa : " + mEdRA.getText());
  /*              if (isTxtValid(mEdRA) && (isTxtValid(mEdPassword)))*/
                    changeFragment(FragmentRoutineDetails.class);
                break;
            case R.id.btnCleanEdit:
                Log.d(TAG, "<---Higa---> btnCleanEdit");
                getBinding().textRa.setText("");
                getBinding().textPassword.setText("");
                break;
        }
    }
}
