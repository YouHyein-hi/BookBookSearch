package com.example.bookbooksearch.dataClass

data class Item(
    val title: String,             // 책 제목
    val link: String,              // 네이버 도서 정보 URL
    val image: String,             // 섬네일 이미지 URL
    val author: String,            // 저자 이름
    val discount: String,          // 판매 가격
    val publisher: String,         // 출판사
    val isbn: String,              // ISBN
    val description: String,       // 네이버 도서의 책 소개
    val pubdate: String,           // 출간일
)