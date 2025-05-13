package com.project.ia_imperiatec.network

<<<<<<< HEAD
import com.project.ia_imperiatec.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IAService {

    @POST("/summarize")
    suspend fun summarize(@Body body: Map<String, String>): Response<SummaryResponse>

    @POST("/search")
    suspend fun search(@Body body: Map<String, Any>): Response<SearchResponse>

    @POST("/ask")
    suspend fun ask(@Body body: Map<String, String>): Response<AskResponse>
}
=======
>>>>>>> 95b63e6271f1fa7a507039735f05ae2e1783d63a
