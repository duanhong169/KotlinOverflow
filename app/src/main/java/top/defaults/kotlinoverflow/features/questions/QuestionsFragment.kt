package top.defaults.kotlinoverflow.features.questions

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.features.simple.SimpleActivity
import top.defaults.kotlinoverflow.adapter.BaseRecyclerViewAdapter
import top.defaults.kotlinoverflow.adapter.QuestionAdapter
import top.defaults.kotlinoverflow.common.listener.OnItemClickListener
import top.defaults.kotlinoverflow.data.resp.Question
import top.defaults.kotlinoverflow.data.resp.QuestionList
import top.defaults.kotlinoverflow.features.question.QuestionFragment
import top.defaults.kotlinoverflow.common.recyclerview.RecyclerViewFragment
import top.defaults.kotlinoverflow.data.param.QuestionsQueryParams
import top.defaults.kotlinoverflow.view.ManagedRecyclerView

class QuestionsFragment : RecyclerViewFragment<Question, QuestionList>(), QuestionsContract.View {

    private val adapter: QuestionAdapter by lazy {
        val adapter = QuestionAdapter()
        adapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val question = adapter.getItem(position)
                val args = Bundle()
                question?.let {
                    args.putParcelable(QuestionFragment.QUESTION, question)
                }
                pushFragment(SimpleActivity.FRAGMENT_TYPE_QUESTION, args)
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
        itemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider_horizontal_1px)!!)
        recyclerView.recyclerView.addItemDecoration(itemDecoration)
    }

    override fun show(data: QuestionList, append: Boolean) {
        managedRecyclerView.setStatus(ManagedRecyclerView.Status.NORMAL)
        if (!append) {
            adapter.clear()
        }
        if (data.items != null) {
            adapter.append(data.items)
            if (!data.items.isEmpty()) {
                managedRecyclerView.paging.inc()
            }
        }
        if (data.hasMore != true) {
            managedRecyclerView.setStatus(ManagedRecyclerView.Status.NO_MORE)
        }
        managedRecyclerView.paging.hasMore = data.hasMore ?: false
        adapter.notifyDataSetChanged()
    }

    override fun getParams(): QuestionsQueryParams =
            QuestionsQueryParams(managedRecyclerView.paging.page,
                    sort = arguments!!.getString("sort", "activity"))
}