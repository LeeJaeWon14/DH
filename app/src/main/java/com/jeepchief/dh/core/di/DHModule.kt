package com.jeepchief.dh.core.di

import android.content.Context
import androidx.room.Room
import com.jeepchief.dh.core.database.DhDatabase
import com.jeepchief.dh.core.database.characters.CharacterDAO
import com.jeepchief.dh.core.database.metadata.ServersDAO
import com.jeepchief.dh.core.network.DfService
import com.jeepchief.dh.core.network.DhApiRepository
import com.jeepchief.dh.core.network.NetworkConstants
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

@Module
@InstallIn(SingletonComponent::class)
object DHModule {
    @Provides
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
    fun provideDfDatabase(@ApplicationContext context: Context): DhDatabase =
        Room.databaseBuilder(
            context,
            DhDatabase::class.java,
            "DH.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideServerDao(db: DhDatabase): ServersDAO =
        db.getServersDAO()

    @Provides
    fun provideCharacterDao(db: DhDatabase): CharacterDAO =
        db.getCharactersDAO()
}