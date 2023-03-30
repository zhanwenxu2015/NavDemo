package com.example.lib2_api

import androidx.navigation.NavDirections

/**
 *  @Author : zhanwenxu
 *  @Date : 2023/3/27 16:54
 *  @Description :
 */
open class Lib2Api {

    companion object {
        private var instance: Lib2Api? = null
        fun getInstance(): Lib2Api {
            if (instance == null) {
                instance = Lib2Api()
            }
            return instance!!
        }
    }

    private var mBase: Lib2Api? = null
    fun inject(impl: Lib2Api) {
        mBase = impl
    }

    open fun getLib2Fragment1(): NavDirections? {
        return mBase?.getLib2Fragment1()
    }

    open fun getLib2Fragment2(): NavDirections? {
        return mBase?.getLib2Fragment2()
    }

}