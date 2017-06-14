package top.defaults.kotlinoverflow.model

class QuestionDetailSection(val type: Int, val data: Any?) {

    companion object {
        val SECTION_TYPE_QUESTION_HEAD = 1
        val SECTION_TYPE_QUESTION_BODY = 2
        val SECTION_TYPE_QUESTION_TAIL = 3
        val SECTION_TYPE_ANSWER_TITLE = 4
        val SECTION_TYPE_ANSWER = 5
    }

    init {
        if (data != null) {
            if (getClazz()?.name != data::class.java.name) {
                throw IllegalArgumentException("Wrong data type")
            }
        }
    }

    fun <T> get(clazz: Class<T>): T? {
        if (data == null) return null
        if (clazz.name == getClazz()?.name) {
            @Suppress("UNCHECKED_CAST")
            return data as T
        }
        return null
    }

    private fun getClazz(): Class<out Any>? {
        when(type) {
            SECTION_TYPE_QUESTION_HEAD -> return Question::class.java
        }

        return null
    }
}