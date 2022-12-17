package com.example.newsapp.Activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapp.Adapter.NewsAdapter
import com.example.newsapp.Adapter.NewsAdapter.Companion.FAILED
import com.example.newsapp.Adapter.NewsAdapter.Companion.FINISHED
import com.example.newsapp.Adapter.NewsAdapter.Companion.HAS_MORE
import com.example.newsapp.MyApplication
import com.example.newsapp.R
import com.example.newsapp.model.NetWorkLog
import com.example.newsapp.model.News
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.model.isNetworkAvailable
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.litepal.LitePal
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class NewFragment(var newType: String,private var category: String):Fragment(){


    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var  swipeLayout: SwipeRefreshLayout
    private val newsList = ArrayList<News>()
    private val newAdapter = NewsAdapter(newsList,this)
    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_news,container,false)
        swipeLayout = view.findViewById(R.id.swipeLayout)
        newsRecyclerView = view.findViewById(R.id.news_RecyclerView)
        return  view
    }

//    // 刷新新闻，重新发送网络请求
//    @SuppressLint("NotifyDataSetChanged")
//    private fun refresh() {
//        // 网络请求放在主线程里会报错，这里创一个子线程
//        thread {
//            // 天行数据：综合新闻API接口
//            val request = Request.Builder()
//                .url("https://apis.tianapi.com/generalnews/index?key=" + newType+ "82e0e07c02b29cfa42ae2d153c2924e1")
//                .build()
//            val response = OkHttpClient().newCall(request).execute()
//            val json = response.body?.string()
//            val newsResponse = Gson().fromJson(json, NewsResponse::class.java)
//            if (newsResponse?.result != null) {
//                val data = newsResponse.result.data
//                // 先清空再重新塞
//                newsList.clear()
//                newsList.addAll(data)
//                activity?.runOnUiThread {
//                    // 通知newsList发生变化
//                    newsRecyclerView.adapter?.notifyDataSetChanged()
//                }
//            }
//        }
//    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    newsRecyclerView.layoutManager = LinearLayoutManager(MyApplication.context)
    newsRecyclerView.adapter = newAdapter

    // 功能1:创建页面后立即从网络获取新的数据，并刷新到UI上
    loadNewData()

    // 功能2:下拉刷新
    swipeLayout.setColorSchemeColors(Color.parseColor("#03A9F4"))
    swipeLayout.setOnRefreshListener {
        thread {
            Thread.sleep(700) // 这个延迟0.7秒只是为了实现视觉效果，与逻辑无关
            activity?.runOnUiThread {
                loadNewData()
                swipeLayout.isRefreshing = false // 让圆形进度条停下来
            }
        }
    }
        //  功能3:滑动到底部加载更多数据，数据来自本地数据库的缓存
        newsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) return // 不监听向上滑动的动作，只监听向下滑动的动作
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val position = layoutManager.findLastVisibleItemPosition()
                if (position == newAdapter.itemCount - 1) {
                    // 向下滑动到底部时，立即加载缓存数据
                    loadCacheData()
                }
            }
        })
    }
        /**
         * 从网络中加载最新的数据。(如果确实无法联网获取新数据，就拿数据库中最新的缓存数据代替)
         */
        private fun loadNewData() {
            if (isLoading) return
            isLoading = true
            if (isNetworkAvailable(MyApplication.context)) {
                // 如果网络可用,就从网络中获取数据
                writeLog() //记录网络请求日志
                thread {
                    val dataFromNetwork = getDataFromNetwork()
                    if (dataFromNetwork != null && dataFromNetwork.isNotEmpty()) {
                        // 如果成功从网络获取到多条新数据,就立即刷新到UI上
                        activity?.runOnUiThread {
                            replaceDataInRecyclerView(dataFromNetwork)
                            // 将新数据缓存到数据库中,耗时操作单独开一个子线程
                            thread {
                                insertNewsToDataBase()
                            }
                            newAdapter.footerViewStatus = HAS_MORE
                            isLoading = false
                        }
                    } else {
                        // 如果从网络获取到 0条数据，改从本地数据库中获取数据
                        val dataFromDatabase = getDataFromDatabase(6)
                        // 刷新UI
                        activity?.runOnUiThread {
                            replaceDataInRecyclerView(dataFromDatabase)
                            newAdapter.footerViewStatus = HAS_MORE
                            isLoading = false
                        }
                    }
                }
            } else {
                // 如果网络不可用,只能从数据库中获取数据
                thread {
                    val dataFromDatabase = getDataFromDatabase(6)
                    // 刷新UI
                    activity?.runOnUiThread {
                        replaceDataInRecyclerView(dataFromDatabase)
                        newAdapter.footerViewStatus = HAS_MORE
                        isLoading = false
                    }
                }
            }
        }

        /**
         * 从数据库中加载未显示在界面上的旧数据
         */
        fun loadCacheData() {
            if (isLoading) return
            if (newAdapter.footerViewStatus != HAS_MORE) return
            isLoading = true
            thread {
                try {
                    Thread.sleep(1000) // 这个延迟1秒只是为了实现视觉效果，与逻辑无关
                    // 注意在缓存时让越早的新闻,id越小
                    val newData = getDataFromDatabase(6, minIdInNewsList() - 1)
                    if (newData.isEmpty()) {
                        newAdapter.footerViewStatus = FINISHED
                        activity?.runOnUiThread {
                            // 若数据加载完毕，则只更新最后一个列表项(即 footer_view)的UI为结束状态
                            // 这里notify了之后，newsAdapter会执行一遍onBindViewHolder重绘UI
                            newAdapter.notifyItemChanged(newAdapter.itemCount - 1)
                            isLoading = false
                        }
                    } else {
                        // 将旧数据和新数据合并到一个新的list中
                        val list = listOf(newsList, newData).flatten()
                        activity?.runOnUiThread {
                            // 若数据加载成功，则更新整个RecyclerView
                            replaceDataInRecyclerView(list)
                            isLoading = false
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    newAdapter.footerViewStatus = FAILED
                    activity?.runOnUiThread {
                        // 若数据加载失败，则只更新最后一个列表项(即 footer_view)的UI为失败状态
                        // 这里notify了之后，newsAdapter会执行一遍onBindViewHolder重绘UI
                        newAdapter.notifyItemChanged(newAdapter.itemCount - 1)
                        isLoading = false
                    }
                }
            }
        }

        private fun getDataFromNetwork(): List<News>? {
            var list: List<News>? = null
            val request =
                Request.Builder()
                    .url("http://v.juhe.cn/toutiao/index?type=" + newType + "&key=" + MyApplication.KEY)
                    .build()
            try {
                val response = OkHttpClient().newCall(request).execute()// 发送请求
                val json = response.body?.string()
                val newsResponse = Gson().fromJson(json, NewsResponse::class.java)// 将json字符串解析为java对象
                if (newsResponse != null) {
                    when (newsResponse.error_code) {
                        0 -> {
                            try {
                                // 错误码为 0代表成功
                                list = newsResponse.result.data
                            } catch (e: Exception) {
                                // 切换回UI线程(即 主线程) 执行刷新UI的操作
                                activity?.runOnUiThread {
                                    Toast.makeText(activity, "数据获取失败", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        10012, 10013 -> {
                            activity?.runOnUiThread {
                            }
                        }
                        else -> {
                            activity?.runOnUiThread {
                                Toast.makeText(activity, "接口异常", Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 切换回UI线程(即 主线程) 执行刷新UI的操作
                activity?.runOnUiThread {
                    Toast.makeText(activity, "请求失败", Toast.LENGTH_SHORT).show()

                }
            }
            return list
        }

        /**
         * 将数据库中所有category类型且id不超过maxId的所有新闻按id降序排序,从前往后取出最多limitCount条。
         * 如果maxId为负数，则不限制id的最大值
         */
        private fun getDataFromDatabase(limitCount: Int = 6, maxId: Long = -996): List<News> {
            // 由于在保存来自网络的数据时将列表翻转了一次,而插入数据库时id是自增的,因此越旧的新闻 id越小
            return if (maxId < 0) {
                // 小于 0的id是无意义的，不拼接到 SQL中
                // 将数据库中所有category类型的所有新闻按id降序排序,从前往后取出最多limitCount条
                LitePal.where("category=?", category)
                    .order("id desc")
                    .limit(limitCount)
                    .find(News::class.java)
            } else {
                // 将数据库中所有category类型且id不超过maxId的所有新闻按id降序排序,从前往后取出最多limitCount条
                LitePal.where("category=? and id<=?", category, maxId.toString())
                    .order("id desc")
                    .limit(limitCount)
                    .find(News::class.java)
            }
        }

        /**
         * 获取当前newsList中所有新闻中id的最小值(一定是正整数),  如果newsList为空则返回-1
         */
        private fun minIdInNewsList(): Long {
            return if (newsList.isNullOrEmpty()) {
                -1
            } else {
                var minId = newsList[0].id
                for (i in newsList.indices) {
                    val id = newsList[i].id
                    if (id < minId) {
                        minId = id
                    }
                }
                minId
            }
        }

        /**
         * 将 UI当前显示的所有数据(即 newsList中的数据)逐条插入到数据库中
         */
        @Deprecated(message = "这个函数的设计很糟糕,必须优化一下,以后再说")
        private fun insertNewsToDataBase() {
            try {
                // 逆序插入的目的是让越早的新闻 id越小
                for (i in newsList.size - 1 downTo 0) {
                    // 先在数据库中按标题查一遍
                    val news = newsList[i]
                    val resultList = LitePal.where("title=?", news.title).find(News::class.java)
                    if (resultList.isEmpty()) {
                        // 如果本地数据库中没有同一标题的新闻，就执行插入操作
                        news.save()
                    } else {
                        // 如果已经有同一标题的新闻
                        news.id = resultList[0].id
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 切换回UI线程执行刷新UI的操作
                activity?.runOnUiThread {
                    Toast.makeText(activity, "缓存失败", Toast.LENGTH_SHORT).show()

                }
            }
        }

        /**
         * 将发送网络请求这一行为作为日志保存到数据库中
         */
        private fun writeLog() {
            // simpleDateFormat 是线程不安全的，但这里只用于主线程就没问题
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Shanghai")
            val netWorkLog = NetWorkLog(MyApplication.KEY, newType, simpleDateFormat.format(Date()))
            netWorkLog.save()
        }

        /**
         * 刷新UI操作:用 newData 替换掉 RecyclerView中所有的旧数据
         */
        @SuppressLint("NotifyDataSetChanged")
        private fun replaceDataInRecyclerView(newData: List<News>) {
            try {
                newsList.clear()
                newsList.addAll(newData)
                newAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

