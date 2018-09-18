package top.defaults.kotlinoverflow.data.source.retrofit

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import top.defaults.kotlinoverflow.data.resp.QuestionList

interface QuestionsApi {

    @GET("questions")
    fun questions(@Query("page") page: Int,
                  @Query("pagesize") pageSize: Int,
                  @Query("tagged") tagged: String,
                  @Query("order") order: String,
                  @Query("sort") sort: String,
                  @Query("filter") filter: String): Observable<QuestionList>

    @GET("questions/{question_id}")
    fun question(@Path("question_id") id: Int,
                  @Query("filter") filter: String): Observable<QuestionList>
}