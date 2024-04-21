package com.techolution.aidldemo

interface ResponseCallBack {

    suspend fun getPriceData(priceHistoryResponse: PriceHistoryResponse)

}