package com.flaao0.notes.di

import android.content.Context
import com.flaao0.notes.data.local.NotesDatabase
import com.flaao0.notes.data.repository.NotesRepositoryImpl
import com.flaao0.notes.domain.repository.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {


    @Binds
    fun bindNotesRepository(
        impl: NotesRepositoryImpl
    ): NotesRepository


    companion object {

        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context
        ): NotesDatabase {
            return NotesDatabase.getInstance(context)
        }

        @Singleton
        @Provides
        fun provideRepository(
            notesDatabase: NotesDatabase
        ): NotesRepositoryImpl {
            return NotesRepositoryImpl.getInstance(notesDatabase)
        }

    }
}