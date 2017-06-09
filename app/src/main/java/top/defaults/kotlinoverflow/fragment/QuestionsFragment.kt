package top.defaults.kotlinoverflow.fragment

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.Observable
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.`object`.Http
import top.defaults.kotlinoverflow.adapter.BaseRecyclerViewAdapter
import top.defaults.kotlinoverflow.adapter.QuestionAdapter
import top.defaults.kotlinoverflow.api.Questions
import top.defaults.kotlinoverflow.common.listener.OnItemClickListener
import top.defaults.kotlinoverflow.model.Question
import top.defaults.kotlinoverflow.model.QuestionList
import top.defaults.kotlinoverflow.util.toast
import top.defaults.kotlinoverflow.view.ManagedRecyclerView

class QuestionsFragment : RecyclerViewFragment<Question, QuestionList>() {
    private val adapter: QuestionAdapter by lazy {
        val adapter = QuestionAdapter()
        adapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val question = adapter.getItem(position) as Question
                toast(question.questionId.toString())
            }
        }
        adapter
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

    override fun onConfigure(recyclerView: ManagedRecyclerView) {
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        itemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider_horizontal_1px))
        recyclerView.recyclerView.addItemDecoration(itemDecoration)
    }

    override fun getObservable(): Observable<QuestionList> {
        return Http.create(Questions::class.java)
                .questions(managedRecyclerView.paging.page)
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
        }, {})
    }
}