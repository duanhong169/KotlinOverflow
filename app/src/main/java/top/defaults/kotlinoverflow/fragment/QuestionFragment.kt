package top.defaults.kotlinoverflow.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.Observable
import top.defaults.kotlinoverflow.`object`.Http
import top.defaults.kotlinoverflow.adapter.BaseRecyclerViewAdapter
import top.defaults.kotlinoverflow.adapter.QuestionDetailAdapter
import top.defaults.kotlinoverflow.api.Questions
import top.defaults.kotlinoverflow.model.QuestionDetailSection
import top.defaults.kotlinoverflow.model.QuestionList
import top.defaults.kotlinoverflow.view.ManagedRecyclerView

class QuestionFragment : RecyclerViewFragment<QuestionDetailSection, QuestionList>() {

    companion object {
        val QUESTION_ID = "question_id"
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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getObservable(): Observable<QuestionList> {
        return Http.create(Questions::class.java)
                .question(arguments.getInt(QUESTION_ID))
    }

    override fun doWork(observable: Observable<QuestionList>, append: Boolean) {
        observable.subscribe({
            managedRecyclerView.setStatus(ManagedRecyclerView.Status.NORMAL)
            if (!append) {
                adapter.clear()
            }
            if (it.items != null) {
                if (!it.items.isEmpty()) {
                    val sections = ArrayList<QuestionDetailSection>()
                    sections.add(QuestionDetailSection(QuestionDetailSection.SECTION_TYPE_QUESTION_HEAD, it.items[0]))
                    adapter.append(sections)
                    managedRecyclerView.paging.inc()
                }
            }
            if (it.hasMore != true) {
                managedRecyclerView.setStatus(ManagedRecyclerView.Status.NO_MORE)
            }
            managedRecyclerView.paging.hasMore = it.hasMore ?: false
            adapter.notifyDataSetChanged()
        }, {})
    }

}