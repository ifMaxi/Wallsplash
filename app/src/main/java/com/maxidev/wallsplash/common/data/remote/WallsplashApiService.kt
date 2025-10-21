package com.maxidev.wallsplash.common.data.remote

import com.maxidev.wallsplash.common.utils.Constants.COLLECTIONS
import com.maxidev.wallsplash.common.utils.Constants.COLLECTION_ID
import com.maxidev.wallsplash.common.utils.Constants.COLLECTION_ID_PHOTOS
import com.maxidev.wallsplash.common.utils.Constants.PAGE_NUMBER
import com.maxidev.wallsplash.common.utils.Constants.PER_PAGE
import com.maxidev.wallsplash.common.utils.Constants.PHOTOS
import com.maxidev.wallsplash.common.utils.Constants.PHOTO_ID
import com.maxidev.wallsplash.common.utils.Constants.SEARCH_COLLECTIONS
import com.maxidev.wallsplash.common.utils.Constants.SEARCH_PHOTOS
import com.maxidev.wallsplash.feature.collections.data.model.remote.CollectionDataDto
import com.maxidev.wallsplash.feature.collections.data.model.remote.CollectionPhotosDto
import com.maxidev.wallsplash.feature.detail.data.model.remote.PhotoDetailDto
import com.maxidev.wallsplash.feature.photos.data.model.remote.CollectionsDto
import com.maxidev.wallsplash.feature.photos.data.model.remote.PhotosDto
import com.maxidev.wallsplash.feature.search.data.model.remote.SearchCollectionsDto
import com.maxidev.wallsplash.feature.search.data.model.remote.SearchPhotosDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WallsplashApiService {

    /**
     * Gets a list of photos.
     *
     * @param page Page number to retrieve. (Optional; default: 1)
     * @param perPage Number of items per page. (Optional; default: 10)
     */
    @GET(PHOTOS)
    suspend fun getPhotos(
        @Query("page") page: Int? = PAGE_NUMBER,
        @Query("per_page") perPage: Int? = PER_PAGE
    ): List<PhotosDto>

    /**
     * Get a single photo.
     *
     * @param id The photo ID in string format. (Required)
     */
    @GET(PHOTO_ID)
    suspend fun getPhotoId(@Path("id") id: String): PhotoDetailDto

    /**
     * Get photos according to the given criteria.
     *
     * @param query Search terms.
     * @param orderBy How to sort the photos. (Optional; default: relevant). Valid values are latest and relevant.
     * @param contentFilter Limit results by content safety. (Optional; default: low). Valid values are low and high.
     * @param page Number to retrieve. (Optional; default: 1)
     * @param perPage Number of items per page. (Optional; default: 10)
     */
    @GET(SEARCH_PHOTOS)
    suspend fun getSearchPhotos(
        @Query("query") query: String,
        @Query("order_by") orderBy: String? = "relevant",
        @Query("content_filter") contentFilter: String? = "low",
        @Query("page") page: Int? = PAGE_NUMBER,
        @Query("per_page") perPage: Int? = PER_PAGE
    ): SearchPhotosDto

    /**
     * Get collections according to the given criteria.
     *
     * @param query Search terms.
     * @param page Number to retrieve. (Optional; default: 1)
     * @param perPage Number of items per page. (Optional; default: 10)
     */
    @GET(SEARCH_COLLECTIONS)
    suspend fun getSearchCollections(
        @Query("query") query: String,
        @Query("page") page: Int? = PAGE_NUMBER,
        @Query("per_page") perPage: Int? = PER_PAGE
    ): SearchCollectionsDto

    /**
     * Get a list of collections.
     *
     * @param page Number to retrieve. (Optional; default: 1)
     * @param perPage Number of items per page. (Optional; default: 10)
     */
    @GET(COLLECTIONS)
    suspend fun getCollections(
        @Query("page") page: Int? = PAGE_NUMBER,
        @Query("per_page") perPage: Int? = PER_PAGE
    ): List<CollectionsDto>

    /**
     * Retrieve a single collection.
     *
     * @param id The collection ID in Int format. (Required)
     */
    @GET(COLLECTION_ID)
    suspend fun getCollectionId(@Path("id") id: String): CollectionDataDto

    /**
     * Get a list of photos in a collection.
     *
     * @param id The collection ID in string format. (Required)
     * @param page Page number to retrieve. (Optional; default: 1)
     * @param perPage Number of items per page. (Optional; default: 10)
     */
    @GET(COLLECTION_ID_PHOTOS)
    suspend fun getCollectionPhotosById(
        @Path("id") id: String,
        @Query("page") page: Int? = PAGE_NUMBER,
        @Query("per_page") perPage: Int? = PER_PAGE
    ): List<CollectionPhotosDto>
}