package com.example.lib_api

import androidx.navigation.NavDirections

/**
 *  @Author : zhanwenxu
 *  @Date : 2023/3/27 16:52
 *  @Description :
 */
open class LibApi {

    companion object {
        private var instance: LibApi? = null
        fun getInstance(): LibApi {
            if (instance == null) {
                instance = LibApi()
            }
            return instance!!
        }
    }

    private var mBase: LibApi? = null
    fun inject(impl: LibApi) {
        mBase = impl
    }

    open fun getLib1Fragment1(): NavDirections? {
        return mBase?.getLib1Fragment1()
    }

    open fun getLib1Fragment2(): NavDirections? {
        return mBase?.getLib1Fragment2()
    }

}