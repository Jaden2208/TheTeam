package com.whalez.theteam.data

data class User(
    val uid: String = "",
    val name: String = "",
    val age: String = "",
    val area: String = "",
    val introduce: String = "",
    val profileImageUrl: String = "",
    val team: String = "소속 팀 없음"
)