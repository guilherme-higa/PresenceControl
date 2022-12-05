package com.example.presencecontroltestapp.ui.fragments;

import android.Manifest;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentManager;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.database.MongoDatabase;
import com.example.presencecontroltestapp.databinding.FragmentHomeBinding;
import com.example.presencecontroltestapp.entities.Students;
import com.example.presencecontroltestapp.provider.IDatabaseResult;

public class FragmentHome extends BaseFragment<FragmentHomeBinding>  implements IDatabaseResult {
    public static final String TAG = FragmentHome.class.getSimpleName();

    private static final int DOUBLE_PRESS_INTERVAL = 2000;
    private boolean mDoubleBackPressed = false;
    private OnBackPressedCallback mDefaultBackPressedCallback;
    private ActivityResultLauncher<String[]> mPermissionRequest;
    private static Handler mHandler;

    private FragmentRecoveryPassword mFragmentRecovery;
    private FragmentCreateAccount mFragmentCreateAccount;

    private MongoDatabase mMongoDatabase;
    private boolean isMongoDBConnected = false;

    private String stringRa, stringPassword;
    private EditText mEtLogin;
    private EditText mEtPassword;

    public FragmentHome() { super(R.layout.fragment_home, FragmentHomeBinding::bind); }

    @Override
    public void onBindCreated(FragmentHomeBinding binding) {
        mEtLogin =  binding.edRa;
        mEtPassword = binding.edPassword;
        mHandler = getHandler();

        binding.tvLogin.setOnClickListener(v -> onClick(binding.tvLogin));
        binding.tvCleanFields.setOnClickListener(v -> cleanEtFields());
        binding.tvForgotPassword.setOnClickListener(v -> onClick(binding.tvForgotPassword));
        binding.tvCreateAccount.setOnClickListener(v -> onClick(binding.tvCreateAccount));

        //add permissions here
        String[] permissionsToRequest = new String[]{
                Manifest.permission.CAMERA
        };

        mMongoDatabase = new MongoDatabase(requireContext(), this);
        mMongoDatabase.connectToMongoDB();

        mDefaultBackPressedCallback = getDefaultOnBackPressed();
        setBackPressedCallback(mDefaultBackPressedCallback);
        getPermissions(permissionsToRequest);
    }

    private Handler getHandler() {
        HandlerThread handlerThread = new HandlerThread(getString(R.string.presence_control_thread));
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
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

    private boolean checkCredentials() {
        boolean valid = false;
        stringRa = mEtLogin.getText().toString();
        stringPassword = mEtPassword.getText().toString();

        if (!isMongoDBConnected) return false;
        if (TextUtils.isEmpty(stringRa) || TextUtils.isEmpty(stringPassword)) {
            Toast.makeText(getActivity(), R.string.empty_fields, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (stringRa.length() > 8) {
            Toast.makeText(getActivity(), getString(R.string.ra_size_wrong), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isValid(stringRa)) {
            valid = true;
        }
        return valid;
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
            case R.id.tv_login:
                if (checkCredentials())
                    mMongoDatabase.select(convertToInteger(stringRa), stringPassword);
                break;
            case R.id.tv_forgot_password:
                mHandler.postDelayed(this::inflateFragmentRecoveryPassword, 1500);
                break;
            case R.id.tv_create_account:
                mHandler.postDelayed(this::inflateFragmentCreateAccount, 1500);
                break;
        }
    }

    private void inflateFragmentRecoveryPassword() {
        mFragmentRecovery = FragmentRecoveryPassword.newInstance(requireContext() ,mMongoDatabase,
                isMongoDBConnected);
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().add(R.id.fragment_home, mFragmentRecovery, null)
                .addToBackStack(null)
                .commit();
    }

    private void inflateFragmentCreateAccount() {
        mFragmentCreateAccount = FragmentCreateAccount.newInstance(requireContext(), mMongoDatabase,
                isMongoDBConnected);
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().add(R.id.fragment_home, mFragmentCreateAccount, null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onConnectionResponse(boolean result) {
        Log.d(TAG, "<---Higa---> connected to Database : " + result);
        isMongoDBConnected = result;
    }

    @Override
    public void onSelectResponse(boolean result) {

    }

    @Override
    public void onSelectResponse(boolean result, Students students) {
        if (result) {
            FragmentRoutineDetails.newInstance(requireContext(), mMongoDatabase, students,
                    isMongoDBConnected);
            changeFragment(FragmentRoutineDetails.class);
        }
        else {
            Toast.makeText(requireContext(), getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    private static int convertToInteger(String value) {
        return Integer.parseInt(value);
    }
}
