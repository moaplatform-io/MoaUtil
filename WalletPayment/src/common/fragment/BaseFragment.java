package com.moaplanet.gosing.common.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseFragment extends Fragment {
    public View view;
    public CompositeDisposable compositeDisposable;
    public RxPermissions rxPermissions;

    public abstract int layoutRes();
    public abstract void initDefaultData(Bundle savedInstanceState);
    public abstract void initLayout(View view);
    public abstract void initListener();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(layoutRes(), container, false);

        compositeDisposable = new CompositeDisposable();
        rxPermissions = new RxPermissions(this);

        initDefaultData(savedInstanceState);
        initLayout(view);
        initListener();
        return view;
    }


    @Override
    public void onDetach() {
        if(compositeDisposable != null){
            compositeDisposable.clear();
            if(!compositeDisposable.isDisposed()){
                compositeDisposable.dispose();
            }
        }
        super.onDetach();
    }
}
