package android.example.com.moodie.models

interface DiaryStore {
    fun findAll(): List<DiaryModel>
    fun create(diary: DiaryModel)
    fun update(diary: DiaryModel)
    fun delete(diary: DiaryModel)
}