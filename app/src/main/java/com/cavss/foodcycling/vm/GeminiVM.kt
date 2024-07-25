package com.cavss.foodcycling.vm

import com.cavss.foodcycling.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel

class GeminiVM {

    private val geminiVersion = "gemini-1.5-flash"
    private val gemini = GenerativeModel(
        modelName = geminiVersion,
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = BuildConfig.geminiApiKey
    )

    // create text
    val prompt = "Write a story about an AI and magic"
    suspend fun createText(prompt : String) : String? {
        val response = gemini.generateContent(prompt)
        return response.text
    }
}