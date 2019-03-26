package com.test.sixpro.dailog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.test.sixpro.R;
import com.test.sixpro.utils.StatusBarUtil;


public class LoadingDialog extends Dialog {
    private boolean mCanCancled;// 点击banck键是否能取消loading
    private Context mContext;

    public LoadingDialog(Context context) {
        super(context, R.style.Dialog);
        mContext = context;
        init();
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        init();
        // TODO Auto-generated constructor stub
    }

    public void setCanceled(boolean canCanceled) {
        mCanCancled = canCanceled;
    }

    protected LoadingDialog(Context context, int theme) {
        super(context, R.style.Dialog);
        mContext = context;
        init();
        // TODO Auto-generated constructor stub
    }

    private void init() {
        setCancelable(true);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.loading_view, null);

        setContentView(layout);
        CircularProgressView circularProgressView = findViewById(R.id.progress_view);
        circularProgressView.startAnimation();

        StatusBarUtil.immersive(getWindow());
//        RotateAnimation rotationAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        rotationAnim.setInterpolator(new LinearInterpolator());
//        rotationAnim.setRepeatCount(Animation.INFINITE);
//        rotationAnim.setDuration(720);
//        findViewById(R.id.loading_circle).startAnimation(rotationAnim);
    }

    /**
     * 抓住，防止在show的时候依附对象已被销毁
     */
    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (mCanCancled) {
            dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event);
        }

//        Activity a = ActivityTracker.getAT().getPossibleTop();
//        if (a != null) {
//            Log.d("dj", "in finish");
//            a.finish();
//        }
        return super.onKeyDown(keyCode, event);
    }

}
