package android.example.com.moodie.activities

import android.content.Intent
import android.example.com.moodie.R
import android.example.com.moodie.helpers.readImage
import android.example.com.moodie.helpers.readImageFromPath
import android.example.com.moodie.helpers.showImagePicker
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

    //false = add diary, true = change diary
    var edit = false

    //used to tell that the image picker request is requested by MainActivity
    val IMAGE_REQUEST = 1

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

        //to retrieve diary data(from DiaryListActivity) when diary card is clicked
        if (intent.hasExtra("diary_edit")) {
            edit = true
            diary = intent.extras.getParcelable<DiaryModel>("diary_edit")
            diaryTitle.setText(diary.title)
            diaryDescription.setText(diary.description)
            btnAdd.setText(R.string.button_saveDiary)
            //read image added by user ( for editing image)
            diaryImage.setImageBitmap(readImageFromPath(this, diary.image))

        }

        //add button listener
        btnAdd.setOnClickListener() {
            diary.title = diaryTitle.text.toString()
            diary.description = diaryDescription.text.toString()
            if (diary.title.isNotEmpty()) {
                if(edit){
                    app.diaries.update(diary.copy())
                }else{
                    //app.diaries.add(diary.copy())
                    app.diaries.create(diary.copy())
                }
                //tell the previous activity that the operation is successful (this activity is opened)
                setResult(AppCompatActivity.RESULT_OK)
                finish()
             }
            else {
                toast (getString(R.string.toast_message))
            }
        }

        //add image button listener
        chooseImage.setOnClickListener {
            info ("Select image")
            showImagePicker(this,IMAGE_REQUEST)
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

    //recover diary image if the IMAGE_REQUEST(from clicking add image button) is seen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    //to recover image data from image picker
                    diary.image = data.getData().toString()
                    //to display image(in MainActivty, when user picked the image)
                    diaryImage.setImageBitmap(readImage(this,resultCode,data))
                }
            }
        }
    }
}
