package com.example.mylibrary

import androidx.navigation.NavDirections
import com.example.lib_api.LibApi

/**
 *  @Author : zhanwenxu
 *  @Date : 2023/3/27 17:15
 *  @Description :
 */
class LibApiImpl: LibApi() {
    override fun getLib1Fragment1(): NavDirections? {
        return NavLibDirections.actionToLibFragment()
    }

    override fun getLib1Fragment2(): NavDirections? {
        return NavLibDirections.actionLibFragmentToLib2Fragment(fromLib = true)
    }
}