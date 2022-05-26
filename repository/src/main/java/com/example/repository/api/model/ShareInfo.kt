package com.example.repository.api.model

import com.google.gson.annotations.SerializedName

data class ShareInfo(
    val removeGeoData: Boolean = false,
    val secret: String? = null,
    val nonPublic: Boolean = false,
    @SerializedName("subscribed_collection_publicId") val subscribedCollectionPublicId: String? = null,
    val authorization: String? = "READ",
    val subscribers: Array<Subscriber>? = null,
    val uri: String? = null,
    val shareDate: Long = 0L,
    val subscriptionDate: Long = 0L,
    val owner: String? = null,
    val ownerFullName: String? = null,
    val admin: Boolean = false
)