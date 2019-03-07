package android.example.com.moodie.activities

import android.example.com.moodie.R
import android.example.com.moodie.models.DiaryModel
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), AnkoLogger {

    //variables
    var diary = DiaryModel()
    val diaries = ArrayList<DiaryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        info("Main Activity started!")

        btnAdd.setOnClickListener() {
            diary.title = diaryTitle.text.toString()
            if (diary.title.isNotEmpty()) {
                diaries.add(diary.copy())
                info("add Button Pressed: $diary")
                diaries.forEach { info("add Button Pressed: ${it.title}") }
            }
            else {
                toast ("Please Enter a title")
            }
        }
    }
}
