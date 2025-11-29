package com.jeepchief.dh.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE 'CharactersEntity' ADD COLUMN 'fame' INTEGER NOT NULL default 0")
        db.execSQL("ALTER TABLE 'CharactersEntity' ADD COLUMN 'guildName' TEXT NOT NULL default ''")
        db.execSQL("ALTER TABLE 'CharactersEntity' ADD COLUMN 'adventureName' TEXT NOT NULL default ''")
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE 'CharactersEntity' ADD COLUMN 'updateTime' INTEGER NOT NULL default 0")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 1. 기존 테이블 이름 변경
        db.execSQL("""
            ALTER TABLE CharactersEntity
            RENAME TO CharactersEntity_old;
        """.trimIndent())

        // 2. 새 테이블 생성 (id 제거 + composite PK)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS CharactersEntity (
                serverId TEXT NOT NULL,
                characterId TEXT NOT NULL,
                characterName TEXT NOT NULL,
                level INTEGER NOT NULL,
                jobId TEXT NOT NULL,
                jobGrowId TEXT NOT NULL,
                jobName TEXT NOT NULL,
                jobGrowName TEXT NOT NULL,
                fame INTEGER NOT NULL,
                guildName TEXT NOT NULL,
                adventureName TEXT NOT NULL,
                PRIMARY KEY(serverId, characterId)
            );
        """.trimIndent())

        // 3. 기존 데이터 복사(id 제외)
        db.execSQL("""
            INSERT INTO CharactersEntity (
                serverId,
                characterId,
                characterName,
                level,
                jobId,
                jobGrowId,
                jobName,
                jobGrowName,
                fame,
                guildName,
                adventureName
            )
            SELECT
                serverId,
                characterId,
                characterName,
                level,
                jobId,
                jobGrowId,
                jobName,
                jobGrowName,
                fame,
                guildName,
                adventureName
            FROM CharactersEntity_old;
        """.trimIndent())

        // 4. 기존 테이블 삭제
        db.execSQL("""
            DROP TABLE CharactersEntity_old;
        """.trimIndent())
    }
}

val MIGRATIONS = arrayOf(
    MIGRATION_3_4,
    MIGRATION_4_5,
    MIGRATION_5_6
)