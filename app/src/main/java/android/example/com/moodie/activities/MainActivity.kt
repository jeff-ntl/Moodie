/*
* Reference:
*   Getting current date and time
*       Basic usage:
*           https://www.programiz.com/kotlin-programming/examples/current-date-time
*       Customize date and time format:
*           https://developer.android.com/reference/java/time/format/DateTimeFormatter
*
*   Check if edit text is null or empty:
*           https://stackoverflow.com/questions/46035877/check-if-edittext-is-empty-kotlin-android
*
*   Mood List:
*           https://www.youtube.com/watch?v=IuvUwQVTfGw
*
*   Position editText text to top left:
*           https://stackoverflow.com/questions/5897181/is-there-anyway-to-make-an-edittext-start-typing-at-the-top-left-corner-of-the-e
*
*   Clickable image (the 4 moods)
*        onClick handler:
*           https://www.tutorialkart.com/kotlin-android/set-onclicklistener-for-imageview-in-kotlin-android/
*        image opacity/ alpha:
*           https://stackoverflow.com/questions/4931071/android-and-setting-alpha-for-image-view-alpha
*
*    Camera
*        basic usage:
*           https://developer.android.com/training/camera/photobasics#kotlin
*        to hardcoding packaging path:
*           https://github.com/jkwiecien/EasyImage/issues/103
*
*     Notification
*        set up:
*           https://medium.com/@lobothijau/create-android-push-notification-easily-with-kotlin-and-firebase-cloud-messaging-part-1-9062f2a57555
*           https://www.youtube.com/watch?v=2EHDSvx_OYg
* */
package android.example.com.moodie.activities

import android.app.Activity
import android.content.Intent
import android.example.com.moodie.R.string.*
import android.example.com.moodie.helpers.readImage
import android.example.com.moodie.helpers.readImageFromPath
import android.example.com.moodie.helpers.showImagePicker
import android.example.com.moodie.main.MainApp
import android.example.com.moodie.models.DiaryModel
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity(), AnkoLogger {

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

    //used for capturing photo
    val REQUEST_TAKE_PHOTO = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.example.com.moodie.R.layout.activity_main)
        info("Main Activity started!")

        //create an instance of MainApp
        app = application as MainApp

        //toolbar / menu
        toolbarAdd.title = title
        //?tell which toolbar supports the onCreateOptionsMenu
        setSupportActionBar(toolbarAdd)

        //to retrieve diary data(from DiaryListActivity) when diary card is clicked
        if (intent.hasExtra("diary_edit")) {
            edit = true
            diary = intent.extras.getParcelable<DiaryModel>("diary_edit")
            diaryTitle.setText(diary.title)
            diaryDescription.setText(diary.description)
            btnAdd.setText(button_saveDiary)
            if (diary.image != "") {
                chooseImage.setText(button_changeImage)
            }
            //read image added by user ( for editing image)
            diaryImage.setImageBitmap(readImageFromPath(this, diary.image))

            //set selected mood opacity to opaque when editing
            when (diary.mood) {
                "Smiling" -> smilingFace.alpha = 1.0f
                "Neutral" -> neutralFace.alpha = 1.0f
                "Sad" -> sadFace.alpha = 1.0f
                "Angry" -> angryFace.alpha = 1.0f
            }
        }

        //add button listener
        btnAdd.setOnClickListener {
            if (diaryTitle.text.isNullOrEmpty()) {
                diary.title = formatted
            } else {
                diary.title = diaryTitle.text.toString()
            }
            //diary.title = formatted
            diary.description = diaryDescription.text.toString()
            if (diary.description.isNotEmpty()) {
                if (edit) {
                    app.diaries.update(diary.copy())
                } else {
                    app.diaries.create(diary.copy())
                }
                //tell the previous activity that the operation is successful (this activity is opened)
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            } else {
                toast(getString(toast_message))
            }
        }

        //add image button listener
        chooseImage.setOnClickListener {
            info("Select image")
            showImagePicker(this, IMAGE_REQUEST)
        }

        //take photo button listener
        takePhoto.setOnClickListener {
            info("Take photo")
            dispatchTakePictureIntent()
        }

        info("Current Date is: $formatted")

        //handle Smiling Face clicked
        smilingFace.setOnClickListener {
            Toast.makeText(this, "You have clicked smiling face!", Toast.LENGTH_SHORT).show()
            smilingFace.alpha = 1.0f
            neutralFace.alpha = 0.5f
            sadFace.alpha = 0.5f
            angryFace.alpha = 0.5f
            diary.mood = "Smiling"
        }

        //handle Neutral Face clicked
        neutralFace.setOnClickListener {
            Toast.makeText(this, "You have clicked neutral face!", Toast.LENGTH_SHORT).show()
            smilingFace.alpha = 0.5f
            neutralFace.alpha = 1.0f
            sadFace.alpha = 0.5f
            angryFace.alpha = 0.5f
            diary.mood = "Neutral"
        }

        //handle Sad Face clicked
        sadFace.setOnClickListener {
            Toast.makeText(this, "You have clicked sad face!", Toast.LENGTH_SHORT).show()
            smilingFace.alpha = 0.5f
            neutralFace.alpha = 0.5f
            sadFace.alpha = 1.0f
            angryFace.alpha = 0.5f
            diary.mood = "Sad"
        }

        //handle Angry Face clicked
        angryFace.setOnClickListener {
            Toast.makeText(this, "You have clicked angry face!", Toast.LENGTH_SHORT).show()
            smilingFace.alpha = 0.5f
            neutralFace.alpha = 0.5f
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
                    diaryImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(button_changeImage)
                }
            }
            //when a photo is taken
            REQUEST_TAKE_PHOTO -> {
                if (data != null && resultCode == Activity.RESULT_OK) {
                    diaryImage.setImageBitmap(readImageFromPath(this, diary.image))
                }
            }
        }

    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    info("Failed to take photo")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "android.example.com.moodie.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    diary.image = photoURI.toString()
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }
}
