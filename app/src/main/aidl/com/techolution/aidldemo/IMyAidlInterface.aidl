// IMyAidlInterface.aidl
package com.techolution.aidldemo;

// Declare any non-default types here with import statements
import com.techolution.aidldemo.IMyAidlInterfaceCallBack;


interface IMyAidlInterface {

    boolean registerCallBack(in IMyAidlInterfaceCallBack callBack);

    boolean unRegisterCallBack();

    int getPid();

}