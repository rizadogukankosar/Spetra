package com.graifstudio.spetra_kotlin.services

import android.content.Context
import android.os.StrictMode
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.graifstudio.spetra_kotlin.R
import com.graifstudio.spetra_kotlin.models.Language
import java.io.IOException

object TranslateService {
    fun getTranslateService(context: Context): Translate? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            context.resources.openRawResource(R.raw.credentials).use { `is` ->
                val myCredentials: GoogleCredentials = GoogleCredentials.fromStream(`is`)
                val translateOptions: TranslateOptions =
                    TranslateOptions.newBuilder().setCredentials(myCredentials).build()
                val translate: Translate = translateOptions.getService()
                return translate
            }
        } catch (ioe: IOException) {
            ioe.printStackTrace()
            return null
        }
    }

    fun setSpinnerLanguages(context: Context, translate: Translate, spinnerSpeakerOne: Spinner, spinnerSpeakerTwo: Spinner){
        if (spinnerSpeakerOne != null && spinnerSpeakerTwo != null){
            val languageList = getLanguageList(translate)
            val adapter = ArrayAdapter(context,
                android.R.layout.simple_spinner_item, languageList)
            spinnerSpeakerOne.adapter = adapter
            spinnerSpeakerTwo.adapter = adapter
        }
    }

    fun getLanguageList(translate: Translate): ArrayList<Language>{
        var langugeList = ArrayList<Language>()
        val languageListFromCloud = translate.listSupportedLanguages()
        for (language in languageListFromCloud){
            langugeList.add(Language(language))
        }
        return langugeList
    }
}