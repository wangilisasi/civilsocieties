package tz.co.vanuserve.civilsocieties.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tz.co.vanuserve.civilsocieties.api.CivilSocietyApi
import tz.co.vanuserve.civilsocieties.data.CivilSocietyDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit():Retrofit=
        Retrofit.Builder()
            .baseUrl(CivilSocietyApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideCivilSocietyApi(retrofit:Retrofit):CivilSocietyApi=
        retrofit.create(CivilSocietyApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): CivilSocietyDatabase =
        Room.databaseBuilder(app,CivilSocietyDatabase::class.java,"restaurant_database")
            .fallbackToDestructiveMigration()  //I addaed this later after tempering with the database versions
            .build()

}