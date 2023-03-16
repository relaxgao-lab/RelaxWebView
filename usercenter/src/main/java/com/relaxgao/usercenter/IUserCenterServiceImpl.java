package com.relaxgao.usercenter;

import android.content.Intent;

import com.google.auto.service.AutoService;
import com.relaxgao.base.BaseApplication;
import com.relaxgao.common.autoservice.IUserCenterService;

@AutoService({IUserCenterService.class})
public class IUserCenterServiceImpl implements IUserCenterService {
    @Override
    public boolean isLogined() {
        return false;
    }

    @Override
    public void login() {
        Intent intent = new Intent(BaseApplication.Companion.getSApplication(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.Companion.getSApplication().startActivity(intent);
    }
}
