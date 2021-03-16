package jp.techacademy.rei.nishimura.apiapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    companion object {
        private const val KEY_SHOP = "Key_shop"
        fun start(activity: Activity, shop: Shop) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_SHOP, shop))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val data = intent.getSerializableExtra(KEY_SHOP) as Shop
        webView.loadUrl(data.couponUrls.sp)
        // お気に入り状態を取得
        val isFavorite = FavoriteShop.findBy(data.id) !=null
        fab.setImageResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border)
        // 白抜きの星マークの画像を指定
        fab.apply {
            setOnClickListener {
                if (isFavorite) {
                    onDeleteFavorite(data.id)
                } else {
                    onAddFavorite(data)
                    setImageResource(R.drawable.ic_star)
                }
            }
        }
     }

    fun onAddFavorite(shop: Shop) { // Favoriteに追加するときのメソッド(Fragment -> Activity へ通知する)
        FavoriteShop.insert(FavoriteShop().apply {
            id = shop.id
            name = shop.name
            address = shop.address
            imageUrl = shop.logoImage
            url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
        })
    }

    fun onDeleteFavorite(id: String) { // Favoriteから削除するときのメソッド(Fragment -> Activity へ通知する)
        showConfirmDeleteFavoriteDialog(id)
    }

    private fun showConfirmDeleteFavoriteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) {_, _ ->
                deleteFavorite(id)
                fab.setImageResource(R.drawable.ic_star_border)

            }
            .setNegativeButton(android.R.string.cancel) {_, _ ->}
            .create()
            .show()
    }

    private fun deleteFavorite(id: String) {
        FavoriteShop.delete(id)
    }

}