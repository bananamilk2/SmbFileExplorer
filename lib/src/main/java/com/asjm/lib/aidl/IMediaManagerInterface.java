package com.asjm.lib.aidl;


import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import com.asjm.lib.entity.MediaBean;
import com.asjm.lib.util.ALog;

public interface IMediaManagerInterface extends IInterface {

    abstract class Stub extends Binder implements IMediaManagerInterface {
        private static final String DESCRIPTOR = "  IMediaManagerInterface";

        public Stub() {
            ALog.getInstance().d("Stub: " + this);
            this.attachInterface(this, DESCRIPTOR);
        }

        public static IMediaManagerInterface asInterface(IBinder obj) {
            ALog.getInstance().d("asInterface");
            if ((obj == null)) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof IMediaManagerInterface))) {   //同进程
                ALog.getInstance().d("");
                return ((IMediaManagerInterface) iin);
            }
            Proxy p = new IMediaManagerInterface.Stub.Proxy(obj);
            ALog.getInstance().d(p);
            return p; //跨进程
        }

        @Override
        public IBinder asBinder() {
            ALog.getInstance().d("asBInder");
            return this;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ALog.getInstance().d("onTransact: " + code + " ");
            String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    ALog.getInstance().d("trans");
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_scan: {
                    ALog.getInstance().d("scan");
                    data.enforceInterface(descriptor);
                    this.scan();
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_scanForResult: {
                    data.enforceInterface(descriptor);
                    boolean _result = this.scanForResult();
                    reply.writeNoException();
                    reply.writeInt(((_result) ? (1) : (0)));
                    return true;
                }
                case TRANSACTION_getSize: {
                    ALog.getInstance().d("getSize");
                    data.enforceInterface(descriptor);
                    int _result = this.getSize();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    try {
                        Thread.sleep(21 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ALog.getInstance().d("");
                    return true;
                }
                case TRANSACTION_getMedia: {
                    data.enforceInterface(descriptor);
                    MediaBean _result = this.getMedia();
                    reply.writeNoException();
                    if ((_result != null)) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case TRANSACTION_regist: {
                    ALog.getInstance().d("regist");
                    data.enforceInterface(descriptor);
                    ICallback _arg0;
                    _arg0 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.regist(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_unRegist: {
                    data.enforceInterface(descriptor);
                    ICallback _arg0;
                    _arg0 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.unRegist(_arg0);
                    reply.writeNoException();
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements IMediaManagerInterface {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                ALog.getInstance().d("Proxy");
                mRemote = remote;
            }

            @Override
            public IBinder asBinder() {
                ALog.getInstance().d("asBinder");
                return mRemote;
            }

            @Override
            public void scan() throws RemoteException {
                ALog.getInstance().d("scan");
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_scan, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public boolean scanForResult() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                boolean _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_scanForResult, _data, _reply, 0);
                    _reply.readException();
                    _result = (0 != _reply.readInt());
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public int getSize() throws RemoteException {
                ALog.getInstance().d("getSize");
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_getSize, _data, _reply, 0);
                    _reply.readException();
                    ALog.getInstance().d("");
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public MediaBean getMedia() throws RemoteException {
                ALog.getInstance().d("getMedia");
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                MediaBean _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_getMedia, _data, _reply, 0);
                    _reply.readException();
                    if ((0 != _reply.readInt())) {
                        _result = MediaBean.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void regist(ICallback callback) throws RemoteException {
                ALog.getInstance().d("regist");
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((callback != null)) ? (callback.asBinder()) : (null)));
                    boolean _status = mRemote.transact(Stub.TRANSACTION_regist, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void unRegist(ICallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((callback != null)) ? (callback.asBinder()) : (null)));
                    boolean _status = mRemote.transact(Stub.TRANSACTION_unRegist, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_scan = (IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_scanForResult = (IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_getSize = (IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_getMedia = (IBinder.FIRST_CALL_TRANSACTION + 3);
        static final int TRANSACTION_regist = (IBinder.FIRST_CALL_TRANSACTION + 4);
        static final int TRANSACTION_unRegist = (IBinder.FIRST_CALL_TRANSACTION + 5);
    }

    void scan() throws RemoteException;

    boolean scanForResult() throws RemoteException;

    int getSize() throws RemoteException;

    MediaBean getMedia() throws RemoteException;

    void regist(ICallback callback) throws RemoteException;

    void unRegist(ICallback callback) throws RemoteException;
}
