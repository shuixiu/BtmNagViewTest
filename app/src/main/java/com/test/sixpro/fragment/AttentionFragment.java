package com.test.sixpro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.sixpro.R;
import com.test.sixpro.base.BaseFragment;

/**
 * Created by Administrator on 2019/2/18.
 */

public class AttentionFragment extends BaseFragment {


    private TextView fragment;
    private String mFrom;
    private static AttentionFragment at = null;
    public static AttentionFragment newInstance(String from) {

        if(at==null){
            at = new AttentionFragment();
            Bundle bundle = new Bundle();
            bundle.putString("from", from);
            at.setArguments(bundle);
        }
        return at;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFrom = getArguments().getString("from");
            Log.d("wwn", mFrom + "--------");
        }
    }
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, null);

        fragment = view.findViewById(R.id.fragment);

        fragment.setText("AttentionFragment");


        Log.d("wwn", "AttentionFragment1111111");
        return view;
    }

    @Override
    protected void initData() {

        Log.d("wwn", "AttentionFragment2222222");
    }
}
