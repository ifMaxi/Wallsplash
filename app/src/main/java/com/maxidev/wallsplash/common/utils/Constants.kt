package com.maxidev.wallsplash.common.utils

import com.maxidev.wallsplash.BuildConfig

object Constants {

    // Network related constants.
    const val BASE_URL = "https://api.unsplash.com/"
    const val CONTENT_TYPE = "application/json"
    const val TIMER_OUT = 15L
    const val API_KEY = BuildConfig.apiKey
    const val AUTHORIZATION = "Authorization"
    const val CLIENT_ID = "Client-ID"
    const val CACHE_CONTROL = "Cache-Control"
    const val CACHE_MAX_AGE = 2
    const val FILE_CACHE_NAME = "wallsplash-cache"

    // Api related constants.
    const val PHOTOS = "photos"
    const val PHOTO_ID = "photos/{id}"
    const val SEARCH_PHOTOS = "search/photos"
    const val SEARCH_COLLECTIONS = "search/collections"
    const val COLLECTIONS = "collections"
    const val COLLECTION_ID = "collections/{id}"
    const val COLLECTION_ID_PHOTOS = "collections/{id}/photos"

    // Paging related constants
    const val PAGE_NUMBER = 1
    const val PER_PAGE = 10

    // Utility constants
    const val COUNT = 1

    // Room related constants
    const val DATABASE_NAME = "wallsplash_database"
    const val PHOTOS_TABLE = "photos_table"
    const val COLLECTIONS_TABLE = "collections_table"
    const val FAVORITE_TABLE = "favorite_table"
}