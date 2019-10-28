package com.moaplanet.gosing.common.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public abstract class BaseMoaFragment<BINDING> extends Fragment {

    public abstract int layoutRes();
    public abstract void initBindViewModel();

    private ViewDataBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.binding =  DataBindingUtil.inflate(inflater, layoutRes(), container, false);

        initBindViewModel();
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    public BINDING getBinding() {
        return (BINDING)binding;
    }

}
