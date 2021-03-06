package com.sdattg.vip.pay;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sdattg.vip.R;
import com.sdattg.vip.pay.PayTabViewpager;

import java.util.List;

/**
 * Created by yinqm on 2018/4/24.
 */

public abstract class PayBaseTablayoutFtagment extends PayBaseTitleBarFragment {
    protected abstract List<PayBaseFragment> getFragmentList();
    protected abstract List<String> getFragmentTitleList();
    private List<String> titles;
    private List<PayBaseFragment> fragmentList;
    private PayTabViewpager adapter;
    private ViewPager viewpager;
    private TabLayout tabLayout;
    @Override
    public int bindLayout() {
        return R.layout.lib_tablayout;
    }


    @Override
    protected void initView(View view) {
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
    }

    @Override
    protected void initData() {
        titles = getFragmentTitleList();
        fragmentList = getFragmentList();
        adapter = new PayTabViewpager(getChildFragmentManager(), getActivity(), titles, fragmentList);
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
    }


}
