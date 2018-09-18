package top.defaults.kotlinoverflow.features.question

import io.reactivex.Observable
import top.defaults.kotlinoverflow.common.recyclerview.RecyclerViewPresenter
import top.defaults.kotlinoverflow.data.resp.QuestionList
import top.defaults.kotlinoverflow.data.source.QuestionsRepository

class QuestionPresenter(private var repository: QuestionsRepository,
                        private var questionView: QuestionContract.View)
    : RecyclerViewPresenter<QuestionList>(questionView) {

    init {
        questionView.setPresenter(this)
    }

    override fun getObservable(): Observable<QuestionList> {
        return repository.question(questionView.getParams())
    }
}