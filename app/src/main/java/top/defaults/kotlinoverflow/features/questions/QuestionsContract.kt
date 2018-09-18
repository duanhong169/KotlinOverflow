package top.defaults.kotlinoverflow.features.questions

import top.defaults.kotlinoverflow.common.recyclerview.RecyclerViewContract
import top.defaults.kotlinoverflow.data.param.QuestionsQueryParams
import top.defaults.kotlinoverflow.data.resp.QuestionList

interface QuestionsContract {

    interface View : RecyclerViewContract.View<QuestionList> {

        fun getParams(): QuestionsQueryParams
    }

}