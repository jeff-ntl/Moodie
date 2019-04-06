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




class PieActivity : AppCompatActivity() {

    lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.example.com.moodie.R.layout.activity_pie)

        //refer to piechart from your layout
        pieChart = findViewById(android.example.com.moodie.R.id.piechart)

        //add y-values in percentage
        //pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        //provide margin
        pieChart.setExtraOffsets(5F,10F,5F,5F)

        //when dragging the piechart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        //the "hole" inside the circle, transparentCircleRadius give a 3d visual
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.transparentCircleRadius = 61F

        //store pie chart data into arraylist
        val yValues = ArrayList<PieEntry>()
        yValues.add(PieEntry(8f, "PartyA"))
        yValues.add(PieEntry(15f, "USA"))
        yValues.add(PieEntry(12f, "UK"))
        yValues.add(PieEntry(25f, "India"))
        yValues.add(PieEntry(23f, "Russia"))
        yValues.add(PieEntry(17f, "Japan"))

        //description of PieChart
        var descr = Description()
        descr.text ="Your Mood in PieChart"
        descr.textSize = 36f
        pieChart.description = descr

        //animation
        pieChart.animateY(1000)

        //set PieChart data
        val dataSet = PieDataSet(yValues,"Countries")
        //the "boundary" between each data
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        //setting colors
        val colors = ArrayList<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS)
            colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors

        val data = PieData(dataSet)
        //data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(16f)
        data.setValueTextColor(Color.DKGRAY)

        pieChart.data = data
    }
}
