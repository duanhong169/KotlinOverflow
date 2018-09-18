package top.defaults.kotlinoverflow.features.questions

import io.reactivex.Observable
import top.defaults.kotlinoverflow.common.recyclerview.RecyclerViewPresenter
import top.defaults.kotlinoverflow.data.resp.QuestionList
import top.defaults.kotlinoverflow.data.source.QuestionsRepository

class QuestionsPresenter(private var repository: QuestionsRepository,
                         private var questionsView: QuestionsContract.View)
    : RecyclerViewPresenter<QuestionList>(questionsView) {

    init {
        questionsView.setPresenter(this)
    }

    override fun getObservable(): Observable<QuestionList> {
        return repository.questions(questionsView.getParams())
    }
}