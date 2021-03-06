package com.sdattg.vip.search;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sdattg.vip.GuideActivity;
import com.sdattg.vip.R;
import com.sdattg.vip.base.BaseActivity;
import com.sdattg.vip.bean.InitDatas;
import com.sdattg.vip.bean.NewBookBean;
import com.sdattg.vip.bean.NewChapterBean;
import com.sdattg.vip.bean.NewShowChapterBean;
import com.sdattg.vip.tool.ZipTool;
import com.sdattg.vip.util.FileUtil;
import com.sdattg.vip.util.SharePreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;


/**
 * Created by yinqm on 2018/7/5.
 */
public class SerachActivity extends BaseActivity {
    private ImageView iv_back;
    private TextView tv_serach, tv_sactivity_selected;
    private TextView tv_sactivity_shengjing, tv_sactivity_huaizhu, tv_sactivity_qita, tv_sactivity_yingwen;
    public static int category_selected = 0;
    public static String category_selected_strs = "";

    public static String category_selected_str1 = "01-圣经";
    public static String category_selected_str2 = "全部";
    public static String category_selected_str3 = "";

    LinearLayout ll_waitselected;
    LinearLayout ll_readytosearch;

    private EditText et_scontent;
    public static int SEARCH_KIND = 3; //0-书籍搜索  1-标题搜索  2-目录搜索  3-内容搜索
    public static String SEARCH_CONTENT = ""; //0-书籍搜索  1-标题搜索  2-目录搜索  3-内容搜索
    public static Set<String> shistroys;
    private RadioGroup rg_searchkind;
    private RadioButton rb_book, rb_title, rb_index, rb_content;

    TextView tv_sactivity_searching_title;
    ListView lv_sactivity_searching_results;

    static Handler updateUIHandler;

    private MySearchResultBookAdapter adapter_book;
    private List<String> results_book;
    private List<NewBookBean> indexs;

