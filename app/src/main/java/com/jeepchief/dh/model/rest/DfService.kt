package com.jeepchief.dh.model.rest

import com.jeepchief.dh.model.rest.dto.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DfService {
    @GET("servers")
    suspend fun getServers() : ServerDTO

    //https://api.neople.co.kr/df/servers/all/characters?characterName=고산노블&apikey=07ZVouD2WFbFbtulj2JiKm1CVNL07wUi
    @GET("servers/{serverId}/characters")
    suspend fun getCharacters(
        @Path("serverId") serverId: String,
        @Query("characterName") characterName: String
    ) : CharacterDTO

    @GET("jobs")
    suspend fun getJobs() : JobDTO

    @GET("servers/{server}/characters/{character_id}/status")
    suspend fun getCharacterStatus(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : StatusDTO

    @GET("servers/{server}/characters/{character_id}/equip/equipment")
    suspend fun getEquipment(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : EquipmentDTO

    @GET("servers/{server}/characters/{character_id}/equip/avatar")
    suspend fun getAvatar(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : AvatarDTO

    @GET("servers/{server}/characters/{character_id}/equip/creature")
    suspend fun getCreature(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : CreatureDTO

    @GET("servers/{server}/characters/{character_id}/equip/flag")
    suspend fun getFlag(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : FlagDTO

    @GET("items")
    suspend fun getSearchItems(
        @Query("itemName") itemName: String,
        @Query("wordType") wordType: String,
        @Query("q") q: String,
        @Query("limit") limit: Int = 30
    ) : ItemSearchDTO

    @GET("items")
    fun getSearchItemsTest(
        @Query("itemName") itemName: String,
        @Query("wordType") wordType: String,
        @Query("q") q: String,
        @Query("limit") limit: Int = 30
    ) : Call<ItemSearchDTO>

    @GET("items/{item_id}")
    suspend fun getItemInfo(
        @Path("item_id") itemId: String
    ) : ItemsDTO

    @GET("skills/{jobId}")
    suspend fun getSkills(
        @Path("jobId") jobId: String,
        @Query("jobGrowId") jobGrowId: String
    ) : SkillDTO

    @GET("skills/{jobId}/{skillId}")
    suspend fun getSkillInfo(
        @Path("jobId") jodId: String,
        @Path("skillId") skillId: String
    ) : SkillInfoDTO

    @GET("servers/{serverId}/characters/{characterId}/equip/talisman")
    suspend fun getTalisman(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String
    ) : TalismanDTO

    @GET("servers/{serverId}/characters/{characterId}/skill/buff/equip/equipment")
    suspend fun getBuffEquip(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String
    ) : BuffEquipDTO

    @GET("servers/{serverId}/characters/{characterId}/timeline")
    suspend fun getTimeLine(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String,
        @Query("limit") limit: Int = 50
    ) : TimeLineDTO

    @GET("auction")
    suspend fun getAuction(
        @Query("itemName") itemName: String,
        @Query("wordType") wordType: String = "front"
    ) : AuctionDTO
}