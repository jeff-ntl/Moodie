package android.example.com.moodie.activities

import android.example.com.moodie.R
import android.example.com.moodie.main.MainApp
import android.example.com.moodie.models.DiaryModel
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import kotlinx.android.synthetic.main.activity_diary_list.*
import kotlinx.android.synthetic.main.card_diary.view.*
import org.jetbrains.anko.startActivityForResult

class DiaryListActivity : AppCompatActivity() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)
        app = application as MainApp


        //card view
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        //?what data (in arraylist) to be used
        //recyclerView.adapter = DiaryAdapter(app.diaries)
        recyclerView.adapter = DiaryAdapter(app.diaries.findAll())

        //tool bar / menu
        //??????????
        toolbarMain.title = title
        setSupportActionBar(toolbarMain)
    }

    //to load menu resource
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //handle + icon clicked
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            //if the + icon is clicked, call MainActivity
            R.id.item_add -> startActivityForResult<MainActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }
}
