package com.insuranceline.ui.fragments.more;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Zeki Guler on 24,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class TermsAndCondition extends BaseFragment {

    @Bind(R.id.about_text)
    TextView mAboutText;

    public static TermsAndCondition getInstance() {
        return new TermsAndCondition();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_terms_and_condition_insuranceline, container, false);
        ButterKnife.bind(this, view);
        mAboutText.setText( Html.fromHtml(getString(R.string.t_c)));
        mAboutText. setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Terms & Condition");
        setHomeAsUpEnabled(R.drawable.icon_back);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

