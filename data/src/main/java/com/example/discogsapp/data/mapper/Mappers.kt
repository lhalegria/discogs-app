package com.example.discogsapp.data.mapper

import com.example.discogsapp.data.response.ArtistDetailsResponse
import com.example.discogsapp.data.response.ArtistImageResponse
import com.example.discogsapp.data.response.ArtistReleaseSearchDataResponse
import com.example.discogsapp.data.response.ArtistReleaseSearchResponse
import com.example.discogsapp.data.response.ArtistSearchDataResponse
import com.example.discogsapp.data.response.ArtistSearchResponse
import com.example.discogsapp.data.response.PaginationResponse
import com.example.discogsapp.domain.model.ArtistDetailModel
import com.example.discogsapp.domain.model.ArtistImageModel
import com.example.discogsapp.domain.model.ArtistReleaseModel
import com.example.discogsapp.domain.model.ArtistReleaseSearchModel
import com.example.discogsapp.domain.model.ArtistSearchModel
import com.example.discogsapp.domain.model.ArtistSummaryModel
import com.example.discogsapp.domain.model.PaginationModel

fun ArtistSearchResponse.toDomain(): ArtistSearchModel =
    ArtistSearchModel(
        pagination = pagination.toDomain(),
        artists = results.map(ArtistSearchDataResponse::toDomain),
    )

fun ArtistSearchDataResponse.toDomain(): ArtistSummaryModel =
    ArtistSummaryModel(
        id = id,
        title = title,
        thumbnailUrl = thumbnailUrl.orEmpty(),
        type = type,
    )

fun ArtistDetailsResponse.toDomain(): ArtistDetailModel =
    ArtistDetailModel(
        id = id,
        name = name,
        profile = profile.orEmpty(),
        realName = realName.orEmpty(),
        urls = urls.orEmpty(),
        images = images.orEmpty().map(ArtistImageResponse::toDomain),
    )

fun ArtistImageResponse.toDomain(): ArtistImageModel =
    ArtistImageModel(
        uri = uri,
        smallUri = smallUri.orEmpty(),
        width = width ?: 0,
        height = height ?: 0,
    )

fun ArtistReleaseSearchResponse.toDomain(): ArtistReleaseSearchModel =
    ArtistReleaseSearchModel(
        pagination = pagination.toDomain(),
        releases = results.map(ArtistReleaseSearchDataResponse::toDomain),
    )

fun ArtistReleaseSearchDataResponse.toDomain(): ArtistReleaseModel =
    ArtistReleaseModel(
        id = id,
        title = title,
        year = year.orEmpty(),
        type = type.orEmpty(),
        thumbnailUrl = thumbnailUrl.orEmpty(),
        label = label.orEmpty(),
        genre = genre.orEmpty(),
    )

fun PaginationResponse.toDomain(): PaginationModel =
    PaginationModel(
        page = page,
        perPage = perPage,
        pages = pages,
        items = items,
    )
