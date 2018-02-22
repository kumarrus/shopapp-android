package com.shopapp.data.dao

import android.content.Context
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.gateway.entity.Error
import com.shopapp.domain.database.Dao
import com.shopapp.data.dao.adapter.CartProductAdapter
import com.shopapp.data.dao.entity.CartProductData
import com.shopapp.data.dao.entity.CartProductDataEntity
import com.shopapp.data.dao.entity.Models
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import io.requery.sql.TableCreationMode

class DaoImpl(context: Context) : Dao {

    companion object {
        private const val MAX_QUANTITY = 999
    }

    private val store: KotlinReactiveEntityStore<Persistable>

    init {
        val source = DatabaseSource(context, Models.DEFAULT, 1)
        source.setTableCreationMode(TableCreationMode.DROP_CREATE)
        store = KotlinReactiveEntityStore(KotlinEntityDataStore(source.configuration))
    }

    override fun getCartDataList(): Observable<List<CartProduct>> {
        return store.select(CartProductDataEntity::class)
            .get()
            .observableResult()
            .map { it -> it.asIterable() }
            .map {
                val list: MutableList<CartProduct> = mutableListOf()
                it.iterator().forEach { list.add(CartProductAdapter.adaptFromStore(it)) }
                list
            }
    }

    override fun addCartProduct(cartProduct: CartProduct): Single<CartProduct> {
        val productVariant = cartProduct.productVariant
        var storeItem: CartProductData? = store.select(CartProductDataEntity::class)
            .where(CartProductDataEntity.ID.eq(productVariant.id))
            .get()
            .singleOrNull()
        if (storeItem != null) {
            val quantity = storeItem.quantity + cartProduct.quantity
            storeItem.quantity = if (quantity > MAX_QUANTITY) MAX_QUANTITY else quantity
        } else {
            storeItem = CartProductAdapter.adaptToStore(cartProduct)
        }
        return store.upsert(storeItem).map { CartProductAdapter.adaptFromStore(it) }
    }

    override fun deleteProductFromCart(productVariantId: String): Completable {
        val storeItem: CartProductData? = store.select(CartProductDataEntity::class)
            .where(CartProductDataEntity.ID.eq(productVariantId))
            .get()
            .singleOrNull()

        return storeItem?.let { store.delete(storeItem) } ?: Completable.complete()
    }

    override fun deleteAllProductsFromCart(): Completable {
        return store.delete(CartProductDataEntity::class).get().single().toCompletable()
    }

    override fun changeCartProductQuantity(
        productVariantId: String,
        newQuantity: Int
    ): Single<CartProduct> {
        val storeItem: CartProductData? = store.select(CartProductDataEntity::class)
            .where(CartProductDataEntity.ID.eq(productVariantId))
            .get()
            .singleOrNull()

        return storeItem?.let {
            it.quantity = newQuantity
            store.update(storeItem).map { CartProductAdapter.adaptFromStore(it) }
        } ?: Single.error(Error.NonCritical("Product not found"))
    }
}