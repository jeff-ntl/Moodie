/*
* Reference:
*   Custom image on toolbar
*       Basic Usage:
*           https://android--code.blogspot.com/2018/02/android-kotlin-toolbar-example.html
*       Free SVG icon (looks perfect on toolbar):
*           https://material.io/tools/icons/?icon=alarm&style=baseline
*       Importing image into drawable folder:
*           https://stackoverflow.com/questions/29047902/how-to-add-an-image-to-the-drawable-folder-in-android-studio
* */
package android.example.com.moodie.activities

import android.content.Intent
import android.example.com.moodie.main.MainApp
import android.example.com.moodie.models.DiaryModel
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_diary_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult


class DiaryListActivity : AppCompatActivity(), DiaryListener, AnkoLogger {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.example.com.moodie.R.layout.activity_diary_list)
        app = application as MainApp

        //card view
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        loadDiaries()

        //tool bar / menu
        toolbarMain.title = title
        setSupportActionBar(toolbarMain)
    }

    //to load menu resource
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(android.example.com.moodie.R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //handle + icon clicked
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            //if the + icon is clicked, call MainActivity
            android.example.com.moodie.R.id.item_add -> startActivityForResult<MainActivity>(0)
            android.example.com.moodie.R.id.item_pie -> startActivityForResult<PieActivity>(1)
        }
        return super.onOptionsItemSelected(item)
    }

    //handle diary card clicked
    override fun onDiaryClick(diary: DiaryModel) {
        //diary data is put as extra ( to be deliver to MainActivity)
        startActivityForResult(intentFor<MainActivity>().putExtra("diary_edit", diary), 0)
        info(diary)
    }

    //refresh diary on the view (card)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadDiaries()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadDiaries() {
        showDiaries(app.diaries.findAll())
    }

    fun showDiaries(diaries: List<DiaryModel>) {
        recyclerView.adapter = DiaryAdapter(diaries, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }


}
