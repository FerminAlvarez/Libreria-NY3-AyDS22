package ayds.newyork.songinfo.moredetails.model.repository.external.nyt

import ayds.newyork.songinfo.moredetails.model.repository.external.nyt.article.NytArticleServiceImpl

object NytInjector {

    val nytArticleService: NytArticleService = NytArticleServiceImpl()
}