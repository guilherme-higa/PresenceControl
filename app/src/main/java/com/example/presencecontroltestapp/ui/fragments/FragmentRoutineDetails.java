package com.example.presencecontroltestapp.ui.fragments;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.databinding.FragmentRoutineDetailsBinding;

public class FragmentRoutineDetails extends BaseFragment<FragmentRoutineDetailsBinding>
        implements PopupMenu.OnMenuItemClickListener, View.OnClickListener {
    public static final String TAG = FragmentRoutineDetails.class.getSimpleName();

    public FragmentRoutineDetails() { super(R.layout.fragment_routine_details, FragmentRoutineDetailsBinding::bind); }

    @Override
    public void onBindCreated(FragmentRoutineDetailsBinding binding) {
        binding.btnMenu.setOnClickListener(v -> showPopupMenu(v));
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
                changeFragment(FragmentHome.class);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View view) {

    }
}
