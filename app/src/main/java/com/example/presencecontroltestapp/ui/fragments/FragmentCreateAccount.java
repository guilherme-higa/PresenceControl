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
import com.example.presencecontroltestapp.databinding.FragmentAddUserBinding;
import com.example.presencecontroltestapp.entities.Students;
import com.example.presencecontroltestapp.provider.IDatabaseResult;

public class FragmentCreateAccount extends BaseFragment<FragmentAddUserBinding> implements IDatabaseResult.CreateAccount {
    public static final String TAG = FragmentCreateAccount.class.getSimpleName();

    private static Context mContext;
    private EditText edRa, edPassword, edName, edEmail;
    private String stringRa, stringPassword, stringName, stringEmail;
    private static MongoDatabase mMongoDatabase;
    private static boolean isConnected = false;

    public FragmentCreateAccount() { super(R.layout.fragment_add_user, FragmentAddUserBinding::bind); }

    public static FragmentCreateAccount newInstance(Context context, MongoDatabase mongoDatabase,
                                                    boolean connected) {
        mContext = context;
        mMongoDatabase = mongoDatabase;
        isConnected = connected;
        return new FragmentCreateAccount();
    }

    @Override
    public void onBindCreated(FragmentAddUserBinding binding) {
        edRa = binding.edRa;
        edPassword = binding.edPassword;
        edName = binding.edName;
        edEmail = binding.edEmail;

        binding.btnCreateAccount.setOnClickListener(v -> onClick(binding.btnCreateAccount));
        setBackPressedCallback();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateAccount:
                if (isConnected && checkCredentials()) {
                    mMongoDatabase.selectCreateAccount(this, convertToInteger(stringRa));
                }
                break;
            case R.id.btnCleanFields:
                cleanEtFields();
                break;
        }
    }

    private boolean checkCredentials() {
        boolean valid = false;
        stringRa = edRa.getText().toString();
        stringEmail = edEmail.getText().toString();
        stringName = edName.getText().toString();
        stringPassword = edPassword.getText().toString();

        if (TextUtils.isEmpty(stringRa) || TextUtils.isEmpty(stringEmail) || TextUtils.isEmpty(stringName) ||
                TextUtils.isEmpty(stringPassword)) {
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
        boolean valid = false;
        try {
            Integer.parseInt(value);
            valid = true;
        } catch (NumberFormatException e) {
            Log.e(TAG, "Failed to parse integer - " + e.getLocalizedMessage());
        }
        return valid;
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

    @Override
    public void onCreateAccountSelect(boolean result) {
        if (result) {
            Toast.makeText(mContext, getString(R.string.user_already_added_to_db), Toast.LENGTH_SHORT).show();
        } else {
            Students students = new Students(stringName, stringEmail, convertToInteger(stringRa), stringPassword);
            mMongoDatabase.insert(students);
        }
        cleanEtFields();
    }

    private void cleanEtFields() {
        edRa.setText(R.string.empty);
        edName.setText(getString(R.string.empty));
        edEmail.setText(R.string.empty);
        edPassword.setText(R.string.empty);
        edRa.requestFocus();
    }

    private static int convertToInteger(String value) {
        return Integer.parseInt(value);
    }
}
