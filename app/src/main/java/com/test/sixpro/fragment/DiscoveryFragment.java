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

/**
 * Created by Administrator on 2019/2/18.
 */

public class DiscoveryFragment extends Fragment {
    private TextView fragment;
    private View v;

    public static DiscoveryFragment newInstance(String title) {
        DiscoveryFragment di = new DiscoveryFragment();
        return di;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(v==null){
            v = inflater.inflate(R.layout.fragment, null);
            fragment = v.findViewById(R.id.fragment);

            fragment.setText("DiscoveryFragment");
        }
        Log.d("wwn","DiscoveryFragment");
        return v;
    }
}
