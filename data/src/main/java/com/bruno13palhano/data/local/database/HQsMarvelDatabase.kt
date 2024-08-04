package com.bruno13palhano.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bruno13palhano.data.local.data.dao.CharacterDao
import com.bruno13palhano.data.local.data.dao.CharacterSummaryDao
import com.bruno13palhano.data.local.data.dao.ComicOffsetDao
import com.bruno13palhano.data.local.data.dao.ComicsDao
import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.model.CharacterSummary
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.ComicOffset

@Database(
    entities = [
        Comic::class,
        Character::class,
        CharacterSummary::class,
        ComicOffset::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class HQsMarvelDatabase : RoomDatabase() {
    abstract val comicsDao: ComicsDao
    abstract val characterDao: CharacterDao
    abstract val characterSummaryDao: CharacterSummaryDao
    abstract val comicOffsetDao: ComicOffsetDao
}