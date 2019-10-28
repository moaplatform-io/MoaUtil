package com.moaplanet.gosing.common.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.chaos.view.PinView;
import com.moaplanet.gosing.R;
import com.moaplanet.gosing.common.view.CommonTitle;
import com.moaplanet.gosing.constants.GoSingConstants;
import com.orhanobut.logger.Logger;

import java.util.Objects;

/**
 * 비밀번호 입력화면
 */
public class PasswordInputFragment extends BaseFragment {

    public static String BUNDLE_REQUEST_FROM_VIEW = "BUNDLE_REQUEST_FROM_VIEW";
    public static String BUNDLE_REQUEST_FROM_VIEW_JOIN = "BUNDLE_REQUEST_FROM_VIEW_JOIN";
    public static String BUNDLE_REQUEST_FROM_VIEW_ACCOUNT_REGISTER = "BUNDLE_REQUEST_FROM_VIEW_ACCOUNT_REGISTER";

    private PinView passwordPinView;
    private TextView tvPasswordError, tvPasswordInputTitle;
    private TextView tvExplanation;
    private String viewType;
    private CommonTitle commonTitle;

    @Override
    public int layoutRes() {
        return R.layout.fragment_password_input;
    }

    @Override
    public void initDefaultData(Bundle savedInstanceState) {

    }

    @Override
    public void initLayout(View view) {

        commonTitle = view.findViewById(R.id.common_password_input_toolbar);
        commonTitle.setTitle(getString(R.string.fragment_password_input_toolbar_sign_up_title));

        passwordPinView = view.findViewById(R.id.pinview_fragment_password_input);
        visibleKeyboard(passwordPinView);

        tvPasswordError = view.findViewById(R.id.tv_fragment_password_input_validation);
        tvPasswordError.setVisibility(View.INVISIBLE);
        tvPasswordInputTitle = view.findViewById(R.id.tv_fragment_password_input_title);

        tvExplanation = view.findViewById(R.id.tv_fragment_password_input_explanation);

        checkPasswordViewType();

    }

    @Override
    public void initListener() {
        commonTitle.setOnClickListener(view -> Navigation.findNavController(view).popBackStack());
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 6) {
                // passwordPinView 뷰에 패스워드 6개가 사용자에게 다 입력됬다는것을 보여주기 위함
                new Handler().postDelayed(() -> checkPassword(editable.toString()), 100);
            }
        }
    };

    private void checkPasswordViewType() {
        if (getArguments() != null) {
            viewType = getArguments().getString(GoSingConstants.BUNDLE_KEY_TYPE_PASSWORD);

            if (viewType == null) {
                Logger.e("viewType 값이 null 입니다.");
            } else if (viewType.equals(GoSingConstants.BUNDLE_VALUE_NEW_PASSWORD)) {
                initCreatePasswordLayout();
            } else {
                initCheckPasswordLayout();
            }

        } else {
            Logger.e("getArguments 값이 null 입니다.");
        }
    }

    private void initCreatePasswordLayout() {
        tvPasswordInputTitle.setText(
                getString(R.string.fragment_password_input_new_payment_password)
        );
        tvExplanation.setText(getString(R.string.fragment_password_input_add_password_explanation));
    }

    private void initCheckPasswordLayout() {
        passwordPinView.setText("");
        tvPasswordInputTitle.setText("결제 비밀번호 확인");
        tvExplanation.setText(getString(R.string.fragment_password_input_again_payment_password));
    }

    private void checkPassword(String password) {
        if (viewType.equals(GoSingConstants.BUNDLE_VALUE_NEW_PASSWORD)) {
            moveCheckPasswordFragment(password);
        } else {
            if (getArguments() != null) {
                String beforePw = getArguments()
                        .getString(GoSingConstants.BUNDLE_KEY_BEFORE_INPUT_PASSWORD);

                if (beforePw == null) {
                    Logger.e("이전에 입력한 비밀번호가 null 입니다.");
                } else if (beforePw.equals(password)) {
                    tvPasswordError.setVisibility(View.INVISIBLE);
                    Navigation.findNavController(view).navigate(
                            R.id.action_fragment_sign_up_completed
                    );
                } else {
                    tvPasswordError.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    private void moveCheckPasswordFragment(String password) {
        Bundle bundle = new Bundle();
        bundle.putString(GoSingConstants.BUNDLE_KEY_TYPE_PASSWORD, GoSingConstants.BUNDLE_VALUE_CHECK_PASSWORD);
        bundle.putString(GoSingConstants.BUNDLE_KEY_BEFORE_INPUT_PASSWORD, password);
        setArguments(bundle);
        checkPasswordViewType();
    }

    private void visibleKeyboard(View view) {
        if (view instanceof EditText) {
            view.requestFocus();
            new Handler().postDelayed(() -> {
                //키보드 올리기
                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(imm).showSoftInput(view, 0);
            }, 200);
        }
    }

    private void goneKeyboard(View view) {
        if (view instanceof EditText) {
            view.clearFocus();
            //키보드 내리기
            InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        passwordPinView.addTextChangedListener(watcher);
    }

    @Override
    public void onPause() {
        passwordPinView.removeTextChangedListener(watcher);
        goneKeyboard(passwordPinView);
        super.onPause();
    }

}
