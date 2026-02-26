package com.example.discogsapp.data.mapper

import com.example.discogsapp.data.response.ArtistDetailsResponse
import com.example.discogsapp.data.response.ArtistImage
import com.example.discogsapp.data.response.ArtistRelease
import com.example.discogsapp.data.response.ArtistReleasesResponse
import com.example.discogsapp.data.response.ArtistSearchResponse
import com.example.discogsapp.data.response.ArtistSearchResult
import com.example.discogsapp.data.response.Pagination
import com.example.discogsapp.domain.model.ArtistDetailsModel
import com.example.discogsapp.domain.model.ArtistImageModel
import com.example.discogsapp.domain.model.ArtistReleaseModel
import com.example.discogsapp.domain.model.ArtistReleasesResultModel
import com.example.discogsapp.domain.model.ArtistSearchResultModel
import com.example.discogsapp.domain.model.ArtistSummaryModel
import com.example.discogsapp.domain.model.PaginationModel

fun ArtistSearchResponse.toDomain(): ArtistSearchResultModel =
    ArtistSearchResultModel(
        pagination = pagination.toDomain(),
        artists = results.map(ArtistSearchResult::toDomain),
    )

fun ArtistSearchResult.toDomain(): ArtistSummaryModel =
    ArtistSummaryModel(
        id = id,
        title = title,
        thumbnailUrl = thumbnailUrl.orEmpty(),
        type = type,
    )

fun ArtistDetailsResponse.toDomain(): ArtistDetailsModel =
    ArtistDetailsModel(
        id = id,
        name = name,
        profile = profile.orEmpty(),
        realName = realName.orEmpty(),
        urls = urls.orEmpty(),
        images = images.orEmpty().map(ArtistImage::toDomain),
    )

fun ArtistImage.toDomain(): ArtistImageModel =
    ArtistImageModel(
        uri = uri,
        smallUri = smallUri.orEmpty(),
        width = width ?: 0,
        height = height ?: 0,
    )

fun ArtistReleasesResponse.toDomain(): ArtistReleasesResultModel =
    ArtistReleasesResultModel(
        pagination = pagination.toDomain(),
        releases = releases.map(ArtistRelease::toDomain),
    )

fun ArtistRelease.toDomain(): ArtistReleaseModel =
    ArtistReleaseModel(
        id = id,
        title = title,
        year = year ?: 0,
        role = role.orEmpty(),
        type = type.orEmpty(),
        thumbnailUrl = thumbnailUrl.orEmpty(),
    )

fun Pagination.toDomain(): PaginationModel =
    PaginationModel(
        page = page,
        perPage = perPage,
        pages = pages,
        items = items,
    )
