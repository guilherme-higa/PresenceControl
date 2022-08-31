package com.example.presencecontroltestapp.ui.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.presencecontroltestapp.R;

import java.util.function.Function;

public abstract class BaseFragment<Binding> extends Fragment {
    private final Function<View, Binding> mBindingGenerator;
    private Binding mBinding = null;

    protected BaseFragment(@LayoutRes int contentLayoutId,
                           Function<View, Binding> bindingGenerator) {
        super(contentLayoutId);
        mBindingGenerator = bindingGenerator;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = mBindingGenerator.apply(view);
        onBindCreated(mBinding);
    }

    public abstract void onBindCreated(Binding binding);

    protected Binding getBinding() {
        return mBinding;
    }

    protected void changeFragment(Class<? extends Fragment> fragment) {
        try {
            Fragment fragmentInstance = fragment.newInstance();
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out)
                    .replace(R.id.containerMain, fragmentInstance, null)
                    .addToBackStack(null)
                    .commit();
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            e.printStackTrace();
        }
    }

    protected void popBackStack() {
        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        super.onDestroyView();
    }
}
