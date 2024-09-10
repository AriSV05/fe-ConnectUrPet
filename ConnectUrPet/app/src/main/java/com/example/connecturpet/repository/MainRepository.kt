package com.example.connecturpet.repository

import com.example.connecturpet.model.NotisRequest
import com.example.connecturpet.model.NotisResult
import com.example.connecturpet.model.ReactionInput
import com.example.connecturpet.model.ReviewInput
import com.example.connecturpet.model.UserProfile
import com.example.connecturpet.service.UsersServices
import retrofit2.Response
import retrofit2.http.Body

class MainRepository constructor(
    private val usersServices: UsersServices
) {
    suspend fun getAdopterById(id: String) = usersServices.getAdopter(id)
    suspend fun getGiverById(id: String) = usersServices.getGiver(id)
    suspend fun getGiverReviews(id: String) = usersServices.getReviews(id)
    suspend fun editAdopter(id: String, adopter: UserProfile) = usersServices.editAdopter(id, adopter)
    suspend fun editGiver(id: String, giver: UserProfile) = usersServices.editGiver(id ,giver)
    suspend fun addReview(review: ReviewInput) = usersServices.addReview(review)
    suspend fun addReaction(reaction: ReactionInput) = usersServices.addReaction(reaction)
    suspend fun readNotis(notisRequest: NotisRequest): Response<NotisResult> = usersServices.readNotis(notisRequest)
}