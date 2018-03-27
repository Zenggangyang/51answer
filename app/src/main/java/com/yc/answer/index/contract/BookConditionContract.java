package com.yc.answer.index.contract;


import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/3/12 08:59.
 */

public interface BookConditionContract {

    interface View extends IView, ILoading, INoData, INoNet, IHide {
        void showConditionList(List<String> data);
    }

    interface Presenter extends IPresenter {
    }
}
