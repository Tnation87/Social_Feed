package com.example.repos.injection

import com.example.data.api.ApiServiceInteractor
import com.example.data.firebaseStorage.FirebaseStorageInteractor
import com.example.repos.feed.PostsRepo
import com.example.repos.feed.PostsRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ReposModule {

    @Provides
    @Singleton
    fun providePostsRepo(
        firebaseStorageInteractor: FirebaseStorageInteractor,
        apiService: ApiServiceInteractor
    ): PostsRepo {
        return PostsRepoImpl(
            apiService,
            firebaseStorageInteractor
        )
    }

}