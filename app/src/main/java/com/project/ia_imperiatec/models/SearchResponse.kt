package com.project.ia_imperiatec.models

data class SearchResponse(
    val results: List<SearchResult>
)

data class SearchResult(
    val document: String,
    val score: Float
)
