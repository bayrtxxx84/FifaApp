package com.example.test.model.entities.api.pets

import com.example.test.model.entities.api.pets.generated.LinksX

data class Pagination(
    val _links: LinksX,
    val count_per_page: Int,
    val current_page: Int,
    val total_count: Int,
    val total_pages: Int
)