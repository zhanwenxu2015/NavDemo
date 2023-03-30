package com.example.mylibrary2

import androidx.navigation.NavDirections
import com.example.lib2_api.Lib2Api

/**
 *  @Author : zhanwenxu
 *  @Date : 2023/3/27 17:23
 *  @Description :
 */
class Lib2ApiImpl: Lib2Api() {
    override fun getLib2Fragment1(): NavDirections? {
        return NavLib2Directions.actionToThirdFragment()
    }

    override fun getLib2Fragment2(): NavDirections? {
        return NavLib2Directions.actionThirdFragmentToFourFragment()
    }
}