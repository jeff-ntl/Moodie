package android.example.com.moodie.activities

import android.content.Intent
import android.example.com.moodie.R
import android.example.com.moodie.main.MainApp
import android.example.com.moodie.models.DiaryModel
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_diary_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult

class DiaryListActivity : AppCompatActivity(), DiaryListener {

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
        //added this due to changed in DiaryAdapter (card click listener)
        //recyclerView.adapter = DiaryAdapter(app.diaries.findAll(),this)
        loadDiaries()

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

    //handle diary card clicked
    override fun onDiaryClick(diary: DiaryModel) {
        //diary data is put as extra ( to be deliver to MainActivity)
        startActivityForResult(intentFor<MainActivity>().putExtra("diary_edit", diary), 0)
    }

    //refresh diary on the view (card)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //recyclerView is a widget in activity_diary_list.xml
        //recyclerView.adapter?.notifyDataSetChanged()
        loadDiaries()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadDiaries(){
        showDiaries(app.diaries.findAll())
    }

    fun showDiaries (diaries:List<DiaryModel>){
        recyclerView.adapter = DiaryAdapter(diaries, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }


}
