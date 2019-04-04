package android.example.com.moodie.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

//return the id of the diary, used to set the id of diary modek (++ so that it starts from 1)
internal fun getId():Long{
    return lastId++
}

class DiaryMemStore: DiaryStore, AnkoLogger{

    val diaries = ArrayList<DiaryModel>()

    //to return the list
    override fun findAll(): List<DiaryModel>{
        return diaries
    }

    //to add new item into the list
    override fun create(diary: DiaryModel){
        diary.id = getId()
        diaries.add(diary)
        logAll()
    }

    //to update
    override fun update(diary: DiaryModel){
        //find the diary model object
        var foundDiary: DiaryModel? = diaries.find{ d -> d.id == diary.id}
        if(foundDiary != null){
            foundDiary.title = diary.title;
            foundDiary.description = diary.description
            foundDiary.image = diary.image
            logAll()
        }
    }

    //to delete
    override fun delete(diary:DiaryModel){
        diaries.remove(diary)
    }

    fun logAll(){
        diaries.forEach{info("${it}")}
    }
}