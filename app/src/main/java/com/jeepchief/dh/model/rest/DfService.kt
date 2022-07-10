package com.jeepchief.dh.model.rest

import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DfService {
    @GET("servers")
    suspend fun getServers(@Query("apikey") apiKey: String = NetworkConstants.API_KEY) : ServerDTO

    @GET("servers/all/characters")
    suspend fun getCharacters(
        @Query("characterName") characterName: String,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : CharacterDTO

    @GET("jobs")
    suspend fun getJobs(
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : JobDTO

    @GET("servers/{server}/characters/{character_id}/status")
    suspend fun getCharacterStatus(
        @Path("server") server: String,
        @Path("character_id") characterId: String,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : StatusDTO

    @GET("servers/{server}/characters/{character_id}/equip/equipment")
    suspend fun getEquipment(
        @Path("server") server: String,
        @Path("character_id") characterId: String,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : EquipmentDTO

    @GET("servers/{server}/characters/{character_id}/equip/avatar")
    suspend fun getAvatar(
        @Path("server") server: String,
        @Path("character_id") characterId: String,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : AvatarDTO
}