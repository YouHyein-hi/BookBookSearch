package com.example.bookbooksearch.dataClass

data class DataDetail(
    val lastBuildDate: String,  // 검색 결과를 생성한 시간
    val total: Int,             // 총 검색 결과 개수
    val start: Int,             // 검색 시작 위치
    val display: Int,           // 한 번에 표시할 검색 결과 개수
    val items: List<DetailItem>,     // 개별 검색 결과
)