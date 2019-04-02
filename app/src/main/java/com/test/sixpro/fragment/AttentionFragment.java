package com.test.sixpro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.sixpro.R;
import com.test.sixpro.base.BaseFragment;
import com.test.sixpro.ui.attion.CallMsMActivity;
import com.test.sixpro.ui.attion.universal.FuctionManager;
import com.test.sixpro.ui.attion.universal.FunctionNoParamNoResult;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/2/18.
 */

public class AttentionFragment extends BaseFragment {

    private View view;
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
    public void onStart() {
        super.onStart();

        FuctionManager.getInstance().addFunction(new FunctionNoParamNoResult("111111") {
            @Override
            public void function() {
                Log.d("wwn", "FuctionManager-----111111");
            }
        });

        FuctionManager.getInstance().addFunction(new FunctionNoParamNoResult("222222") {
            @Override
            public void function() {
                Log.d("wwn", "FuctionManager-----222222");
            }
        });
        FuctionManager.getInstance().addFunction(new FunctionNoParamNoResult("333333") {
            @Override
            public void function() {
                Log.d("wwn", "FuctionManager-----333333");
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_attion, null);
        ButterKnife.bind(this,view);
        fragment = view.findViewById(R.id.fragment);
        fragment.setText("AttentionFragment");
        initTitleView();
        return view;
    }
    private void initTitleView() {
        View view_title = view.findViewById(R.id.in_title);
        TextView title_font = view_title.findViewById(R.id.title_font);
        title_font.setText("设置");
    }

    @OnClick(R.id.bt_call_msm)
    void callMsM(View view){



        startActivity(new Intent(context,CallMsMActivity.class));

    }


    @Override
    protected void initData() {

        Log.d("wwn", "AttentionFragment2222222");
    }
}
