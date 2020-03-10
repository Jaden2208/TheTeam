package com.whalez.theteam.data

data class Team(
    val teamName: String = "",
    val teamOwner: String = "",
    val teamIntroduce: String = "",
    val teamLogoUrl: String = "",
    val teamMembers: List<String> = ArrayList()
)