package android.example.com.moodie.activities

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.EventLogTags
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.formatter.PercentFormatter
import android.R.attr.data
import android.example.com.moodie.main.MainApp
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class PieActivity : AppCompatActivity(), AnkoLogger {

    lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.example.com.moodie.R.layout.activity_pie)

        //variable
        lateinit var app: MainApp
        //counter to be used for pieChart
        var smilingCounter = 0f
        var neutralCounter = 0f
        var sadCounter = 0f
        var angryCounter = 0f

        //create an instance of MainApp
        app = application as MainApp

        //loop through the arraylist and count mood
        var diaryArrayList = app.diaries.findAll()
        for(item in diaryArrayList){
            when(item.mood){
                "Smiling" -> smilingCounter++
                "Neutral" -> neutralCounter++
                "Sad" -> sadCounter++
                "Angry" -> angryCounter++
            }
        }

        info("Smiling: $smilingCounter, Neutral: $neutralCounter, Sad: $sadCounter, Angry: $angryCounter")

        //refer to piechart from your layout
        pieChart = findViewById(android.example.com.moodie.R.id.piechart)

        //add y-values in percentage
        //pieChart.setUsePercentValues(true)
        //pieChart.description.isEnabled = false
        //provide margin
        pieChart.setExtraOffsets(5F,10F,5F,5F)

        //when dragging the piechart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        //the "hole" inside the circle, transparentCircleRadius give a 3d visual
        pieChart.isDrawHoleEnabled = false
        //pieChart.setHoleColor(Color.WHITE)
        //pieChart.transparentCircleRadius = 61F

        //store pie chart data into arraylist
        val yValues = ArrayList<PieEntry>()
        yValues.add(PieEntry(sadCounter, "Sad"))
        yValues.add(PieEntry(neutralCounter, "Neutral"))
        yValues.add(PieEntry(angryCounter, "Angry"))
        yValues.add(PieEntry(smilingCounter, "Smiling"))
        
        //description of PieChart
        var descr = Description()
        descr.text ="Your Mood in PieChart"
        descr.textSize = 36f
        pieChart.description = descr

        //animation
        pieChart.animateY(1000)

        //set PieChart data
        val dataSet = PieDataSet(yValues,"")
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
}
