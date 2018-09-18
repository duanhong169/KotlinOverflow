package top.defaults.kotlinoverflow.features.question

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.defaults.kotlinoverflow.adapter.BaseRecyclerViewAdapter
import top.defaults.kotlinoverflow.adapter.QuestionDetailAdapter
import top.defaults.kotlinoverflow.common.recyclerview.RecyclerViewFragment
import top.defaults.kotlinoverflow.data.param.QuestionQueryParams
import top.defaults.kotlinoverflow.data.resp.Question
import top.defaults.kotlinoverflow.data.resp.QuestionDetailSection
import top.defaults.kotlinoverflow.data.resp.QuestionList
import top.defaults.kotlinoverflow.util.unescapeHtml
import top.defaults.kotlinoverflow.view.ManagedRecyclerView

class QuestionFragment : RecyclerViewFragment<QuestionDetailSection, QuestionList>(), QuestionContract.View {

    private var question: Question? = null

    companion object {
        const val QUESTION = "question"
    }

    private val adapter: QuestionDetailAdapter by lazy {
        QuestionDetailAdapter()
    }

    private val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context)
    }

    override fun getAdapter(): BaseRecyclerViewAdapter<QuestionDetailSection> {
        return adapter
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        question = arguments?.getParcelable(QUESTION)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        question?.let { question ->
            fillAdapter(question, true)
            setTitle(question.title?.unescapeHtml())
        }
    }

    override fun getParams(): QuestionQueryParams = QuestionQueryParams(question?.questionId?:0)

    override fun show(data: QuestionList, append: Boolean) {
        managedRecyclerView.setStatus(ManagedRecyclerView.Status.NORMAL)
        if (!append) {
            adapter.clear()
        }
        if (data.items != null) {
            if (!data.items.isEmpty()) {
                data.items[0]?.let { question ->
                    fillAdapter(question)
                }
                managedRecyclerView.paging.inc()
            }
        }
        if (data.hasMore != true) {
            managedRecyclerView.setStatus(ManagedRecyclerView.Status.NO_MORE)
        }
        managedRecyclerView.paging.hasMore = data.hasMore ?: false
        adapter.notifyDataSetChanged()
    }

    private fun fillAdapter(question: Question, onlyHead: Boolean = false) {
        val sections = ArrayList<QuestionDetailSection>()
        sections.add(QuestionDetailSection(QuestionDetailSection.SECTION_TYPE_QUESTION_HEAD, question))
        if (!onlyHead) {
            sections.add(QuestionDetailSection(QuestionDetailSection.SECTION_TYPE_QUESTION_BODY, question))
            sections.add(QuestionDetailSection(QuestionDetailSection.SECTION_TYPE_QUESTION_TAIL, question))
            question.answerCount?.let { answerCount ->
                sections.add(QuestionDetailSection(QuestionDetailSection.SECTION_TYPE_ANSWER_TITLE, answerCount))
            }
            question.answers?.let { answers ->
                for (answer in answers) {
                    answer?.let { it ->
                        sections.add(QuestionDetailSection(QuestionDetailSection.SECTION_TYPE_ANSWER, it))
                    }
                }
            }
        }
        adapter.append(sections)
    }
}