package ayds.newyork.songinfo.utils

import ayds.newyork.songinfo.utils.navigation.NavigationUtils
import ayds.newyork.songinfo.utils.navigation.NavigationUtilsImpl
import ayds.newyork.songinfo.utils.view.ImageLoader
import ayds.newyork.songinfo.utils.view.ImageLoaderImpl
import com.squareup.picasso.Picasso

object UtilsInjector {

    val imageLoader: ImageLoader = ImageLoaderImpl(Picasso.get())

    val navigationUtils: NavigationUtils = NavigationUtilsImpl()
}