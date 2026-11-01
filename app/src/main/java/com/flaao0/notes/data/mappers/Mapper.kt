package com.flaao0.notes.data.mappers

import com.flaao0.notes.data.local.entity.ContentItemDbModel
import com.flaao0.notes.data.local.entity.NoteDbModel
import com.flaao0.notes.domain.entity.ContentItem
import com.flaao0.notes.domain.entity.Note
import kotlinx.serialization.json.Json

fun Note.toDbModel(): NoteDbModel {
    val contentAsString = Json.encodeToString(content.toContentItemDbModels())
    return NoteDbModel(
        id, title, contentAsString, updatedAt, isPinned
    )
}

fun List<ContentItem>.toContentItemDbModels(): List<ContentItemDbModel> {
    return this.map { contentItem ->
        when (contentItem) {
            is ContentItem.Image -> ContentItemDbModel.Image(url = contentItem.url)
            is ContentItem.Text -> ContentItemDbModel.Text(text = contentItem.text)
        }
    }
}

fun List<ContentItemDbModel>.toContentItem(): List<ContentItem> {
    return this.map { contentItem ->
        when (contentItem) {
            is ContentItemDbModel.Image -> ContentItem.Image(url = contentItem.url)
            is ContentItemDbModel.Text -> ContentItem.Text(text = contentItem.text)
        }
    }
}

fun NoteDbModel.toEntity(): Note {
    val contentItemDbModels = Json.decodeFromString<List<ContentItemDbModel>>(content).toContentItem()
    return Note(
        id, title, contentItemDbModels, updatedAt, isPinned
    )
}