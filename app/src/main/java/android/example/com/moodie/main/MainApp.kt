package android.example.com.moodie.main

import android.app.Application
import android.example.com.moodie.models.DiaryMemStore
import android.example.com.moodie.models.DiaryModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp: Application(), AnkoLogger {

    //create instance of DiaryMemStore to have access to the list stored in that class
    //val diaries = ArrayList<DiaryModel>()
    val diaries = DiaryMemStore()

    override fun onCreate() {
        super.onCreate()
        info("Diary started")
    }
}