package com.jeepchief.dh.model.rest

import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.*
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

    @GET("servers/{server}/characters/{character_id}/equip/creature")
    suspend fun getCreature(
        @Path("server") server: String,
        @Path("character_id") characterId: String,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : CreatureDTO

    @GET("servers/{server}/characters/{character_id}/equip/flag")
    suspend fun getFlag(
        @Path("server") server: String,
        @Path("character_id") characterId: String,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : FlagDTO

    @GET("items")
    suspend fun getSearchItems(
        @Query("itemName") itemName: String,
        @Query("limit") limit: Int = 30,
        @Query("wordType") wordType: String = NetworkConstants.WORD_TYPE,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : ItemSearchDTO

    @GET("items/{item_id}")
    suspend fun getItemInfo(
        @Path("item_id") itemId: String,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : ItemsDTO

    @GET("skills/{jobId}")
    suspend fun getSkills(
        @Path("jobId") jobId: String,
        @Query("jobGrowId") jobGrowId: String,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : SkillDTO

    @GET("skills/{jobId}/{skillId}")
    suspend fun getSkillInfo(
        @Path("jobId") jodId: String,
        @Path("skillId") skillId: String,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : SkillInfoDTO
}