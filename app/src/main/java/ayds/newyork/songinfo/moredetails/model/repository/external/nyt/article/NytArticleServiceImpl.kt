package ayds.newyork.songinfo.moredetails.model.repository.external.nyt.article

import ayds.newyork.songinfo.moredetails.model.entities.NytArtistInfo
import ayds.newyork.songinfo.moredetails.model.repository.external.nyt.NytArticleService
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


private const val NYT_API_URL = "https://api.nytimes.com/svc/search/v2/"

internal class NytArticleServiceImpl(
    private val nytToArtistInfoResolver: NytToArtistInfoResolver = JsonToArtistInfoResolver()
) : NytArticleService {

    private val nytimesAPI: NYTimesAPI = createRetrofit()

    override fun getArtistInfo(name: String): NytArtistInfo? {
        val callResponse = getArtistInfoFromService(name)
        return nytToArtistInfoResolver.getArtistInfoFromExternalData(callResponse.body(), name)
    }

    private fun createRetrofit(): NYTimesAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(NYT_API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(NYTimesAPI::class.java)
    }
    private fun getArtistInfoFromService(query: String): Response<String?> {
        val response = nytimesAPI.getArtistInfo(query)

        return response?.execute() ?: throw Exception("No Response from api")
    }
}