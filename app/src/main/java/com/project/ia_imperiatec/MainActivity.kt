package com.project.ia_imperiatec

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.project.ia_imperiatec.models.*
import com.project.ia_imperiatec.network.RetrofitClient

class MainActivity : AppCompatActivity() {

    private lateinit var searchBar: EditText
    private lateinit var buttonAnalyze: Button
    private lateinit var textSummary: TextView
    private lateinit var textSearchResults: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchBar = findViewById(R.id.searchBar)
        buttonAnalyze = findViewById(R.id.buttonAnalyze)
        textSummary = findViewById(R.id.textSummary)
        textSearchResults = findViewById(R.id.textSearchResults)

        // 🔘 Analyse (résumé) déclenchée sans lire le PDF localement
        buttonAnalyze.setOnClickListener {
            lifecycleScope.launch {
                try {
                    Log.d("DEBUG", "Envoi d'une requête à /summarize (le PDF est côté serveur)")
                    val response = RetrofitClient.instance.summarize(emptyMap())

                    if (response.isSuccessful) {
                        val summary = response.body()?.summary ?: "Résumé non disponible."
                        Log.d("DEBUG", "Résumé reçu : $summary")
                        textSummary.text = summary
                    } else {
                        Log.e("ERREUR", "Erreur serveur /summarize : ${response.code()}")
                        textSummary.text = "Erreur serveur : ${response.code()}"
                    }
                } catch (e: Exception) {
                    Log.e("ERREUR", "Exception /summarize : ${e.localizedMessage}", e)
                    textSummary.text = "Erreur : ${e.localizedMessage}"
                }
            }
        }

        // 🔎 Barre de recherche toujours connectée à /search avec le texte extrait localement
        searchBar.setOnEditorActionListener { _, _, _ ->
            val query = searchBar.text.toString()
            if (query.isBlank()) {
                Toast.makeText(this, "Entrez une requête.", Toast.LENGTH_SHORT).show()
                return@setOnEditorActionListener true
            }

            val documentText = readTextFromAssetPdf("LettreELNO.pdf")
            val body = mapOf(
                "query" to query,
                "documents" to listOf(documentText)
            )
            Log.d("DEBUG", "Recherche avec : \"$query\" dans document de ${documentText.length} caractères.")

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.instance.search(body)
                    if (response.isSuccessful) {
                        val results = response.body()?.results ?: emptyList()
                        Log.d("DEBUG", "Résultats reçus : ${results.size} éléments.")

                        val displayText = results.joinToString(separator = "\n\n") { result ->
                            "➤ Score: ${"%.2f".format(result.score)}\n${result.document.take(150)}..."
                        }

                        textSearchResults.text = displayText
                    } else {
                        Log.e("ERREUR", "Erreur serveur /search : ${response.code()}")
                        textSearchResults.text = "Erreur serveur : ${response.code()}"
                    }
                } catch (e: Exception) {
                    Log.e("ERREUR", "Exception /search : ${e.localizedMessage}", e)
                    textSearchResults.text = "Erreur : ${e.localizedMessage}"
                }
            }

            true
        }
    }

    // 📘 Cette fonction reste utilisée uniquement pour /search
    private fun readTextFromAssetPdf(filename: String): String {
        val inputStream = assets.open(filename)
        val document = com.tom_roush.pdfbox.pdmodel.PDDocument.load(inputStream)
        val stripper = com.tom_roush.pdfbox.text.PDFTextStripper()
        val text = stripper.getText(document)
        document.close()
        return text
    }
}
