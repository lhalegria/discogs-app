package com.example.discogsapp.data.mapper

import com.example.discogsapp.data.response.ArtistDetailsResponse
import com.example.discogsapp.data.response.ArtistImageResponse
import com.example.discogsapp.data.response.ArtistReleaseSearchDataResponse
import com.example.discogsapp.data.response.ArtistReleaseSearchResponse
import com.example.discogsapp.data.response.ArtistSearchDataResponse
import com.example.discogsapp.data.response.ArtistSearchResponse
import com.example.discogsapp.data.response.PaginationResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersTest {
    @Test
    fun `ArtistSearchResponse toDomain maps pagination and artists`() {
        val response =
            ArtistSearchResponse(
                pagination = PaginationResponse(page = 1, perPage = 20, pages = 3, items = 50),
                results =
                    listOf(
                        ArtistSearchDataResponse(
                            id = 10,
                            title = "Daft Punk",
                            thumbnailUrl = "https://thumb",
                            type = "artist",
                        ),
                    ),
            )

        val result = response.toDomain()

        assertEquals(1, result.pagination.page)
        assertEquals(20, result.pagination.perPage)
        assertEquals(1, result.artists.size)
        assertEquals("Daft Punk", result.artists.first().title)
    }

    @Test
    fun `ArtistSearchDataResponse toDomain defaults nullable thumbnail`() {
        val response =
            ArtistSearchDataResponse(
                id = 11,
                title = "Justice",
                thumbnailUrl = null,
                type = "artist",
            )

        val result = response.toDomain()

        assertEquals(11, result.id)
        assertEquals("", result.thumbnailUrl)
    }

    @Test
    fun `ArtistDetailsResponse toDomain defaults nullable profile values`() {
        val response =
            ArtistDetailsResponse(
                id = 12,
                name = "Bonobo",
                profile = null,
                realName = null,
                urls = null,
                images = null,
            )

        val result = response.toDomain()

        assertEquals(12, result.id)
        assertEquals("", result.profile)
        assertEquals("", result.realName)
        assertEquals(emptyList<String>(), result.urls)
        assertEquals(emptyList<String>(), result.images.map { it.uri })
    }

    @Test
    fun `ArtistImageResponse toDomain defaults nullable fields`() {
        val response =
            ArtistImageResponse(
                uri = "https://full",
                smallUri = null,
                width = null,
                height = null,
            )

        val result = response.toDomain()

        assertEquals("https://full", result.uri)
        assertEquals("", result.smallUri)
        assertEquals(0, result.width)
        assertEquals(0, result.height)
    }

    @Test
    fun `ArtistReleaseSearchResponse toDomain maps pagination and releases`() {
        val response =
            ArtistReleaseSearchResponse(
                pagination = PaginationResponse(page = 2, perPage = 15, pages = 4, items = 60),
                results =
                    listOf(
                        ArtistReleaseSearchDataResponse(
                            id = 99,
                            title = "Homework",
                            year = "1997",
                            type = "release",
                            label = listOf("Virgin"),
                            genre = listOf("Electronic"),
                            thumbnailUrl = "https://thumb",
                        ),
                    ),
            )

        val result = response.toDomain()

        assertEquals(2, result.pagination.page)
        assertEquals(1, result.releases.size)
        assertEquals("Homework", result.releases.first().title)
    }

    @Test
    fun `ArtistReleaseSearchDataResponse toDomain defaults nullable values`() {
        val response =
            ArtistReleaseSearchDataResponse(
                id = 101,
                title = "Discovery",
                year = null,
                type = null,
                label = null,
                genre = null,
                thumbnailUrl = null,
            )

        val result = response.toDomain()

        assertEquals("", result.year)
        assertEquals("", result.type)
        assertEquals(emptyList<String>(), result.label)
        assertEquals(emptyList<String>(), result.genre)
        assertEquals("", result.thumbnailUrl)
    }

    @Test
    fun `PaginationResponse toDomain maps all fields`() {
        val response = PaginationResponse(page = 3, perPage = 10, pages = 8, items = 75)

        val result = response.toDomain()

        assertEquals(3, result.page)
        assertEquals(10, result.perPage)
        assertEquals(8, result.pages)
        assertEquals(75, result.items)
    }
}
