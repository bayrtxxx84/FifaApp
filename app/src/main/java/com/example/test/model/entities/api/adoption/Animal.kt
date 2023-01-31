package com.example.test.model.entities.api.adoption

import com.example.test.model.entities.api.adoption.generated.*

data class Animal(
    val _links: Links,
    val age: String,
    val attributes: Attributes,
    val breeds: Breeds,
    val coat: String,
    val colors: Colors,
    val contact: Contact,
    val description: String,
    val distance: Any,
    val environment: Environment,
    val gender: String,
    val id: Int,
    val name: String,
    val organization_animal_id: String,
    val organization_id: String,
    val photos: List<Photo>,
    val primary_photo_cropped: PrimaryPhotoCropped,
    val published_at: String,
    val size: String,
    val species: String,
    val status: String,
    val status_changed_at: String,
    val tags: List<Any>,
    val type: String,
    val url: String,
    val videos: List<Video>
)