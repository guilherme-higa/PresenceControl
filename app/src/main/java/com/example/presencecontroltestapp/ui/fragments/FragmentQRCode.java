package com.example.presencecontroltestapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentManager;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.database.MongoDatabase;
import com.example.presencecontroltestapp.databinding.FragmentQrcodeBinding;
import com.example.presencecontroltestapp.entities.Students;
import com.example.presencecontroltestapp.provider.IDatabaseResult;

public class FragmentQRCode extends BaseFragment<FragmentQrcodeBinding>  implements IDatabaseResult.RecoveryFrequency{
    public static final String TAG = FragmentQRCode.class.getSimpleName();

    private static Context mContext;
    private static MongoDatabase mMongoDatabase;
    private static boolean isConnected = false;
    private static Students mStudents;
    private static ClassInformation mClassInformation;
    private EditText mCodigo, mClassName, mClassDay;
    private Handler mHandler;
    private static FragmentRoutineDetails mFragmentHome;

    public FragmentQRCode() { super(R.layout.fragment_qrcode, FragmentQrcodeBinding::bind); }

    @Override
    public void onBindCreated(FragmentQrcodeBinding binding) {
        mCodigo = binding.edCodigo;
        mClassName = binding.edMateria;
        mClassDay = binding.edDiaMateria;
        mHandler = getHandler();
        binding.btnBack.setOnClickListener(v -> popBackStack());
        binding.btnConfirma.setOnClickListener(v -> onClick(binding.btnConfirma));
        binding.btnCode.setOnClickListener(v -> onClick(binding.btnCode));
        binding.btnQRCode.setOnClickListener(v -> onClick(binding.btnQRCode));
        setBackPressedCallback();
    }

    public static FragmentQRCode newInstance(Context context, MongoDatabase mongoDatabase,
                                             Students students, FragmentRoutineDetails fragmentHome) {
        mContext = context;
        mMongoDatabase = mongoDatabase;
        mStudents = students;
        mFragmentHome = fragmentHome;
        return new FragmentQRCode();
    }

    private android.os.Handler getHandler() {
        HandlerThread handlerThread = new HandlerThread(getString(R.string.presence_control_thread));
        handlerThread.start();
        return new android.os.Handler(handlerThread.getLooper());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnQRCode:
                openCamera();
                break;
            case R.id.btnCode:
                getBinding().constraintLayout1.setVisibility(View.VISIBLE);
                getBinding().constraintLayout2.setVisibility(View.VISIBLE);
                getBinding().constraintLayout3.setVisibility(View.VISIBLE);
                getBinding().btnConfirma.setVisibility(View.VISIBLE);
                break;
            case R.id.btnConfirma:
                if (isCodeCorrect(mCodigo.getText().toString())) {
                    mHandler.postDelayed(() -> {
                        mFragmentHome.getActivity().runOnUiThread(() -> {
                            mMongoDatabase.selectFrequency(this, mStudents.getCredentialsRa(),
                                    mClassName.getText().toString(), mClassDay.getText().toString());
                        });
                    }, 1500);
            } else {
                    Toast.makeText(requireActivity(), getString(R.string.invalid_code),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isCodeCorrect(String codigo) {
        if (codigo == null || codigo.isEmpty() || codigo.length() < 5) return false;
        return true;
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
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

    @Override
    public void onRecoveryFrequency(boolean result, ClassInformation classInformation) {
        if (result) {
            int newFrequency = classInformation.getQtdAulasAssistidas();
            int newClassNumber = classInformation.getQtdAulasDadas();
            newFrequency ++;
            newClassNumber ++;
            mMongoDatabase.findAndUpdateFrequency(mStudents.getCredentialsRa(),
                    classInformation.getDiaDaSemana(), classInformation.getName(), newFrequency, newClassNumber);
        }
    }

    @Override
    public void onRecoveryFrequency(boolean result) {
        Log.d(TAG, "<---Higa---> [onRecoveryFrequency] result  : " + result);
        Toast.makeText(mContext, mContext.getString(R.string.frequency_update_failed), Toast.LENGTH_SHORT).show();
    }
}
