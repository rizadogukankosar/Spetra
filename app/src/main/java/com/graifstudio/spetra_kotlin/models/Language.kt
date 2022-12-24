package com.graifstudio.spetra_kotlin.models

import com.google.cloud.translate.Language

data class Language(val detected: Language){
    override fun toString(): String {
        return detected.name
    }
}
