# NavDemo

前置知识(需了解): 

1、(多模块项目的导航最佳做法）https://developer.android.com/guide/navigation/navigation-multi-module?hl=zh-cn

2、(Navigation结构)https://zhuanlan.zhihu.com/p/510412392 

3、（注解路由）https://juejin.cn/post/6844904131577004039

目的：找出合适组件化中Navigation的改造方式。少改动原始逻辑前提下能够多模块跳转Fragment。



第一步：确认跨模块Fragment是否可以使用Navigation来管理
方案一：官方建议的DeepLink方式。
方案二：注解路由方式。
方案三：合并navigation.xml方式。
   方案三验证：

   准备：建立app和 lib，lib为依赖module，分别在app和lib建立nav_app和nav_lib两个navigation文件

   目标：在app的fragment中通过lib生成的NavLibDirections通过nav_lib声明的action跳转打开lib的Fragment。

   执行：app的主NavGraph动态创建，将lib的NavGraph对象所有destination和action都转移到app的NavGraph中；相当于将lib的nav内容平移到app的nav。


![4](https://user-images.githubusercontent.com/12827988/228741330-06a5f392-71c3-4409-ae87-e82c215a38dc.png)

![1 (1)](https://user-images.githubusercontent.com/12827988/228725544-9f275ab6-7907-4227-8d36-c1129bec8251.png)


在app中通过lib的NavLibDirections的action打开lib的Fragment。

![3](https://user-images.githubusercontent.com/12827988/228725569-7845704b-c213-4b62-b842-0b86e0990932.png)

大致说明：动态设置NavGraph→graphA，将module的graph所有的destination和action合并到graphA。Navigation使用Safe Args控制的跳转类和方法就可正常在graphA中使用。



==================2023/3/27======================

组件化方案探索1: 

背景：不使用ARouter + 工程单一Activity + 各组件使用Navigation管理Fragment

结构：

![11](https://user-images.githubusercontent.com/12827988/228725586-6ca60e8d-fb38-4a48-a712-d2b9bfb8a885.png)

逻辑点说明：

1、app壳需要整合所有module的navigation布局元素 + 指定首页Fragment的id + 各个组件需要执行自己的Api模块注入实现类动作。具体如下：

           val graphLib = NavInflater(this@MainActivity, this.navigatorProvider).inflate(com.example.mylibrary.R.navigation.nav_lib)
           //组件1执行自己的Api模块注入实现类动作
           LibApi.getInstance().inject(LibApiImpl())
           //组件2执行自己的Api模块注入实现类动作
           val graphLib2 = NavInflater(this@MainActivity, this.navigatorProvider).inflate(com.example.mylibrary2.R.navigation.nav_lib2)
           Lib2Api.getInstance().inject(Lib2ApiImpl())
 
           this.graph = NavInflater(this@MainActivity, this.navigatorProvider).inflate(R.navigation.nav_app).apply {
                //整合navigation布局元素(包括所有NavDirections和Action)
               injectGraph(this, graphLib)
               injectGraph(this, graphLib2)
 
               //指定首页Fragment的id
               startDestination = com.example.mylibrary.R.id.libFragment
           }
//module1组件1的对外层LibApi（位于单独module，如上图的"Api Login"）
open class LibApi {
 
    companion object {  。。。  }
 
    private var mBase: LibApi? = null
    fun inject(impl: LibApi) {
        mBase = impl
    }
 
    //组件1对外提供的Fragment1界面的跳转
    open fun getLib1Fragment1(): NavDirections? {
        //具体的跳转目的对象由注入的mBase实现类提供
        return mBase?.getLib1Fragment1()
    }
 
    open fun getLib1Fragment2(): NavDirections? {
        return mBase?.getLib1Fragment2()
    }
 
}
 
//组件1对于对外层的具体实现类(也就是主要在app中执行注入的类，即mBase的具体实现)
class LibApiImpl: LibApi() {
    override fun getLib1Fragment1(): NavDirections? {
        return NavLibDirections.actionToLibFragment()
    }
 
    override fun getLib1Fragment2(): NavDirections? {
        return NavLibDirections.actionLibFragmentToLib2Fragment(fromLib = true)
    }
}
