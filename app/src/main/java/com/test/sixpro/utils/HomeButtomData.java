package com.test.sixpro.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.sixpro.R;
import com.test.sixpro.fragment.AttentionFragment;
import com.test.sixpro.fragment.DiscoveryFragment;
import com.test.sixpro.fragment.HomeFragment;

/**
 * Created by Administrator on 2019/2/20.
 */

public class HomeButtomData {

    public static final int []mTabRes = new int[]{R.drawable.ic_home_black_24dp,
            R.drawable.ic_dashboard_black_24dp,R.drawable.ic_notifications_black_24dp,};
    public static final int []mTabResPressed =
            new int[]{R.drawable.ic_home_black_24dp,R.drawable.ic_dashboard_black_24dp,
                    R.drawable.ic_notifications_black_24dp};
    public static final String []mTabTitle = new String[]{"Home","Discovery","Attention"};

    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[4];
        fragments[0] = HomeFragment.newInstance(from);
        fragments[1] = DiscoveryFragment.newInstance(from);
        fragments[2] = AttentionFragment.newInstance(from);
        return fragments;
    }

    /**
     * 获取Tab 显示的内容
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.tab_content_main,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(HomeButtomData.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }
}
