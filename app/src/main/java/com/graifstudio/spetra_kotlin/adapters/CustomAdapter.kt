package com.graifstudio.spetra_kotlin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.graifstudio.spetra_kotlin.R
import com.graifstudio.spetra_kotlin.models.TranslatedData


class CustomAdapter(private val dataSet: ArrayList<TranslatedData>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val translateInfoTextView: TextView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.translatedText)
            translateInfoTextView = view.findViewById(R.id.translateInfo)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.translated_text_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position].translatedText
        viewHolder.translateInfoTextView.text = dataSet[position].sourceLanguage.detected.name + " -> " + dataSet[position].targetLanguage.detected.name
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
