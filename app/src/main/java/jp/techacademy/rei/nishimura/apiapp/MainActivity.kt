package jp.techacademy.rei.nishimura.apiapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), FragmentCallback {

    private val viewPagerAdapter by lazy { ViewPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchView = toolbar.menu.findItem(R.id.menu_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            //文字入力が確定したときに送られてくる　決定ボタンを押したりしたとき
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.v("nullpo_q", "${query}")
                (viewPagerAdapter.flagments[VIEW_PAGER_POSITION_API] as ApiFragment).updateDate(false, query!!)

                //★★★ ソフトキーボードを隠す。
                val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)


                //相変わらずよくわからない返値　たぶんfalseで元の設定されたイベントを行う trueでしない
                return true
            }
            //文字が入力されるたびに入力文字列が送られてくる　部分一致のサジェスト機能などな利用できる
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_info -> {
                    ReviewDialog().show(supportFragmentManager,"")
                    true

                }
                else ->{true}

            }
        }




        // ViewPager2の初期化
        viewPager2.apply {
            adapter = viewPagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL // スワイプの向きを横
            offscreenPageLimit = viewPagerAdapter.itemCount // ViewPager2で保持する画面数
        }

        // TabLayoutの初期化
        // TabLayoutとViewPager2を紐づける
        // TabLayoutのTextを指定する
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setText(viewPagerAdapter.titleIds[position])
        }.attach()
    }

    override fun onClickItem(shop: Shop) {
        WebViewActivity.start(this, shop)
    }

    override fun onAddFavorite(shop: Shop) { // Favoriteに追加するときのメソッド(Fragment -> Activity へ通知する)
        FavoriteShop.insert(FavoriteShop().apply {
            id = shop.id
            name = shop.name
            address = shop.address
            imageUrl = shop.logoImage
            url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
        })
        (viewPagerAdapter.flagments[VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateDate()
    }

    override fun onDeleteFavorite(id: String) { // Favoriteから削除するときのメソッド(Fragment -> Activity へ通知する)
        showConfirmDeleteFavoriteDialog(id)
    }

    private fun showConfirmDeleteFavoriteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) {_, _ ->
                deleteFavorite(id)
            }
            .setNegativeButton(android.R.string.cancel) {_, _ ->}
            .create()
            .show()
    }

    private fun deleteFavorite(id: String) {
        FavoriteShop.delete(id)
        (viewPagerAdapter.flagments[VIEW_PAGER_POSITION_API] as ApiFragment).updateView()
        (viewPagerAdapter.flagments[VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateDate()
    }

    companion object {
        private const val VIEW_PAGER_POSITION_API = 0
        private const val VIEW_PAGER_POSITION_FAVORITE = 1
    }

    override fun onRestart() {
        super.onRestart()
        (viewPagerAdapter.flagments[VIEW_PAGER_POSITION_API] as ApiFragment).updateView()
        (viewPagerAdapter.flagments[VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateDate()

    }
}