/*
* Reference:
*   MPAndroidChart
*       tutorial video for basic usage:
*           https://www.youtube.com/watch?v=MiVx3AQD_PI
*       original source (on GitHub):
*           https://github.com/PhilJay/MPAndroidChart
*       idea of adding colors to the pie chart:
*           https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/PieChartActivity.java
*       idea of adding custom colors & what colors are included in the template provided
*           https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartLib/src/main/java/com/github/mikephil/charting/utils/ColorTemplate.java
*       more detailed use of pie chart:
*           https://www.studytutorial.in/android-pie-chart-using-mpandroid-library-tutorial
* */
package android.example.com.moodie.activities

import android.example.com.moodie.R
import android.example.com.moodie.main.MainApp
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_pie.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class PieActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.example.com.moodie.R.layout.activity_pie)

        //variable
        val app: MainApp = application as MainApp
        //counter to be used for pieChart
        var smilingCounter = 0f
        var neutralCounter = 0f
        var sadCounter = 0f
        var angryCounter = 0f

        //create an instance of MainApp

        toolbarPie.title = title
        //?tell which toolbar supports the onCreateOptionsMenu
        setSupportActionBar(toolbarPie)

        //loop through the arraylist and count mood
        val diaryArrayList = app.diaries.findAll()
        for (item in diaryArrayList) {
            when (item.mood) {
                "Smiling" -> smilingCounter++
                "Neutral" -> neutralCounter++
                "Sad" -> sadCounter++
                "Angry" -> angryCounter++
            }
        }

        info("Smiling: $smilingCounter, Neutral: $neutralCounter, Sad: $sadCounter, Angry: $angryCounter")

        //refer to piechart from your layout
        pieChart = this.findViewById(android.example.com.moodie.R.id.piechart)

        //provide margin
        pieChart.setExtraOffsets(5F, 10F, 5F, 5F)

        //when dragging the piechart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        //the "hole" inside the circle, transparentCircleRadius give a 3d visual
        pieChart.isDrawHoleEnabled = false

        //store pie chart data into arraylist
        val yValues = ArrayList<PieEntry>()
        yValues.add(PieEntry(sadCounter, "Sad"))
        yValues.add(PieEntry(neutralCounter, "Neutral"))
        yValues.add(PieEntry(angryCounter, "Angry"))
        yValues.add(PieEntry(smilingCounter, "Smiling"))

        //description of PieChart
        val descr = Description()
        descr.text = "Your Mood in PieChart"
        descr.textColor = R.color.colorAccent
        descr.textSize = 36f
        pieChart.description = descr

        //animation
        pieChart.animateY(1000)

        //set PieChart data
        val dataSet = PieDataSet(yValues, "")

        //the "boundary" between each data
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        //setting colors
        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.getHoloBlue())

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        dataSet.colors = colors

        val data = PieData(dataSet)
        //data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(16f)
        data.setValueTextColor(Color.DKGRAY)

        pieChart.data = data
    }

    //to load menu resource
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_pie, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //handle cancel button clicked
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.example.com.moodie.R.id.item_pie_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
