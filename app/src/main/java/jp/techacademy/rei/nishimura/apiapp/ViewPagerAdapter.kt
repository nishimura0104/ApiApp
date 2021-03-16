package jp.techacademy.rei.nishimura.apiapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    val titleIds = listOf(R.string.tab_title_api, R.string.tab_title_favorite)

    val flagments = listOf(ApiFragment(), FavoriteFragment())

    override fun getItemCount(): Int {
        return flagments.size
    }

    override fun createFragment(position: Int): Fragment {
        return flagments[position]
    }
}