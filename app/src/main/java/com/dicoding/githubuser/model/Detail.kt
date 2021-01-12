package com.dicoding.githubuser.model

data class Detail(
    var htmlUrl: String? = null,
    var name: String? = null,
    var company: String? = null,
    var location: String? = null,
    var publicRepos: Int = 0,
    var followers: Int = 0,
    var following: Int = 0
)