package top.defaults.kotlinoverflow.data.source.server

import io.reactivex.Observable
import top.defaults.kotlinoverflow.`object`.Http
import top.defaults.kotlinoverflow.data.param.QuestionQueryParams
import top.defaults.kotlinoverflow.data.param.QuestionsQueryParams
import top.defaults.kotlinoverflow.data.resp.QuestionList
import top.defaults.kotlinoverflow.data.source.QuestionsRepository
import top.defaults.kotlinoverflow.data.source.server.retrofit.QuestionsApi
import top.defaults.kotlinoverflow.util.SingletonHolder

class QuestionsAPIv22Repository: QuestionsRepository {

    companion object : SingletonHolder<QuestionsAPIv22Repository>(::QuestionsAPIv22Repository) {
        val questionsApi = Http.create(QuestionsApi::class.java)
    }

    override fun questions(params: QuestionsQueryParams): Observable<QuestionList> {
        return questionsApi.questions(params.page, params.pageSize, params.tagged, params.order, params.sort, params.filter)
    }

    override fun question(params: QuestionQueryParams): Observable<QuestionList> {
        return questionsApi.question(params.id, params.filter)
    }
}