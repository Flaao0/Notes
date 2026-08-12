package com.example.notes.domain.entity

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val updatedAt: Long,
    val isPinned: Boolean
)
