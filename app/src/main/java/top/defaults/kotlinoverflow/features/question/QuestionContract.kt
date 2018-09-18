package top.defaults.kotlinoverflow.features.question

import top.defaults.kotlinoverflow.common.recyclerview.RecyclerViewContract
import top.defaults.kotlinoverflow.data.param.QuestionQueryParams
import top.defaults.kotlinoverflow.data.resp.QuestionList

interface QuestionContract {

    interface View : RecyclerViewContract.View<QuestionList> {

        fun getParams(): QuestionQueryParams
    }

}