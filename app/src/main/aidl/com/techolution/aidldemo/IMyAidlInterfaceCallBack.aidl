// IMyAidlInterfaceCallBack.aidl
package com.techolution.aidldemo;

interface IMyAidlInterfaceCallBack {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void getPriceData(int id, int price);
}