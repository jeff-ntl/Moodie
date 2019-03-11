package android.example.com.moodie.activities

import android.example.com.moodie.R
import android.example.com.moodie.main.MainApp
import android.example.com.moodie.models.DiaryModel
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), AnkoLogger {

    //variables
    var diary = DiaryModel()
    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        info("Main Activity started!")

        //create an instance of MainApp
        app = application as MainApp

        //toolbar / menu
        //???????
        toolbarAdd.title = title
        //?tell which toolbar supports the onCreateOptionsMenu
        setSupportActionBar(toolbarAdd)

        //to retrieve diary data when diary card is clicked
        if (intent.hasExtra("diary_edit")) {
            diary = intent.extras.getParcelable<DiaryModel>("diary_edit")
            diaryTitle.setText(diary.title)
            diaryDescription.setText(diary.description)
        }

        //add button listener
        btnAdd.setOnClickListener() {
            diary.title = diaryTitle.text.toString()
            diary.description = diaryDescription.text.toString()
            if (diary.title.isNotEmpty()) {
                //app.diaries.add(diary.copy())
                app.diaries.create(diary.copy())
                //tell the previous activity that the operation is successful (this activity is opened)
                setResult(AppCompatActivity.RESULT_OK)
                finish()
             }
            else {
                toast ("Please Enter a title")
            }
        }
    }

    //to load menu resource
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_diary, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //handle cancel button clicked
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
