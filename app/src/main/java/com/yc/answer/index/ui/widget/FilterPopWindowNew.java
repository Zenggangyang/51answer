package com.yc.answer.index.ui.widget;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.index.model.bean.VersionDetailInfo;
import com.yc.answer.index.model.bean.VersionInfo;
import com.yc.answer.index.ui.activity.SearchActivity;
import com.yc.answer.index.ui.adapter.FilterItemDetailAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BasePopwindow;
import yc.com.base.CommonInfoHelper;


/**
 * Created by wanglin  on 2018/3/8 15:53.
 */

public class FilterPopWindowNew extends BasePopwindow {


    private String mFlag;
    @BindView(R.id.subject_recyclerView)
    RecyclerView subjectRecyclerView;


    private FilterItemDetailAdapter subjectFilterItemAdapter;

    private VersionDetailInfo subjectDetailInfo;
    private String simple_flag = "";

    public FilterPopWindowNew(Activity context, String flag) {
        super(context);
        this.mFlag = flag;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_filter_view;
    }

    @Override
    public void init() {

        setOutsideTouchable(true);

        subjectRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        CommonInfoHelper.getO(mContext, SpConstant.INDEX_VERSION, new TypeReference<VersionInfo>() {
        }.getType(), new CommonInfoHelper.onParseListener<VersionInfo>() {


            @Override
            public void onParse(VersionInfo versionInfo) {
                List<VersionDetailInfo> subjectList = null;
                if (TextUtils.equals(mContext.getString(R.string.grade), mFlag)) {
                    subjectList = createNewList(versionInfo.getGrade());
                    simple_flag = "grade";
                } else if (TextUtils.equals(mContext.getString(R.string.subject), mFlag)) {
                    subjectList = createNewList(versionInfo.getSubject());
                    simple_flag = "subject";
                } else if (TextUtils.equals(mContext.getString(R.string.part), mFlag)) {
                    subjectList = createNewList(versionInfo.getPart_type());
                    simple_flag = "part";
                } else if (TextUtils.equals(mContext.getString(R.string.version), mFlag)) {
                    subjectList = createNewList(versionInfo.getVersion());
                    simple_flag = "version";
                }

                if (subjectList != null)
                    subjectDetailInfo = subjectList.get(0);

                subjectFilterItemAdapter = new FilterItemDetailAdapter(subjectList, simple_flag);

                subjectRecyclerView.setAdapter(subjectFilterItemAdapter);

                subjectRecyclerView.addItemDecoration(new MyDecoration(10));
                subjectFilterItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        subjectFilterItemAdapter.onClick(position);
                        subjectDetailInfo = subjectFilterItemAdapter.getItem(position);
                        RxSPTool.putString(mContext, simple_flag, subjectDetailInfo.getName());
                        if (listener != null) {
                            listener.onPopClick(subjectDetailInfo.getName());
                        }

                    }
                });
            }

            @Override
            public void onFail(String json) {

            }
        });


    }

    @Override
    public int getAnimationID() {
        return 0;
    }

    private List<VersionDetailInfo> createNewList(List<VersionDetailInfo> oldList) {

        if (oldList != null) {
            if (oldList.size() > 0 && !TextUtils.equals(mContext.getString(R.string.all), oldList.get(0).getName()))
                oldList.add(0, new VersionDetailInfo("", mContext.getString(R.string.all)));
        }
        return oldList;
    }


    private OnPopClickListener listener;

    public void setOnPopClickListener(OnPopClickListener listener) {
        this.listener = listener;
    }

    public interface OnPopClickListener {
        void onPopClick(String name);
    }

}
