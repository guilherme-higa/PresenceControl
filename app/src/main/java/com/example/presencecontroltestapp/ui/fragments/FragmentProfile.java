package com.example.presencecontroltestapp.ui.fragments;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import androidx.fragment.app.FragmentManager;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.database.MongoDatabase;
import com.example.presencecontroltestapp.databinding.FragmentProfileBinding;
import com.example.presencecontroltestapp.entities.Students;
import com.example.presencecontroltestapp.provider.IDatabaseResult;

public class FragmentProfile extends BaseFragment<FragmentProfileBinding>  implements IDatabaseResult {
    private static final String TAG = FragmentProfile.class.getSimpleName();

    private static Context mContext;
    private static MongoDatabase mMongoDatabase;
    private static Students mStudents;

    public static FragmentProfile newInstance(Context context, MongoDatabase mongoDatabase, Students students) {
        mContext = context;
        mMongoDatabase = mongoDatabase;
        mStudents = students;
        return new FragmentProfile();
    }

    @Override
    public void onBindCreated(FragmentProfileBinding binding) {
        binding.edRa.setText(String.valueOf(mStudents.getCredentialsRa()));
        binding.edEmail.setText(mStudents.getEmail());
        binding.edName.setText(mStudents.getName());
        
        binding.btnBack.setOnClickListener(v -> onClick(binding.btnBack));
    }

    private void onClick(ImageButton btnBack) {
        switch (btnBack.getId()) {
            case R.id.btnBack:
                hideKeyboard();
                popBackStack();
                break;
        }
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

    public FragmentProfile() { super(R.layout.fragment_profile, FragmentProfileBinding::bind); }


    @Override
    public void onConnectionResponse(boolean result) {

    }

    @Override
    public void onSelectResponse(boolean result) {

    }

    @Override
    public void onSelectResponse(boolean result, Students students) {

    }
}
