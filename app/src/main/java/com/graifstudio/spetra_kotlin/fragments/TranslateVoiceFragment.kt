package com.graifstudio.spetra_kotlin.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.graifstudio.spetra_kotlin.R
import com.graifstudio.spetra_kotlin.adapters.CustomAdapter
import com.graifstudio.spetra_kotlin.models.Language
import com.graifstudio.spetra_kotlin.models.TranslatedData
import com.graifstudio.spetra_kotlin.services.TranslateService
import java.io.IOException
import java.util.*


class TranslateVoiceFragment : Fragment() {
    private lateinit var fragment : View
    private lateinit var micButton1 : ImageButton
    private lateinit var micButton2 : ImageButton
    private lateinit var spinnerSpeakerOne: Spinner
    private lateinit var spinnerSpeakerTwo: Spinner
    private lateinit var translate : Translate
    private lateinit var recyclerView: RecyclerView
    private lateinit var translatedDataList: ArrayList<TranslatedData>
    private lateinit var adapter: CustomAdapter

    private lateinit var sourceLanguage: Language
    private lateinit var targetLanguage: Language
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragment = inflater.inflate(R.layout.fragment_translate_voice, container, false)
        translatedDataList = ArrayList()

        identifyViewItems()
        setButtonClickListeners()
        setupRecyclerView()

        translate = TranslateService.getTranslateService(fragment.context)!!
        TranslateService.setSpinnerLanguages(
            fragment.context,
            translate,
            spinnerSpeakerOne,
            spinnerSpeakerTwo
        )

        return fragment
    }

    fun setButtonClickListeners(){
        micButton1.setOnClickListener {
            sourceLanguage = spinnerSpeakerOne.selectedItem as Language
            targetLanguage = spinnerSpeakerTwo.selectedItem as Language
            speechToText()
        }

        micButton2.setOnClickListener {
            targetLanguage = spinnerSpeakerOne.selectedItem as Language
            sourceLanguage = spinnerSpeakerTwo.selectedItem as Language
            speechToText()
        }
    }

    private fun speechToText()
    {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault())
        if (activity?.packageManager?.let { intent.resolveActivity(it) } != null)
        {
            activity?.startActivityForResult(intent, 10)
        } else
        {
            Toast.makeText(activity,
                "Your Device Doesn't Support Speech Input",
                Toast.LENGTH_SHORT)
                .show()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode,
            resultCode, data)
        when (requestCode) {
            10 -> if (resultCode == RESULT_OK &&
                data != null)
            {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                translateText(result?.get(0).toString())
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun translateText(text: String){
        if (!sourceLanguage.detected.code.equals(targetLanguage.detected.code)) {
            val translatedText: String = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(sourceLanguage.detected.code),
                Translate.TranslateOption.targetLanguage(targetLanguage.detected.code),
                Translate.TranslateOption.model("base")
            ).translatedText
            val translatedData = TranslatedData(translatedText, sourceLanguage, targetLanguage)
            translatedDataList.add(translatedData)
            adapter.notifyDataSetChanged()
            recyclerView.smoothScrollToPosition(adapter.getItemCount());
        }else{
            Toast.makeText(fragment.context, "Source language same as target language", Toast.LENGTH_SHORT).show()
        }
    }

    fun identifyViewItems(){
        micButton1 = fragment.findViewById(R.id.micButton1)
        micButton2 = fragment.findViewById(R.id.micButton2)
        spinnerSpeakerOne = fragment.findViewById(R.id.spinnerSpeakerOne)
        spinnerSpeakerTwo = fragment.findViewById(R.id.spinnerSpeakerTwo)
        recyclerView = fragment.findViewById(R.id.translateVoiceRecycler)
    }

    fun setupRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(fragment.context, LinearLayoutManager.VERTICAL, false)
        adapter = CustomAdapter(translatedDataList)
        recyclerView.adapter = adapter
    }


}