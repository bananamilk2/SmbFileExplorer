package com.asjm.lib.aidl;
import com.asjm.lib.aidl.MediaBean;
import com.asjm.lib.aidl.ICallback;

interface IMediaManagerInterface {

    void scan();

    boolean scanForResult();

    int getSize();

    MediaBean getMedia();

    void regist(ICallback callback);

    void unRegist(ICallback callback);

}
