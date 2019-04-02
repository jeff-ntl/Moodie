package android.example.com.moodie.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import android.example.com.moodie.helpers.*
import java.util.*

/*
* These are used in the implementations. These describe the filename,
 * a utility to serialize a java class (pretty printing it)
 * and an object to help in
 * converting a JSON string to a java collection (recognising PlacemarkModels along the way)
* */
val JSON_FILE = "diaries.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<DiaryModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class DiaryJSONStore : DiaryStore, AnkoLogger {

    val context: Context
    var diaries = mutableListOf<DiaryModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<DiaryModel> {
        return diaries
    }

    override fun create(diary: DiaryModel) {
        diary.id = generateRandomId()
        diaries.add(diary)
        serialize()
    }


    override fun update(diary: DiaryModel) {
        // todo
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(diaries, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        diaries = Gson().fromJson(jsonString, listType)
    }
}