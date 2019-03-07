package android.example.com.moodie.main

import android.app.Application
import android.example.com.moodie.models.DiaryModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp: Application(), AnkoLogger {

    val diaries = ArrayList<DiaryModel>()

    override fun onCreate() {
        super.onCreate()
        info("Diary started")
    }
}