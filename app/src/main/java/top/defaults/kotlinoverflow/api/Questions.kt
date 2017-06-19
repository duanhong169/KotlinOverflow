package top.defaults.kotlinoverflow.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import top.defaults.kotlinoverflow.model.QuestionList
import top.defaults.kotlinoverflow.util.DEF_PAGE_SIZE

interface Questions {

    @GET("questions")
    fun questions(@Query("page") page: Int,
                  @Query("pagesize") pageSize: Int = DEF_PAGE_SIZE,
                  @Query("tagged") tagged: String = "kotlin",
                  @Query("order") order: String = "desc",
                  @Query("sort") sort: String = "activity",
                  @Query("filter") filter: String = "!6JW7LsM8VJgxM"): Observable<QuestionList>

    @GET("questions/{question_id}")
    fun question(@Path("question_id") id: Int,
                  @Query("filter") filter: String = "!-MQ9xUObayihy__dEmWyMV-jO0Py.E(i9"): Observable<QuestionList>
}