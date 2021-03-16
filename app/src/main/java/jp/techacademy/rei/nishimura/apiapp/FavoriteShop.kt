package jp.techacademy.rei.nishimura.apiapp

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class FavoriteShop: RealmObject() {

    @PrimaryKey
    var id: String = ""
    var imageUrl: String = ""
    var name: String = ""
    var address: String = ""
    var url: String = ""
    var deleteFlag: Boolean = false

    companion object {
        fun findAll(): List<FavoriteShop> = // お気に入りのshopを全件取得
            Realm.getDefaultInstance().use { realm ->
                realm.where(FavoriteShop::class.java)
                    .equalTo("deleteFlag", false).findAll()
                    .let {
                        realm.copyFromRealm(it)
                    }
            }

        fun findBy(id: String): FavoriteShop? = // お気に入りされているshopをidとdeleteFlagの真偽で検索して返す。お気に入り登録されていなければnullで返す
            Realm.getDefaultInstance().use { realm ->
                realm.where(FavoriteShop::class.java)
                    .equalTo(FavoriteShop::id.name, id)
                    .equalTo("deleteFlag", false)
                    .findFirst()?.let {
                        realm.copyFromRealm(it)
                    }
            }

        fun insert(favoriteShop: FavoriteShop) = // お気に入り追加
            Realm.getDefaultInstance().executeTransaction {
                it.insertOrUpdate(favoriteShop)
            }

        fun delete(id: String) = // idでお気に入りから削除する
            Realm.getDefaultInstance().use { realm ->
                realm.where(FavoriteShop::class.java)
                    .equalTo(FavoriteShop::id.name, id)
                    .findFirst()?.also { deleteShop ->
                        realm.executeTransaction {
                            deleteShop.deleteFlag = true
                            it.insertOrUpdate(deleteShop)
                        }
                    }
            }

    }
}