package com.example.bookbooksearch.retrofit

import com.example.bookbooksearch.dataClass.DataDetail
import com.example.bookbooksearch.dataClass.DataMain
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverAPI {
    @GET("v1/search/book.json?")
    fun getSearchBooks(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,            // 검색어, UTF-8로 인코딩
        @Query("display") display: Int? = 10,   // 한 번에 표시할 검색 결과 개수 (기본값:10, 최댓값:100)
        @Query("start") start: Int? = 1,       // 검색 시작 위치 (기본값:1, 최댓값:100)
        @Query("sort") sort: String? = "sim"       // 검색 결과 정렬 방법 (sim:정확도순으로 내림차순, 기본값! / date: 출간일순으로 내림차순)
    ): Call<DataMain>

    @GET("v1/search/book_adv.json?")
    fun getSearchBooksDetail(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("d_isbn") d_isbn: String,
        @Query("display") display: Int? = 1,   // 한 번에 표시할 검색 결과 개수 (기본값:10, 최댓값:100)
        @Query("start") start: Int? = 1,       // 검색 시작 위치 (기본값:1, 최댓값:100)
        /*        @Query("query") query: String? = null,
                @Query("sort") sort: String? = "sim",
                @Query("d_titl") d_titl: String? = null,*/
    ): Call<DataDetail>
}