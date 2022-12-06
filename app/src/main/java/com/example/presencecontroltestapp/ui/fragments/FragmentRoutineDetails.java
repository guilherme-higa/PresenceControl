package com.example.presencecontroltestapp.ui.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.database.MongoDatabase;
import com.example.presencecontroltestapp.databinding.FragmentRoutineDetailsBinding;
import com.example.presencecontroltestapp.entities.Students;
import com.example.presencecontroltestapp.provider.IDatabaseResult;
import com.example.presencecontroltestapp.utils.AdapterFiles;

import java.util.ArrayList;
import java.util.List;

public class FragmentRoutineDetails extends BaseFragment<FragmentRoutineDetailsBinding>
        implements PopupMenu.OnMenuItemClickListener, View.OnClickListener, IDatabaseResult.informationClass {

    public static final String TAG = FragmentRoutineDetails.class.getSimpleName();

    private static final int DOUBLE_PRESS_INTERVAL = 2000;

    private boolean mDoubleBackPressed = false;
    private static Context mContext;
    private static Students mStudents;
    private static MongoDatabase mMongoDatabase;
    private static boolean mFound = false;
    private List<ClassInformation> mClassInformation = new ArrayList<>();
    private static FragmentHome mFragmentHome;
    private Handler mHandler;
    private RecyclerView mView;
    private LinearLayout mLinear;

    private OnBackPressedCallback mDefaultBackPressedCallback;

    public FragmentRoutineDetails() { super(R.layout.fragment_routine_details, FragmentRoutineDetailsBinding::bind); }

    public static FragmentRoutineDetails newInstance(Context context, MongoDatabase mongoDatabase, Students students,
                                                     boolean connected, FragmentHome fragmentHome) {
        mContext = context;
        mMongoDatabase = mongoDatabase;
        mFound = connected;
        mStudents = students;
        mFragmentHome = fragmentHome;
        return new FragmentRoutineDetails();
    }

    @Override
    public void onBindCreated(FragmentRoutineDetailsBinding binding) {
        binding.tvUserName.setText(mStudents.getName());

        mHandler = getHandler();
        mView = binding.rvImdfs;
        mLinear = binding.clClNoItem;

        mMongoDatabase.selectClassInformation(this, mStudents.getCredentialsRa());
        binding.btnMenu.setOnClickListener(v -> showPopupMenu(v));
        mDefaultBackPressedCallback = getDefaultOnBackPressed();
        setBackPressedCallback(mDefaultBackPressedCallback);

        mHandler.postDelayed(() -> {
            mFragmentHome.getActivity().runOnUiThread(() -> {
                populateFilesList();
            });
        }, 2000);
    }

    private Handler getHandler() {
        HandlerThread handlerThread = new HandlerThread(getString(R.string.fragment_routine_thread));
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }


    private void showPopupMenu(View v) {
        Context wrapper = new ContextThemeWrapper(getContext(), R.style.popupMenuStyle);
        PopupMenu popup = new PopupMenu(wrapper, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.home_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
        popup.getMenu();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_qrcode:
                changeFragment(FragmentQRCode.class);
                return true;
            case R.id.menu_aboutApp:
                changeFragment(FragmentAboutApp.class);
                return true;
            case R.id.menu_profile:
                FragmentProfile.newInstance(mContext, mMongoDatabase, mStudents);
                changeFragment(FragmentProfile.class);
                return true;
            default:
                return false;
        }
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

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onClassInformationSelect(boolean result, List<ClassInformation> classInformation) {
        mFound = result;
        mClassInformation = classInformation;
    }

    private void populateFilesList() {
        if (mFound) {
            if (mClassInformation.size() >0) {
                AdapterFiles adapterFiles = new AdapterFiles(mClassInformation);
                getBinding().rvImdfs.setAdapter(adapterFiles);
                getBinding().rvImdfs.setVisibility(View.VISIBLE);
                getBinding().clClNoItem.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(requireActivity(), getString(R.string.warning_pres_back_again),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClassInformationSelect(boolean result) {

    }
}
