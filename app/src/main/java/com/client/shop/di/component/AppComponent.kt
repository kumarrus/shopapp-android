package com.client.shop.di.component

import com.client.shop.di.module.RepositoryModule
import com.client.shop.di.module.RouterModule
import com.client.shop.di.module.ValidatorModule
import com.client.shop.ui.account.di.AuthComponent
import com.client.shop.ui.account.di.AuthModule
import com.client.shop.ui.blog.di.BlogComponent
import com.client.shop.ui.blog.di.BlogModule
import com.client.shop.ui.cart.di.CartComponent
import com.client.shop.ui.cart.di.CartModule
import com.client.shop.ui.category.di.CategoryComponent
import com.client.shop.ui.category.di.CategoryModule
import com.client.shop.ui.product.di.ProductDetailsComponent
import com.client.shop.ui.product.di.ProductDetailsModule
import com.client.shop.ui.order.di.OrderComponent
import com.client.shop.ui.order.di.OrderModule
import com.client.shop.ui.popular.di.PopularComponent
import com.client.shop.ui.popular.di.PopularModule
import com.client.shop.ui.product.di.ProductListComponent
import com.client.shop.ui.product.di.ProductListModule
import com.client.shop.ui.recent.di.RecentComponent
import com.client.shop.ui.recent.di.RecentModule
import com.client.shop.ui.search.di.SearchComponent
import com.client.shop.ui.search.di.SearchModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RouterModule::class, RepositoryModule::class, ValidatorModule::class])
interface AppComponent {

    fun attachBlogComponent(module: BlogModule): BlogComponent

    fun attachDetailsComponent(module: ProductDetailsModule): ProductDetailsComponent

    fun attachSearchComponent(module: SearchModule): SearchComponent

    fun attachProductComponent(module: ProductListModule): ProductListComponent

    fun attachRecentComponent(module: RecentModule): RecentComponent

    fun attachPopularComponent(module: PopularModule): PopularComponent

    fun attachCategoryComponent(module: CategoryModule): CategoryComponent

    fun attachCartComponent(module: CartModule): CartComponent

    fun attachAuthComponent(module: AuthModule): AuthComponent

    fun attachOrderComponent(module: OrderModule): OrderComponent

}