package com.project.ia_imperiatec.models

<<<<<<< HEAD
data class SearchResponse(
    val results: List<SearchResult>
)

data class SearchResult(
    val document: String,
    val score: Float
)
=======
data class SearchResponse(val best_match: String, val score: Float)
>>>>>>> 95b63e6271f1fa7a507039735f05ae2e1783d63a
