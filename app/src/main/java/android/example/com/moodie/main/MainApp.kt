package android.example.com.moodie.main

import android.app.Application
import android.example.com.moodie.models.DiaryJSONStore
import android.example.com.moodie.models.DiaryStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    //create instance of DiaryStore to have access to the list stored in that class
    lateinit var diaries: DiaryStore

    override fun onCreate() {
        super.onCreate()
        //diaries = DiaryMemStore()
        diaries = DiaryJSONStore(applicationContext)
        info("Diary started")
    }
}