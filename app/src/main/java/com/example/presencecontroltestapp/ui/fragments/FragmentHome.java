package com.example.presencecontroltestapp.ui.fragments;

import android.Manifest;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.databinding.FragmentHomeBinding;

public class FragmentHome extends BaseFragment<FragmentHomeBinding> {
    public static final String TAG = FragmentHome.class.getSimpleName();

    private static final int DOUBLE_PRESS_INTERVAL = 2000;

    private boolean mDoubleBackPressed = false;
    private OnBackPressedCallback mDefaultBackPressedCallback;
    private ActivityResultLauncher<String[]> mPermissionRequest;

    private Button mBtnLogin, mBtnClean, mBtnRecoveryPassword, mBtnCreateAccount;
    private EditText mEtLogin;
    private EditText mEtPassword;

    public FragmentHome() { super(R.layout.fragment_home, FragmentHomeBinding::bind); }

    @Override
    public void onBindCreated(FragmentHomeBinding binding) {
        mBtnLogin = binding.btnLogin;
        mBtnClean = binding.btnCleanEdit;
        mBtnRecoveryPassword = binding.btnForgotPassword;
        mBtnCreateAccount = binding.btnNotUser;

        mEtLogin =  binding.textRa;
        mEtPassword = binding.textPassword;

        binding.btnLogin.setOnClickListener(v -> onClick(mBtnLogin));
        binding.btnCleanEdit.setOnClickListener(v -> cleanEtFields());
        binding.btnForgotPassword.setOnClickListener(v -> onClick(mBtnRecoveryPassword));
        binding.btnNotUser.setOnClickListener(v -> onClick(mBtnRecoveryPassword));

        String[] permissionsToRequest = new String[]{
                Manifest.permission.CAMERA
        };

        mDefaultBackPressedCallback = getDefaultOnBackPressed();
        setBackPressedCallback(mDefaultBackPressedCallback);
        getPermissions(permissionsToRequest);
    }

    private boolean sdkCheck() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    private void getPermissions(String[] permissions) {
        mPermissionRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean cameraGranted = false;
                    if (sdkCheck()) {
                        cameraGranted = result.getOrDefault(
                                Manifest.permission.CAMERA, false);
                    }

                    if (cameraGranted) {
                        //camera location granted
                    }
                }
        );
        mPermissionRequest.launch(permissions);
    }

    private boolean isValid(String value) {
        if (TextUtils.isEmpty(value)) return false;

        boolean valid = false;
        try {
            Integer.parseInt(value);
            valid = true;
        } catch (NumberFormatException e) {
            Log.e(TAG, "Failed to parse integer - " + e.getLocalizedMessage());
        }
        return valid;
    }

    private boolean checkCredentials() {
        String login = mEtLogin.getText().toString();
        String password = mEtLogin.getText().toString();

        if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) return false;

        boolean valid = false;
        if (isValid(login)) {
            valid = true;
        }
        return valid;
    }

    private void cleanEtFields() {
        mEtLogin.setText(R.string.empty);
        mEtPassword.setText(R.string.empty);

        mEtLogin.requestFocus();
    }

    private OnBackPressedCallback getDefaultOnBackPressed() {
        return new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mDoubleBackPressed) {
                    getActivity().finish();
                }
                mDoubleBackPressed = true;
                Toast.makeText(requireActivity(), getString(R.string.warning_pres_back_again),
                        Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(
                        () -> mDoubleBackPressed = false, DOUBLE_PRESS_INTERVAL);
            }
        };
    }

    private void setBackPressedCallback(OnBackPressedCallback callback) {
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (checkCredentials()) {
                    cleanEtFields();
                    changeFragment(FragmentRoutineDetails.class);
                }
                break;
            case R.id.btnForgotPassword:
                changeFragment(FragmentRecoveryPassword.class);
                break;
            case R.id.btnNotUser:
                changeFragment(FragmentNotUser.class);
                break;
/*            case R.id.btn_show_password:
                mBtnShowPassword.setP*/
        }
    }
}
