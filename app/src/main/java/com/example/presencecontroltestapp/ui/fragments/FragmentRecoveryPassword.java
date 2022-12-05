package com.example.presencecontroltestapp.ui.fragments;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.database.MongoDatabase;
import com.example.presencecontroltestapp.databinding.FragmentRecoveryPasswordBinding;
import com.example.presencecontroltestapp.provider.IDatabaseResult;

public class FragmentRecoveryPassword extends BaseFragment<FragmentRecoveryPasswordBinding>
        implements IDatabaseResult.Recovery {

    public static final String TAG = FragmentRecoveryPassword.class.getSimpleName();

    private EditText edRa, edEmail, edPassword;
    private static MongoDatabase mMongoDatabase;
    private static boolean isConnected = false;
    private static Context mContext;

    public FragmentRecoveryPassword() {
        super(R.layout.fragment_recovery_password, FragmentRecoveryPasswordBinding::bind); }


    public static FragmentRecoveryPassword newInstance(Context context, MongoDatabase mongoDatabase,
                                                       boolean connected) {
        mContext = context;
        mMongoDatabase= mongoDatabase;
        isConnected = connected;
        return new FragmentRecoveryPassword();
    };

    @Override
    public void onBindCreated(FragmentRecoveryPasswordBinding binding) {
        edRa = binding.edRa;
        edEmail = binding.edEmail;
        edPassword = binding.edPassword;
        binding.btnCreateAccount.setOnClickListener(v -> onClick(binding.btnCreateAccount));
        binding.btnCleanFields.setOnClickListener(v -> onClick(binding.btnCleanFields));

        setBackPressedCallback();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateAccount:
                if (isConnected) {
                    if (checkRaCredentials(edRa) && checkEmailCredentials(edEmail)) {
                        mMongoDatabase.selectRecovery(this, convertToInteger(
                                edRa.getText().toString()), edEmail.getText().toString());
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.empty_fields),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), getString(R.string.no_connection_with_db),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCleanFields:
                cleanEtFields();
                break;
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

    private boolean checkRaCredentials(EditText editText) {
        boolean valid = false;
        String string  = editText.getText().toString();

        if (TextUtils.isEmpty(string)) {
            Toast.makeText(getActivity(), R.string.empty_fields, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (string.length() > 8) {
            Toast.makeText(getActivity(), getString(R.string.ra_size_wrong), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isValid(string)) {
            valid = true;
        }
        return valid;
    }

    private boolean checkEmailCredentials(EditText editText) {
        boolean valid = true;
        String string = editText.getText().toString();

        if (TextUtils.isEmpty(string)) {
            Toast.makeText(getActivity(), R.string.empty_fields, Toast.LENGTH_SHORT).show();
            valid = false;
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
        edRa.setText(R.string.empty);
        edEmail.setText(R.string.empty);
        edPassword.setText(R.string.empty);
        edRa.requestFocus();
    }

    private static int convertToInteger(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public void onRecoverySelect(boolean result) {
        Log.d(TAG, "<---Higa---> [onRecoverySelect] : " + result);
        if (result) {
            if (checkEmailCredentials(edPassword)) {
                mMongoDatabase.update(convertToInteger(edRa.getText().toString()),
                        edPassword.getText().toString());
            }
        } else {
            Toast.makeText(mContext, getString(R.string.user_already_added_to_db),
                    Toast.LENGTH_SHORT).show();
        }
        cleanEtFields();
    }
}
