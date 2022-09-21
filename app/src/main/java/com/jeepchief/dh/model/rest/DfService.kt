package com.jeepchief.dh.model.rest

import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DfService {
    @GET("servers")
    suspend fun getServers(@Query("apikey") apiKey: String = NetworkConstants.API_KEY) : ServerDTO

    //https://api.neople.co.kr/df/servers/all/characters?characterName=고산노블&apikey=07ZVouD2WFbFbtulj2JiKm1CVNL07wUi
    @GET("servers/{serverId}/characters")
    suspend fun getCharacters(
        @Path("serverId") serverId: String,
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
        @Query("wordType") wordType: String,
        @Query("q") q: String,
        @Query("limit") limit: Int = 30,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : ItemSearchDTO

    @GET("items")
    fun getSearchItemsTest(
        @Query("itemName") itemName: String,
        @Query("wordType") wordType: String,
        @Query("q") q: String,
        @Query("limit") limit: Int = 30,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : Call<ItemSearchDTO>

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

    @GET("servers/{serverId}/characters/{characterId}/equip/talisman")
    suspend fun getTalisman(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : TalismanDTO

    @GET("servers/{serverId}/characters/{characterId}/skill/buff/equip/equipment")
    suspend fun getBuffEquip(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : BuffEquipDTO

    @GET("servers/{serverId}/characters/{characterId}")
    fun getTalismanTest(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String,
        @Query("apikey") apiKey: String = NetworkConstants.API_KEY
    ) : Call<TalismanDTO>
}