package android.example.com.moodie.activities

import android.app.Activity
import android.content.Intent
import android.example.com.moodie.R
import android.example.com.moodie.helpers.readImage
import android.example.com.moodie.helpers.readImageFromPath
import android.example.com.moodie.helpers.showImagePicker
import android.example.com.moodie.main.MainApp
import android.example.com.moodie.models.DiaryModel
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v4.widget.ImageViewCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_diary.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity(), AnkoLogger{

    //variables
    var diary = DiaryModel()
    lateinit var app: MainApp

    //false = add diary, true = change diary
    var edit = false

    //used to tell that the image picker request is requested by MainActivity
    val IMAGE_REQUEST = 1

    //to get current date and time
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy")
    val formatted = current.format(formatter)

    //to take photo
    val REQUEST_IMAGE_CAPTURE = 2





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.example.com.moodie.R.layout.activity_main)
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
            //diaryTitle.setText(diary.title)
            diaryDescription.setText(diary.description)
            btnAdd.setText(android.example.com.moodie.R.string.button_saveDiary)
            if(diary.image!=null){
                chooseImage.setText(android.example.com.moodie.R.string.button_changeImage)
            }
            //read image added by user ( for editing image)
            diaryImage.setImageBitmap(readImageFromPath(this, diary.image))

            //set selected mood opacity to opaque when editing
            when(diary.mood){
                "Smiling" -> smilingFace.alpha = 1.0f
                "Neutral" -> neutralFace.alpha = 1.0f
                "Sad" -> sadFace.alpha = 1.0f
                "Angry" -> angryFace.alpha = 1.0f
            }
        }

        //add button listener
        btnAdd.setOnClickListener {
            diary.title = formatted
            diary.description = diaryDescription.text.toString()
            if (diary.description.isNotEmpty()) {
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
                toast (getString(android.example.com.moodie.R.string.toast_message))
            }
        }

        //add image button listener
        chooseImage.setOnClickListener {
            info ("Select image")
            showImagePicker(this,IMAGE_REQUEST)
        }

        //take photo button listener
        takePhoto.setOnClickListener{
            info("Take photo")
            dispatchTakePictureIntent()
        }

        info("Current Date is: $formatted")

        //handle Smiling Face clicked
        smilingFace.setOnClickListener{
            Toast.makeText(this,"You have clicked smiling face!", Toast.LENGTH_SHORT).show()
            smilingFace.alpha = 1.0f
            neutralFace.alpha= 0.5f
            sadFace.alpha = 0.5f
            angryFace.alpha = 0.5f
            diary.mood = "Smiling"
        }

        //handle Neutral Face clicked
        neutralFace.setOnClickListener{
            Toast.makeText(this,"You have clicked neutral face!", Toast.LENGTH_SHORT).show()
            smilingFace.alpha = 0.5f
            neutralFace.alpha= 1.0f
            sadFace.alpha = 0.5f
            angryFace.alpha = 0.5f
            diary.mood = "Neutral"
        }

        //handle Sad Face clicked
        sadFace.setOnClickListener{
            Toast.makeText(this,"You have clicked sad face!", Toast.LENGTH_SHORT).show()
            smilingFace.alpha = 0.5f
            neutralFace.alpha= 0.5f
            sadFace.alpha = 1.0f
            angryFace.alpha = 0.5f
            diary.mood = "Sad"
        }

        //handle Angry Face clicked
        angryFace.setOnClickListener{
            Toast.makeText(this,"You have clicked angry face!", Toast.LENGTH_SHORT).show()
            smilingFace.alpha = 0.5f
            neutralFace.alpha= 0.5f
            sadFace.alpha = 0.5f
            angryFace.alpha = 1.0f
            diary.mood = "Angry"
        }
    }


    //to load menu resource
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(android.example.com.moodie.R.menu.menu_diary, menu)
        //set the trash icon to be true if in edit mode and menu exists?
        if (edit && menu != null) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    //handle cancel button clicked
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.example.com.moodie.R.id.item_delete -> {
                app.diaries.delete(diary)
                finish()
            }
            android.example.com.moodie.R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //recover diary image if the IMAGE_REQUEST(from clicking add image button) is seen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        info("your image data is: $data")
        when (requestCode) {
            //when an image is loaded
            IMAGE_REQUEST -> {
                if (data != null) {
                    //to recover image data from image picker
                    diary.image = data.data.toString()
                    //to display image(in MainActivty, when user picked the image)
                    diaryImage.setImageBitmap(readImage(this,resultCode,data))
                    chooseImage.setText(android.example.com.moodie.R.string.button_changeImage)
                }
            }
            //when a photo is taken
            REQUEST_IMAGE_CAPTURE ->{
                if(data!=null && resultCode == Activity.RESULT_OK){
                    //diary.image = data.toString()

                    val imageBitmap = data.extras.get("data") as Bitmap
                    diaryImage.setImageBitmap(imageBitmap)

                }

            }
        }

    }
    //to take photo
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }











}
