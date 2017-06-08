package top.defaults.kotlinoverflow.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.Observable
import top.defaults.kotlinoverflow.`object`.Http
import top.defaults.kotlinoverflow.adapter.BaseRecyclerViewAdapter
import top.defaults.kotlinoverflow.adapter.QuestionAdapter
import top.defaults.kotlinoverflow.api.Questions
import top.defaults.kotlinoverflow.model.Question
import top.defaults.kotlinoverflow.model.QuestionList
import top.defaults.kotlinoverflow.util.android
import top.defaults.kotlinoverflow.view.ManagedRecyclerView

class QuestionsFragment : RecyclerViewFragment<Question, QuestionList>() {
    private val adapter: QuestionAdapter by lazy {
        QuestionAdapter()
    }

    private val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context)
    }

    override fun getAdapter(): BaseRecyclerViewAdapter<Question> {
        return adapter
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManager
    }

    override fun getObservable(): Observable<QuestionList> {
        return Http.create(Questions::class.java)
                .questions(managedRecyclerView.paging.page)
                .android(this)
    }

    override fun doWork(observable: Observable<QuestionList>, append: Boolean) {
        observable.subscribe({
            managedRecyclerView.setStatus(ManagedRecyclerView.Status.NORMAL)
            if (!append) {
                adapter.clear()
            }
            if (it.items != null) {
                adapter.append(it.items)
                if (!it.items.isEmpty()) {
                    managedRecyclerView.paging.inc()
                }
            }
            if (it.hasMore != true) {
                managedRecyclerView.setStatus(ManagedRecyclerView.Status.NO_MORE)
            }
            managedRecyclerView.paging.hasMore = it.hasMore ?: false
            adapter.notifyDataSetChanged()
        })
    }
}