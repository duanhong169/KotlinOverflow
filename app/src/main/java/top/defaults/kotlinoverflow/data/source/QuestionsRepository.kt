package top.defaults.kotlinoverflow.data.source

import io.reactivex.Observable
import top.defaults.kotlinoverflow.data.param.QuestionQueryParams
import top.defaults.kotlinoverflow.data.param.QuestionsQueryParams
import top.defaults.kotlinoverflow.data.resp.QuestionList

interface QuestionsRepository {

    fun questions(params: QuestionsQueryParams): Observable<QuestionList>

    fun question(params: QuestionQueryParams): Observable<QuestionList>

}