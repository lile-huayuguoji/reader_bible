package com.sdattg.vip.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.sdattg.vip.R;
import com.sdattg.vip.adapter.Tab01ProductAdapter;
import com.sdattg.vip.base.BaseFragment;
import com.sdattg.vip.base.BaseTablayoutFtagment3;
import com.sdattg.vip.bean.CategoryBean;
import com.sdattg.vip.search.MyCategoryDBHelper;
import com.sdattg.vip.util.FileUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinqm on 2018/6/26.
 */
public class Main02FragmentBible extends BaseTablayoutFtagment3 {
    public static int book_count_jiuyue = 0;
    public static int book_count_xinyue = 0;

    public static List<String> list_item;
    public static List<CategoryBean> list_jiuyue;
    public static List<String> list_jiuyue_str;
    public static  List<CategoryBean> list_xinyue;
    public static  List<String> list_xinyue_str;

    Main02FragmentList main02FragmentList_0 = new Main02FragmentList();
    Main02FragmentList main02FragmentList_1 = new Main02FragmentList();
    Main02FragmentList main02FragmentList_2 = new Main02FragmentList();
   /* Log.d("Main02FragmentList", "list_item.size():" + list_item.size());
    adapter = new Tab01ProductAdapter(R.layout.recy_item, list_item, getActivity());
    rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);*/
    @Override
    protected List<BaseFragment> getFragmentList() {
        List<BaseFragment> list = new ArrayList<>();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("title", 0);
        main02FragmentList_0.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putInt("title", 1);
        main02FragmentList_1.setArguments(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putInt("title", 2);
        main02FragmentList_2.setArguments(bundle3);

        list.add(main02FragmentList_0);
        list.add(main02FragmentList_1);
        list.add(main02FragmentList_2);
        return list;
    }

    @Override
    protected List<String> getFragmentTitleList() {
        initMyDatas();

        List<String> list = new ArrayList<>();
        list.add("全部");
        list.add("旧约");
        list.add("新约");
        /*main02FragmentList_1.initView();
        main02FragmentList_1.adapter = new Tab01ProductAdapter(R.layout.recy_item, list_item, getActivity());
        main02FragmentList_1.rv = main02FragmentList_1.view2.findViewById(R.id.rv);
        main02FragmentList_1.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        main02FragmentList_1.rv.setAdapter(main02FragmentList_1.adapter);


        main02FragmentList_2.getLayoutId();
        main02FragmentList_2.adapter = new Tab01ProductAdapter(R.layout.recy_item, list_jiuyue_str, getActivity());
        main02FragmentList_2.rv = main02FragmentList_2.view2.findViewById(R.id.rv);
        main02FragmentList_2.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        main02FragmentList_2.rv.setAdapter(main02FragmentList_2.adapter);


        main02FragmentList_3.getLayoutId();
        main02FragmentList_3.adapter = new Tab01ProductAdapter(R.layout.recy_item, list_xinyue_str, getActivity());
        main02FragmentList_3.rv = main02FragmentList_3.view2.findViewById(R.id.rv);
        main02FragmentList_3.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        main02FragmentList_3.rv.setAdapter(main02FragmentList_3.adapter);*/
        return list;
    }

    private void initMyDatas(){

        list_item = new ArrayList<String>();
        list_jiuyue_str = new ArrayList<String>();
        list_xinyue_str = new ArrayList<String>();

        MyCategoryDBHelper myCategoryDBHelper = new MyCategoryDBHelper(getContext());
        list_jiuyue = myCategoryDBHelper.getCategory(FileUtil.replaceBy_("01-旧约"), null);
        for (CategoryBean categoryBean:
                list_jiuyue) {
            BookBeanOnShuKu bean = new BookBeanOnShuKu();
            bean.author = "作者:摩西";
            bean.title = categoryBean.categoryName.substring(categoryBean.categoryName.indexOf("_") + 1);
            List<CategoryBean> books_jiuyue = myCategoryDBHelper.getCategory(FileUtil.replaceBy_(categoryBean.categoryName), "jieshao");
            if(books_jiuyue.size() > 0){
                bean.jieshao = books_jiuyue.get(0).categoryJieshao;
                Log.d("Tab01Product", "bean.jieshao:" + bean.jieshao);
            }else{
                bean.jieshao = "无介绍";
            }
            list_jiuyue_str.add(bean.toString());
            list_item.add(bean.toString());
            Log.d("GuideActivity", "categoryInsertOrder:" + categoryBean.categoryInsertOrder + ", categoryPath:" + categoryBean.categoryPath + ", categoryNam:" + categoryBean.categoryName);
        }

        book_count_jiuyue = list_jiuyue.size();

        list_xinyue = myCategoryDBHelper.getCategory(FileUtil.replaceBy_("02-新约"), null);
        for (CategoryBean categoryBean:
                list_xinyue) {
            BookBeanOnShuKu bean = new BookBeanOnShuKu();
            bean.title = "";
            bean.title = categoryBean.categoryName.substring(categoryBean.categoryName.indexOf("_") + 1);
            bean.author = "作者:摩西";
            List<CategoryBean> books_xinyue = myCategoryDBHelper.getCategory(FileUtil.replaceBy_(categoryBean.categoryName), "jieshao");
            if(books_xinyue.size() > 0){
                bean.jieshao = books_xinyue.get(0).categoryJieshao;
                Log.d("Tab01Product", "bean.jieshao:" + bean.jieshao);
            }else{
                bean.jieshao = "无介绍";
            }
            list_xinyue_str.add(bean.toString());
            list_item.add(bean.toString());
            Log.d("GuideActivity", "categoryInsertOrder:" + categoryBean.categoryInsertOrder + ", categoryPath:" + categoryBean.categoryPath + ", categoryNam:" + categoryBean.categoryName);
        }

        book_count_xinyue = list_xinyue.size();
    }

    private class BookBeanOnShuKu{
        public String title;
        public String author;
        public String jieshao;

        @Override
        public String toString() {

            return title + "#3#" + author + "#3#" + jieshao;
        }
    }

    @Override
    public String setTitle() {
        return null;
    }

    @Override
    protected void iniLogic() {
        setTabLine(tabLayout,20,20);
    }

    @Override
    protected void httpRequest() {

    }


    public void setTabLine(TabLayout tab,int left,int right){
        try {
            Class<?> tablayout = tab.getClass();
            Field tabStrip = tablayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            LinearLayout ll_tab= (LinearLayout) tabStrip.get(tabLayout);
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                child.setPadding(0,0,0,0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);
                //修改两个tab的间距
                params.setMarginStart(dip2px(getActivity(),left));
                params.setMarginEnd(dip2px(getActivity(),right));
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp==dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
