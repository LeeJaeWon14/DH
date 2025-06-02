package com.jeepchief.dh.core.network

import com.jeepchief.dh.core.network.dto.AuctionDTO
import com.jeepchief.dh.core.network.dto.AvatarDTO
import com.jeepchief.dh.core.network.dto.BuffEquipDTO
import com.jeepchief.dh.core.network.dto.CharacterDTO
import com.jeepchief.dh.core.network.dto.CreatureDTO
import com.jeepchief.dh.core.network.dto.EquipmentDTO
import com.jeepchief.dh.core.network.dto.FameDTO
import com.jeepchief.dh.core.network.dto.FlagDTO
import com.jeepchief.dh.core.network.dto.ItemSearchDTO
import com.jeepchief.dh.core.network.dto.ItemsDTO
import com.jeepchief.dh.core.network.dto.JobDTO
import com.jeepchief.dh.core.network.dto.ServerDTO
import com.jeepchief.dh.core.network.dto.SkillDTO
import com.jeepchief.dh.core.network.dto.SkillInfoDTO
import com.jeepchief.dh.core.network.dto.StatusDTO
import com.jeepchief.dh.core.network.dto.TalismanDTO
import com.jeepchief.dh.core.network.dto.TimeLineDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DfService {
    @GET("servers")
    suspend fun getServers() : Response<ServerDTO>

    //https://api.neople.co.kr/df/servers/all/characters?characterName=고산노블&apikey=07ZVouD2WFbFbtulj2JiKm1CVNL07wUi
    @GET("servers/{serverId}/characters")
    suspend fun getCharacters(
        @Path("serverId") serverId: String,
        @Query("characterName") characterName: String
    ) : Response<CharacterDTO>

    @GET("jobs")
    suspend fun getJobs() : Response<JobDTO>

    @GET("servers/{server}/characters/{character_id}/status")
    suspend fun getCharacterStatus(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : Response<StatusDTO>

    @GET("servers/{server}/characters/{character_id}/equip/equipment")
    suspend fun getEquipment(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : Response<EquipmentDTO>

    @GET("servers/{server}/characters/{character_id}/equip/avatar")
    suspend fun getAvatar(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : Response<AvatarDTO>

    @GET("servers/{server}/characters/{character_id}/equip/creature")
    suspend fun getCreature(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : Response<CreatureDTO>

    @GET("servers/{server}/characters/{character_id}/equip/flag")
    suspend fun getFlag(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : Response<FlagDTO>

    @GET("items")
    suspend fun getSearchItems(
        @Query("itemName") itemName: String,
        @Query("wordType") wordType: String,
        @Query("q") q: String,
        @Query("limit") limit: Int = 30
    ) : Response<ItemSearchDTO>

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
    ) : Response<ItemsDTO>

    @GET("skills/{jobId}")
    suspend fun getSkills(
        @Path("jobId") jobId: String,
        @Query("jobGrowId") jobGrowId: String
    ) : Response<SkillDTO>

    @GET("skills/{jobId}/{skillId}")
    suspend fun getSkillInfo(
        @Path("jobId") jodId: String,
        @Path("skillId") skillId: String
    ) : Response<SkillInfoDTO>

    @GET("servers/{serverId}/characters/{characterId}/equip/talisman")
    suspend fun getTalisman(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String
    ) : Response<TalismanDTO>

    @GET("servers/{serverId}/characters/{characterId}/skill/buff/equip/equipment")
    suspend fun getBuffEquip(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String
    ) : Response<BuffEquipDTO>

    @GET("servers/{serverId}/characters/{characterId}/timeline")
    suspend fun getTimeLine(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String,
        @Query("limit") limit: Int = 50
    ) : Response<TimeLineDTO>

    @GET("auction")
    suspend fun getAuction(
        @Query("itemName") itemName: String,
        @Query("wordType") wordType: String
    ) : Response<AuctionDTO>

    // https://api.neople.co.kr/df/servers/<serverId>/characters-fame?minFame=<minFame>&maxFame=<maxFame>&jobId=<jobId>&jobGrowId=<jobGrowId>&isAllJobGrow=<isAllJobGrow>&isBuff=<isBuff>&limit=<limit>&apikey=<apikey>
    @GET("servers/{serverId}/characters-fame")
    suspend fun getFame(
        @Path("serverId") serverId: String = "all",
        @Query("minFame") minFame: Int? = null,
        @Query("maxFame") maxFame: Int? = null,
        @Query("jobId") jobId: String? = null,
        @Query("jobGrowId") jobGrowId: String? = null,
        @Query("isAllJobGrow") isAllJobGrow: Boolean = false,
        @Query("isBuff") isBuff: Boolean = false,
        @Query("limit") limit: Int = 20
    ) : Response<FameDTO>
}