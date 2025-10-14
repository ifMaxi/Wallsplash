package com.maxidev.wallsplash.common.di

import android.content.Context
import androidx.room.Room
import com.maxidev.wallsplash.common.data.local.WallsplashDataBase
import com.maxidev.wallsplash.common.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providesRoomDatabase(
        @ApplicationContext context: Context
    ): WallsplashDataBase {
        return Room.databaseBuilder(
            context = context,
            klass = WallsplashDataBase::class.java,
            name = DATABASE_NAME
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }
}