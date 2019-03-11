package android.example.com.moodie.activities

import android.example.com.moodie.R
import android.example.com.moodie.models.DiaryModel
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_diary.view.*

//?takes in a list, return recycleview adapter (to hold the data in the list)
class DiaryAdapter constructor(private var diaries: List<DiaryModel>) : RecyclerView.Adapter<DiaryAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_diary, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val diary = diaries[holder.adapterPosition]
        holder.bind(diary)
    }

    override fun getItemCount(): Int = diaries.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(diary: DiaryModel) {
            itemView.diaryTitle.text = diary.title
            itemView.diaryDescription.text = diary.description
        }
    }
}