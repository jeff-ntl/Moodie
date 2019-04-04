package android.example.com.moodie.activities

import android.example.com.moodie.R
import android.example.com.moodie.helpers.readImageFromPath
import android.example.com.moodie.models.DiaryModel
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_diary.view.*

//represent click events on the diary card
interface DiaryListener {
    fun onDiaryClick(diary: DiaryModel)
}

//?takes in a list, return recycleview adapter (to hold the data in the list)
//listener is the DiaryListActivitiy
class DiaryAdapter constructor(private var diaries: List<DiaryModel>,  private val listener: DiaryListener) : RecyclerView.Adapter<DiaryAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_diary, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val diary = diaries[holder.adapterPosition]
        holder.bind(diary, listener)
    }

    override fun getItemCount(): Int = diaries.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //data displayed in each card
        fun bind(diary: DiaryModel, listener: DiaryListener) {
            itemView.diaryTitle.text = diary.title
            when(diary.mood){
                "Smiling" -> itemView.moodIcon.setImageResource(R.drawable.smiling)
                "Neutral" -> itemView.moodIcon.setImageResource(R.drawable.neutral)
                "Sad" -> itemView.moodIcon.setImageResource(R.drawable.sad)
                "Angry" -> itemView.moodIcon.setImageResource(R.drawable.angry)
                else -> itemView.moodIcon.setImageResource(R.drawable.question)
            }
            //itemView.diaryDescription.text = diary.description
            //itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, diary.image))
            itemView.setOnClickListener { listener.onDiaryClick(diary) }
        }
    }
}