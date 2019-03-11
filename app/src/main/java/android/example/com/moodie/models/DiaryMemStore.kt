package android.example.com.moodie.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class DiaryMemStore: DiaryStore, AnkoLogger{

    val diaries = ArrayList<DiaryModel>()

    //to return the list
    override fun findAll(): List<DiaryModel>{
        return diaries
    }

    //to add new item into the list
    override fun create(diary: DiaryModel){
        diaries.add(diary)
        logAll()
    }

    fun logAll(){
        diaries.forEach{info("${it}")}
    }
}