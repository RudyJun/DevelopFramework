package com.rudy.framework.module.index.view;

import com.rudy.framework.base.view.IView;
import com.rudy.framework.module.index.service.entity.PhoneQuery;

/**
 * Created by RudyJun on 2016/12/8.
 */

public interface IExamplerView extends IView {

    void getPhoneQueryResult(PhoneQuery queryResult);
}
