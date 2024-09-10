package com.example.connecturpet.service

import com.example.connecturpet.model.AddPetRequest
import com.example.connecturpet.model.AddPetVaccineRequest
import com.example.connecturpet.model.AllGiversPetsResponse
import com.example.connecturpet.model.DefaultResponse
import com.example.connecturpet.model.EditPetRequest
import com.example.connecturpet.model.OnePetEditResponse
import com.example.connecturpet.model.OnePetRequest
import com.example.connecturpet.model.OnePetResponse
import com.example.connecturpet.model.PetCareInfo
import com.example.connecturpet.model.PetVaccinesRequest
import com.example.connecturpet.model.PetVaccinesResponse
import com.example.connecturpet.model.PetsOfGiverRequest
import com.example.connecturpet.model.PetsOfGiverResponse
import com.example.connecturpet.model.RecommendationInput
import com.example.connecturpet.model.SpeciesAndBreeds
import com.example.connecturpet.model.VaccinesResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PetService {
    @POST("/v1/api/pet/giverAddPet")
    suspend fun giverAddPet(@Body pet:AddPetRequest): Response<DefaultResponse>
    @POST("/v1/api/pet/giverEditPet")
    suspend fun giverEditPet(@Body pet:EditPetRequest): Response<DefaultResponse>
    @POST("/v1/api/pet/onePet")
    suspend fun onePet(@Body pet: OnePetRequest): Response<OnePetResponse>
    @POST("/v1/api/pet/onePetEdit")
    suspend fun onePetEdit(@Body pet: OnePetRequest): Response<OnePetEditResponse>
    @POST("/v1/api/pet/petsOfGiver")
    suspend fun petsOfGiver(@Body pet: PetsOfGiverRequest): Response<PetsOfGiverResponse>
    @GET("/v1/api/pet/giversPets")
    suspend fun giversPets(): Response<AllGiversPetsResponse>
    @POST("/v1/api/pet/vaccines")
    suspend fun vaccines(@Body pet: PetVaccinesRequest): Response<VaccinesResponse>
    @POST("/v1/api/pet/petVaccines")
    suspend fun petVaccines(@Body pet: PetVaccinesRequest): Response<PetVaccinesResponse>
    @POST("/v1/api/pet/addPetVaccines")
    suspend fun addPetVaccine(@Body pet: AddPetVaccineRequest): Response<DefaultResponse>
    @GET("/v1/api/pet/speciesAndBreeds")
    suspend fun speciesAndBreeds(): Response<SpeciesAndBreeds>
    @POST("/v1/api/pet/recommendations")
    suspend fun recommendations(@Body pet: RecommendationInput): Response<PetCareInfo>

    companion object {
        private var petService: PetService? = null

        fun getInstance() : PetService {
            if (petService == null) {
                petService = ServiceBuilder.buildService(PetService::class.java)
            }
            return petService!!
        }
        fun reloadInstance() {
            petService = ServiceBuilder.buildService(PetService::class.java)
        }
    }
}