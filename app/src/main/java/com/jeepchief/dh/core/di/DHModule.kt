package com.jeepchief.dh.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jeepchief.dh.core.database.DhDatabase
import com.jeepchief.dh.core.database.characters.CharacterDAO
import com.jeepchief.dh.core.database.metadata.ServersDAO
import com.jeepchief.dh.core.database.recent.RecentSearchDAO
import com.jeepchief.dh.core.network.DfService
import com.jeepchief.dh.core.repository.DhApiRepository
import com.jeepchief.dh.core.network.NetworkConstants
import com.jeepchief.dh.core.repository.DhCharacterRepository
import com.jeepchief.dh.core.repository.DhRecentRepository
import com.jeepchief.dh.core.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DHModule {
    @Provides
    @Singleton
    fun provideDfService(): DfService {
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().apply {
                addNetworkInterceptor(HttpLoggingInterceptor{ message -> Log.d(message) }.apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader(
                            "apiKey",
                            NetworkConstants.API_KEY
                        )
                        .build()
                    chain.proceed(request)
                }
                connectTimeout(10, TimeUnit.SECONDS)
            }.build())
            .build()
            .create(DfService::class.java)
    }

    @Provides
    fun provideApiRepository(dfService: DfService): DhApiRepository =
        DhApiRepository(dfService)

    @Provides
    fun provideCharacterRepository(characterDAO: CharacterDAO): DhCharacterRepository =
        DhCharacterRepository(characterDAO)

    @Provides
    fun provideRecentSearchRepository(recentSSearchDAO: RecentSearchDAO): DhRecentRepository =
        DhRecentRepository(recentSSearchDAO)

    @Provides
    @Singleton
    fun provideDfDatabase(@ApplicationContext context: Context): DhDatabase =
        Room.databaseBuilder(
            context,
            DhDatabase::class.java,
            "DH.db"
        )
            .addMigrations(
                object : Migration(3, 4) {
                    override fun migrate(db: SupportSQLiteDatabase) {
                        db.execSQL("ALTER TABLE 'CharactersEntity' ADD COLUMN 'fame' INTEGER NOT NULL default 0")
                        db.execSQL("ALTER TABLE 'CharactersEntity' ADD COLUMN 'guildName' TEXT NOT NULL default ''")
                        db.execSQL("ALTER TABLE 'CharactersEntity' ADD COLUMN 'adventureName' TEXT NOT NULL default ''")
                    }
                },
                object : Migration(4, 5) {
                    override fun migrate(db: SupportSQLiteDatabase) {
                        db.execSQL("ALTER TABLE 'CharactersEntity' ADD COLUMN 'updateTime' INTEGER NOT NULL default 0")
                    }
                }
            )
            .build()

    @Provides
    fun provideServerDao(db: DhDatabase): ServersDAO =
        db.getServersDAO()

    @Provides
    fun provideCharacterDao(db: DhDatabase): CharacterDAO =
        db.getCharactersDAO()

    @Provides
    fun provideRecentSearchDao(db: DhDatabase): RecentSearchDAO =
        db.getRecentSearchDAO()
}