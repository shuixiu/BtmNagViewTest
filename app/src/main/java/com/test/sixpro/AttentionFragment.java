package com.test.sixpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2019/2/18.
 */

public class AttentionFragment extends Fragment {


    private View v;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment, null);
            fragment = v.findViewById(R.id.fragment);
            fragment.setText("AttentionFragment");
            Log.d("wwn", "AttentionFragment");

            // init view  init data
        }
        return v;
    }
}
