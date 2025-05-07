package com.project.ia_imperiatec

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var searchBar: EditText
    private lateinit var buttonImport: Button
    private lateinit var buttonAnalyze: Button
    private lateinit var textSummary: TextView
    private lateinit var textSearchResults: TextView

    private var selectedDocumentUri: Uri? = null

    private val pickPdfLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedDocumentUri = result.data?.data
                Toast.makeText(this, getString(R.string.button_import) + " OK", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchBar = findViewById(R.id.searchBar)
        buttonImport = findViewById(R.id.buttonImport)
        buttonAnalyze = findViewById(R.id.buttonAnalyze)
        textSummary = findViewById(R.id.textSummary)
        textSearchResults = findViewById(R.id.textSearchResults)

        buttonImport.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "application/pdf"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            pickPdfLauncher.launch(intent)
        }

        buttonAnalyze.setOnClickListener {
            textSummary.text = getString(R.string.text_summary_placeholder)
        }

        searchBar.setOnEditorActionListener { _, _, _ ->
            val query = searchBar.text.toString()
            textSearchResults.text = getString(R.string.text_search_placeholder) + "\n\nRequête : $query"
            true
        }
    }
}
