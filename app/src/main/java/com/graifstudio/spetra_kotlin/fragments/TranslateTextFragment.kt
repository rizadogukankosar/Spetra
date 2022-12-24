package com.graifstudio.spetra_kotlin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.cloud.translate.Translate
import com.graifstudio.spetra_kotlin.R
import com.graifstudio.spetra_kotlin.adapters.CustomAdapter
import com.graifstudio.spetra_kotlin.models.Language
import com.graifstudio.spetra_kotlin.models.TranslatedData
import com.graifstudio.spetra_kotlin.services.TranslateService.getTranslateService
import com.graifstudio.spetra_kotlin.services.TranslateService.setSpinnerLanguages

class TranslateTextFragment : Fragment(){

    private lateinit var fragment : View
    private lateinit var translateButton : ImageButton
    private lateinit var editText: EditText
    private lateinit var spinnerSpeakerOne: Spinner
    private lateinit var spinnerSpeakerTwo: Spinner
    private lateinit var translate : Translate
    private lateinit var recyclerView: RecyclerView
    private lateinit var translatedDataList: ArrayList<TranslatedData>
    private lateinit var adapter: CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragment = inflater.inflate(R.layout.fragment_translate_text, container, false)
        translatedDataList = ArrayList()

        identifyViewItems()
        setButtonClickListeners()
        setupRecyclerView()

        translate = getTranslateService(fragment.context)!!
        setSpinnerLanguages(fragment.context, translate, spinnerSpeakerOne, spinnerSpeakerTwo)

        return fragment
    }

    fun setButtonClickListeners(){
        translateButton.setOnClickListener {
            val text = editText.text.toString().trim()
            if (!text.equals(""))
            translateText(text)
            editText.text.clear()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun translateText(text: String){
        val selectedItemOne = spinnerSpeakerOne.selectedItem as Language
        val selectedItemTwo = spinnerSpeakerTwo.selectedItem as Language

        if (!selectedItemOne.detected.code.equals(selectedItemTwo.detected.code)) {
            val translatedText: String = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(selectedItemOne.detected.code),
                Translate.TranslateOption.targetLanguage(selectedItemTwo.detected.code),
                Translate.TranslateOption.model("base")
            ).translatedText
            val translatedData = TranslatedData(translatedText, selectedItemOne, selectedItemTwo)
            translatedDataList.add(translatedData)
            adapter.notifyDataSetChanged()
            recyclerView.smoothScrollToPosition(adapter.getItemCount());
        }else{
            Toast.makeText(fragment.context, "Source language same as target language", Toast.LENGTH_SHORT).show()
        }
    }

    fun identifyViewItems(){
        translateButton = fragment.findViewById(R.id.translateButton)
        editText = fragment.findViewById(R.id.editText)
        spinnerSpeakerOne = fragment.findViewById(R.id.spinnerSpeakerOne)
        spinnerSpeakerTwo = fragment.findViewById(R.id.spinnerSpeakerTwo)
        recyclerView = fragment.findViewById(R.id.translateTextRecycler)
    }

    fun setupRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(fragment.context, LinearLayoutManager.VERTICAL, false)
        adapter = CustomAdapter(translatedDataList)
        recyclerView.adapter = adapter
    }

}