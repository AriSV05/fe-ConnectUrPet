package com.example.connecturpet.repository

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
import com.example.connecturpet.service.PetService
import retrofit2.Response
import retrofit2.http.Body

class PetRepository constructor(
    private val petService: PetService
) {
    suspend fun giverAddPet(pet: AddPetRequest) = petService.giverAddPet(pet)

    suspend fun giverEditPet(pet: EditPetRequest): Response<DefaultResponse> =
        petService.giverEditPet(pet)

    suspend fun onePet(pet: OnePetRequest): Response<OnePetResponse> = petService.onePet(pet)
    suspend fun onePetEdit(pet: OnePetRequest): Response<OnePetEditResponse> = petService.onePetEdit(pet)

    suspend fun petsOfGiver(pet: PetsOfGiverRequest): Response<PetsOfGiverResponse> =
        petService.petsOfGiver(pet)

    suspend fun giversPets(): Response<AllGiversPetsResponse> = petService.giversPets()

    suspend fun vaccines(petRequest: PetVaccinesRequest): Response<VaccinesResponse> =
        petService.vaccines(petRequest)

    suspend fun petVaccines(pet: PetVaccinesRequest): Response<PetVaccinesResponse> =
        petService.petVaccines(pet)

    suspend fun addPetVaccine(pet: AddPetVaccineRequest): Response<DefaultResponse> =
        petService.addPetVaccine(pet)

    suspend fun speciesAndBreeds(): Response<SpeciesAndBreeds> = petService.speciesAndBreeds()
    suspend fun recommendations(pet: RecommendationInput):Response<PetCareInfo> = petService.recommendations(pet)
}