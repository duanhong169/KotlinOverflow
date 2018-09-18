package top.defaults.kotlinoverflow.`object`

import android.content.Context
import top.defaults.kotlinoverflow.data.source.server.QuestionsAPIv22Repository
import top.defaults.kotlinoverflow.data.source.QuestionsRepository
import top.defaults.kotlinoverflow.data.source.server.UsersAPIv22Repository
import top.defaults.kotlinoverflow.data.source.UsersRepository

object Injection {

    fun provideQuestionsRepository(context: Context): QuestionsRepository {
        checkNotNull(context)
        return QuestionsAPIv22Repository.getInstance()
    }

    fun provideUsersRepository(context: Context): UsersRepository {
        checkNotNull(context)
        return UsersAPIv22Repository.getInstance()
    }
}