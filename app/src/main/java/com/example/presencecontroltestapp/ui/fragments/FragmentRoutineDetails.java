package com.example.presencecontroltestapp.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.database.MongoDatabase;
import com.example.presencecontroltestapp.databinding.FragmentRoutineDetailsBinding;
import com.example.presencecontroltestapp.entities.Students;
import com.example.presencecontroltestapp.provider.IDatabaseResult;
import com.example.presencecontroltestapp.utils.AdapterFiles;
import com.example.presencecontroltestapp.utils.Constants;

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
    private TextView segunda, terca, quarta, quinta, sexta, sabado, domingo;

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
        segunda = binding.segunda;
        terca = binding.terca;
        quarta = binding.quarta;
        quinta = binding.quinta;
        sexta = binding.sexta;
        sabado = binding.sabado;
        domingo = binding.domingo;

        mHandler = getHandler();
        mMongoDatabase.selectClassInformation(this, mStudents.getCredentialsRa(), Constants.defaultDate);

        binding.btnMenu.setOnClickListener(this::showPopupMenu);
        binding.segunda.setOnClickListener(v -> onClick(binding.segunda));
        binding.terca.setOnClickListener(v -> onClick(binding.terca));
        binding.quarta.setOnClickListener(v -> onClick(binding.quarta));
        binding.quinta.setOnClickListener(v -> onClick(binding.quinta));
        binding.sexta.setOnClickListener(v -> onClick(binding.sexta));
        binding.sabado.setOnClickListener(v -> onClick(binding.sabado));
        binding.domingo.setOnClickListener(v -> onClick(binding.domingo));

        mDefaultBackPressedCallback = getDefaultOnBackPressed();
        setBackPressedCallback(mDefaultBackPressedCallback);

        mHandler.postDelayed(() -> {
            mFragmentHome.getActivity().runOnUiThread(() -> {
                populateFilesList();
            });
        }, 2000);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.segunda:
                mMongoDatabase.selectClassInformation(this, mStudents.getCredentialsRa(), Constants.defaultDate);
                mHandler.postDelayed(() -> {
                    mFragmentHome.getActivity().runOnUiThread(() -> {
                        populateFilesList();
                    });
                }, 1500);
                break;
            case R.id.terca:
                mMongoDatabase.selectClassInformation(this, mStudents.getCredentialsRa(), Constants.terca);
                mHandler.postDelayed(() -> {
                    mFragmentHome.getActivity().runOnUiThread(() -> {
                        populateFilesList();
                    });
                }, 1500);
                break;
            case R.id.quarta:
                mMongoDatabase.selectClassInformation(this, mStudents.getCredentialsRa(), Constants.quarta);
                mHandler.postDelayed(() -> {
                    mFragmentHome.getActivity().runOnUiThread(() -> {
                        populateFilesList();
                    });
                }, 1500);
                break;
            case R.id.quinta:
                mMongoDatabase.selectClassInformation(this, mStudents.getCredentialsRa(), Constants.quinta);
                mHandler.postDelayed(() -> {
                    mFragmentHome.getActivity().runOnUiThread(() -> {
                        populateFilesList();
                    });
                }, 1500);
                break;
            case R.id.sexta:
                mMongoDatabase.selectClassInformation(this, mStudents.getCredentialsRa(), Constants.sexta);
                mHandler.postDelayed(() -> {
                    mFragmentHome.getActivity().runOnUiThread(() -> {
                        populateFilesList();
                    });
                }, 1500);
                break;
            case R.id.sabado:
                mMongoDatabase.selectClassInformation(this, mStudents.getCredentialsRa(), Constants.sabado);
                mHandler.postDelayed(() -> {
                    mFragmentHome.getActivity().runOnUiThread(() -> {
                        populateFilesList();
                    });
                }, 1500);
                break;
            case R.id.domingo:
                mMongoDatabase.selectClassInformation(this, mStudents.getCredentialsRa(), Constants.domingo);
                mHandler.postDelayed(() -> {
                    mFragmentHome.getActivity().runOnUiThread(() -> {
                        populateFilesList();
                    });
                }, 1500);
                break;
        }
    }

    private void switchTextViewColor(TextView textView){
        if (textView.getCurrentTextColor() == -16711423 || textView.getCurrentTextColor() == Color.BLACK) {
            textView.setTextColor(Color.RED);
        } else {
            textView.setTextColor(Color.BLACK);
        }
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
            } else {
                getBinding().rvImdfs.setVisibility(View.GONE);
                getBinding().clClNoItem.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(requireActivity(), getString(R.string.warning_pres_back_again),
                    Toast.LENGTH_SHORT).show();
        }
        mClassInformation = new ArrayList<>();
    }

    @Override
    public void onClassInformationSelect(boolean result) {

    }
}