    private AlertDialog bookUpdatingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.serach_activity;
    }

    @Override
    protected void addViewLayout() {

    }

    @Override
    protected void initView() {


        ll_waitselected = findViewById(R.id.ll_waitselected);
        ll_readytosearch = findViewById(R.id.ll_readytosearch);

        tv_sactivity_searching_title = findViewById(R.id.tv_sactivity_searching_title);
        lv_sactivity_searching_results = findViewById(R.id.lv_sactivity_searching_results);


        tv_sactivity_shengjing = findViewById(R.id.tv_sactivity_shengjing);
        tv_sactivity_huaizhu = findViewById(R.id.tv_sactivity_huaizhu);
        tv_sactivity_qita = findViewById(R.id.tv_sactivity_qita);
        tv_sactivity_yingwen = findViewById(R.id.tv_sactivity_yingwen);

        tv_sactivity_shengjing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_selected = 0;
                iniLogic();
            }
        });
        tv_sactivity_huaizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_selected = 1;
                iniLogic();
            }
        });
        tv_sactivity_qita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_selected = 2;
                iniLogic();
            }
        });
        tv_sactivity_yingwen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_selected = 3;
                iniLogic();
            }
        });

        iv_back = findViewById(R.id.iv_back);
        tv_serach = findViewById(R.id.tv_serach);
        et_scontent = findViewById(R.id.et_scontent);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tv_serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReadyToSearch();
                if (TextUtils.isEmpty(et_scontent.getText())) {
                    Toast.makeText(SerachActivity.this, "搜索内容为空", Toast.LENGTH_LONG).show();
                } else {
                    SEARCH_CONTENT = et_scontent.getText().toString().trim();
                    if (TextUtils.isEmpty(SEARCH_CONTENT)) {
                        Toast.makeText(SerachActivity.this, "搜索内容为空", Toast.LENGTH_LONG).show();
                    } else {

                        //Serach2Fragment  serachFragment = new Serach2Fragment();
                        //replaceFragment2(serachFragment,R.id.fl);
                        tv_sactivity_searching_title.setText("请稍等，正在搜索...");
                        String search_content = et_scontent.getText().toString().trim();
                        Log.d("findbug071717", "into 1");
                        if (SEARCH_KIND != 0) {
                            Log.d("findbug071717", "InitDatas.hasAllDone:" + InitDatas.hasAllDone);
                            Log.d("findbug071717", "into 2");
                            if (!InitDatas.hasAllDone) {
                                Log.d("findbug071717", "into 3");
                                showUpdateAllBookDialog();
                                UpdatingBooksThread updatingBooksThread = UpdatingBooksThread.getSingleInstance(SerachActivity.this);
                                updatingBooksThread.start();
                                return;
                            }
                        }
                        Log.d("findbug071717", "into 4");
                        SearchingThread searchingThread = new SearchingThread(SerachActivity.this, search_content, category_selected_str1, category_selected_str2, category_selected_str3);
                        searchingThread.start();
                    }
                }

            }
        });

        rg_searchkind = (RadioGroup) findViewById(R.id.rg_searchkind);
        /*rb_book = (RadioButton) findViewById(R.id.rb_book);
        rb_title = (RadioButton) findViewById(R.id.rb_title);
        rb_index = (RadioButton) findViewById(R.id.rb_index);
        rb_content = (RadioButton) findViewById(R.id.rb_content);*/
        rg_searchkind.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String msg = "";
                switch (checkedId) {
                    case R.id.rb_book:
                        msg = "书籍搜索";
                        SerachActivity.SEARCH_KIND = 0;

                        break;
                    case R.id.rb_title:
                        msg = "标题搜索";
                        SerachActivity.SEARCH_KIND = 1;
                        break;
                    case R.id.rb_index:
                        msg = "目录搜索";
                        SerachActivity.SEARCH_KIND = 2;
                        break;
                    case R.id.rb_content:
                        msg = "内容搜索";
                        SerachActivity.SEARCH_KIND = 3;
                        break;

                }
                ll_waitselected.setVisibility(View.VISIBLE);
                ll_readytosearch.setVisibility(View.INVISIBLE);
                if (SerachActivity.updateUIHandler != null) {
                    SerachActivity.updateUIHandler.sendEmptyMessage(9);
                }
                clearSearchResultLL();

                /*if(rb_book.getId()==checkedId){
                    msg = "当前选中的性别为:"+male.getText().toString();
                }
                if(female.getId()==checkedId){
                    msg = "当前选中的性别为:"+female.getText().toString();
                }*/
                //Toast.makeText(getApplication(),"SerachActivity.SEARCH_KIND:" + SerachActivity.SEARCH_KIND,Toast.LENGTH_SHORT).show();
            }
        });


        et_scontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_waitselected.setVisibility(View.INVISIBLE);
                ll_readytosearch.setVisibility(View.VISIBLE);
                //readyToSearch();
            }
        });

        tv_sactivity_selected = findViewById(R.id.tv_sactivity_selected);


        updateUIHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        results_book = (List<String>) msg.obj;
                        if (lv_sactivity_searching_results != null) {
                            adapter_book = new MySearchResultBookAdapter(SerachActivity.this, results_book);
                            lv_sactivity_searching_results.setAdapter(adapter_book);
                        }
                        //更新 tv_sactivity_searching_title => text:""
                        if (tv_sactivity_searching_title != null) {
                            tv_sactivity_searching_title.setText("搜索出：" + results_book.size() + "个结果：");
                        }
                        break;
                    case 1:
                        List<NewShowChapterBean> all_titles = (List<NewShowChapterBean>) msg.obj;
                        if (lv_sactivity_searching_results != null) {
                            MySearchResultContentAdapter adapter_chapter = new MySearchResultContentAdapter(SerachActivity.this, all_titles);
                            lv_sactivity_searching_results.setAdapter(adapter_chapter);
                        }
                        //更新 tv_sactivity_searching_title => text:""
                        if (tv_sactivity_searching_title != null) {
                            tv_sactivity_searching_title.setText("搜索出：" + all_titles.size() + "个结果：");
                        }
                        //titles search


                        break;
                    case 2:
                        indexs = (List<NewBookBean>) msg.obj;
                        if (lv_sactivity_searching_results != null) {
                            MySearchResultIndexAdapter adapter_index = new MySearchResultIndexAdapter(SerachActivity.this, indexs);
                            lv_sactivity_searching_results.setAdapter(adapter_index);
                        }
                        //更新 tv_sactivity_searching_title => text:""
                        if (tv_sactivity_searching_title != null) {
                            tv_sactivity_searching_title.setText("搜索出：" + indexs.size() + "个结果：");
                        }
                        break;
                    case 3:
                        List<NewShowChapterBean> all_chapters = (List<NewShowChapterBean>) msg.obj;
                        if (lv_sactivity_searching_results != null) {
                            MySearchResultContentAdapter adapter_chapter = new MySearchResultContentAdapter(SerachActivity.this, all_chapters);
                            lv_sactivity_searching_results.setAdapter(adapter_chapter);
                        }
                        //更新 tv_sactivity_searching_title => text:""
                        if (tv_sactivity_searching_title != null) {
                            tv_sactivity_searching_title.setText("搜索出：" + all_chapters.size() + "个结果：");
                        }
                        break;
                    case 9:
                        //更新 tv_sactivity_searching_title => text:""
                        if (tv_sactivity_searching_title != null) {
                            tv_sactivity_searching_title.setText("搜索结果如下：");
                        }
                        break;
                    case 99:
                        //更新完毕，取消更新弹窗
                        if(bookUpdatingDialog != null){
                            bookUpdatingDialog.cancel();
                        }
                        Toast.makeText(SerachActivity.this, "更新完毕，请开始搜索吧", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    private void clearSearchResultLL() {
        /*if(results_book != null){
            results_book = new ArrayList<String>();
        }
        if(adapter_book != null){
            adapter_book.notifyDataSetChanged();
        }*/
        if (lv_sactivity_searching_results != null) {
            lv_sactivity_searching_results.setAdapter(new MySearchResultBookAdapter(SerachActivity.this, new ArrayList<String>()));
        }
    }

    private boolean checkHasAllDone() {
        //InitDatas.hasAllDone
        return false;
    }

    /**
     * timer = new Timer();
     * timer.schedule(new TimerTask() {
     *
     * @Override public void run() {
     * runOnUiThread(new Runnable() {
     * @Override public void run() {
     * bnp.incrementProgressBy(1);
     * }
     * });
     * }
     * }, 1000, 100);
     */
    private void showUpdateAllBookDialog() {
        bookUpdatingDialog = new AlertDialog.Builder(this)
                .setTitle("提示：")
                .setMessage("书籍还未完全初始化，请稍等：")
                .create();
        bookUpdatingDialog.show();
        bookUpdatingDialog.setCancelable(false);
        bookUpdatingDialog.setCanceledOnTouchOutside(false);
        bookUpdatingDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    private void readyToSearch() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void iniLogic() {
        switch (category_selected) {
            case 0:
                category_selected_str1 = "01-圣经";

                tv_sactivity_shengjing.setTextColor(getResources().getColor(R.color.blue));
                tv_sactivity_huaizhu.setTextColor(getResources().getColor(R.color.text_333));
                tv_sactivity_qita.setTextColor(getResources().getColor(R.color.text_333));
                tv_sactivity_yingwen.setTextColor(getResources().getColor(R.color.text_333));

                SerachFragmentX serachFragment_X0 = new SerachFragmentX();
                replaceFragment(serachFragment_X0, R.id.fl);
                break;
            case 1:
                category_selected_str1 = "02-怀著";

                tv_sactivity_shengjing.setTextColor(getResources().getColor(R.color.text_333));
                tv_sactivity_huaizhu.setTextColor(getResources().getColor(R.color.blue));
                tv_sactivity_qita.setTextColor(getResources().getColor(R.color.text_333));
                tv_sactivity_yingwen.setTextColor(getResources().getColor(R.color.text_333));

                SerachFragmentX serachFragment_X1 = new SerachFragmentX();
                replaceFragment(serachFragment_X1, R.id.fl);
                break;
            case 2:
                category_selected_str1 = "其他";

                tv_sactivity_shengjing.setTextColor(getResources().getColor(R.color.text_333));
                tv_sactivity_huaizhu.setTextColor(getResources().getColor(R.color.text_333));
                tv_sactivity_qita.setTextColor(getResources().getColor(R.color.blue));
                tv_sactivity_yingwen.setTextColor(getResources().getColor(R.color.text_333));

                SerachFragmentX serachFragment_X2 = new SerachFragmentX();
                replaceFragment(serachFragment_X2, R.id.fl);
                break;
            case 3:
                category_selected_str1 = "英文";

                tv_sactivity_shengjing.setTextColor(getResources().getColor(R.color.text_333));
                tv_sactivity_huaizhu.setTextColor(getResources().getColor(R.color.text_333));
                tv_sactivity_qita.setTextColor(getResources().getColor(R.color.text_333));
                tv_sactivity_yingwen.setTextColor(getResources().getColor(R.color.blue));

                tv_sactivity_yingwen.setTextColor(getResources().getColor(R.color.blue));
                SerachFragmentX serachFragment_X3 = new SerachFragmentX();
                replaceFragment(serachFragment_X3, R.id.fl);
                break;
        }

        updateSelected();

    }

    public void updateSelected() {
        category_selected_strs = FileUtil.replaceBy_(category_selected_str1).substring(FileUtil.replaceBy_(category_selected_str1).indexOf("_") + 1)
                + "-" + FileUtil.replaceBy_(category_selected_str2).substring(FileUtil.replaceBy_(category_selected_str2).indexOf("_") + 1)
                + "-" + "《" + FileUtil.replaceBy_(category_selected_str3).substring(FileUtil.replaceBy_(category_selected_str3).indexOf("_") + 1) + "》";
        tv_sactivity_selected.setText("已选中：" + category_selected_strs);
    }

    public void showReadyToSearch() {
        ll_waitselected.setVisibility(View.INVISIBLE);
        ll_readytosearch.setVisibility(View.VISIBLE);
        tv_sactivity_searching_title.setText("请输入搜索内容");
    }

    public void showWaitToSelected() {
        ll_readytosearch.setVisibility(View.INVISIBLE);
        ll_waitselected.setVisibility(View.VISIBLE);
        tv_sactivity_searching_title.setText("请输入搜索内容");
    }

    @Override
    protected void httpRequest() {

    }
}
