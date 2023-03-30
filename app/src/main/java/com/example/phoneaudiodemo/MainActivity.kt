package com.example.phoneaudiodemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.collection.SparseArrayCompat
import androidx.collection.forEach
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import com.example.lib2_api.Lib2Api
import com.example.lib_api.LibApi
import com.example.mylibrary.LibApiImpl
import com.example.mylibrary2.Lib2ApiImpl

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController.apply {
            //根据lib的navigation生成NavGraph
            val graphLib = NavInflater(this@MainActivity, this.navigatorProvider).inflate(com.example.mylibrary.R.navigation.nav_lib)
            LibApi.getInstance().inject(LibApiImpl())

            val graphLib2 = NavInflater(this@MainActivity, this.navigatorProvider).inflate(com.example.mylibrary2.R.navigation.nav_lib2)
            Lib2Api.getInstance().inject(Lib2ApiImpl())

            //根据app的navigation生成NavGraph
            this.graph = NavInflater(this@MainActivity, this.navigatorProvider).inflate(R.navigation.nav_app).apply {
                injectGraph(this, graphLib)
                injectGraph(this, graphLib2)
                startDestination = com.example.mylibrary.R.id.libFragment
            }
        }
    }

    private fun injectGraph(graphMain: NavGraph, graphLib: NavGraph) {
        graphMain.apply {
            //将lib的所有action合并到app中
            reflectAction(graphLib, this)
            //将lib的所有destination合并到app中
            val iterator = graphLib.iterator()
            while (iterator.hasNext()) {
                val node = iterator.next()
                iterator.remove()
                this.addDestination(node)
            }
        }
    }

    private fun reflectAction(fromGraph: NavGraph, toGraph: NavGraph) {
        val fromActions = fromGraph.javaClass.superclass.getDeclaredField("mActions").apply { this.isAccessible = true }.get(fromGraph) as SparseArrayCompat<NavAction>
        var actions = toGraph.javaClass.superclass.getDeclaredField("mActions").apply { this.isAccessible = true }.get(toGraph)
        if (actions == null) {
            actions = fromActions
        } else {
            actions as SparseArrayCompat<NavAction>
            fromActions.forEach { key, value ->
                actions.append(key, value)
            }
        }
        toGraph.javaClass.superclass.getDeclaredField("mActions").apply { this.isAccessible = true }.set(toGraph, actions)
    }
}