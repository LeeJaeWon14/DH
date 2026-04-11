package com.jeepchief.dh.core.network

import com.jeepchief.dh.core.network.dto.AuctionDTO
import com.jeepchief.dh.core.network.dto.AvatarDTO
import com.jeepchief.dh.core.network.dto.BuffEquipDTO
import com.jeepchief.dh.core.network.dto.CharacterDTO
import com.jeepchief.dh.core.network.dto.CharacterRows
import com.jeepchief.dh.core.network.dto.CreatureDTO
import com.jeepchief.dh.core.network.dto.EquipmentDTO
import com.jeepchief.dh.core.network.dto.FameDTO
import com.jeepchief.dh.core.network.dto.FlagDTO
import com.jeepchief.dh.core.network.dto.ItemSearchDTO
import com.jeepchief.dh.core.network.dto.ItemsDTO
import com.jeepchief.dh.core.network.dto.JobDTO
import com.jeepchief.dh.core.network.dto.MistAssimilationDTO
import com.jeepchief.dh.core.network.dto.ServerDTO
import com.jeepchief.dh.core.network.dto.SkillDTO
import com.jeepchief.dh.core.network.dto.SkillInfoDTO
import com.jeepchief.dh.core.network.dto.StatusDTO
import com.jeepchief.dh.core.network.dto.TalismanDTO
import com.jeepchief.dh.core.network.dto.TimeLineDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DfService {
    @GET("servers")
    fun getServers() : Call<ServerDTO>

    //https://api.neople.co.kr/df/servers/all/characters?characterName=고산노블&apikey=07ZVouD2WFbFbtulj2JiKm1CVNL07wUi
    @GET("servers/{serverId}/characters")
    fun getCharacters(
        @Path("serverId") serverId: String,
        @Query("characterName") characterName: String,
        @Query("limit") limit: Int = 200
    ) : Call<CharacterDTO>

    @GET("jobs")
    fun getJobs() : Call<JobDTO>

    @GET("servers/{server}/characters/{character_id}/status")
    fun getCharacterStatus(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : Call<StatusDTO>

    @GET("servers/{server}/characters/{character_id}/equip/equipment")
    fun getEquipment(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : Call<EquipmentDTO>

    @GET("servers/{server}/characters/{character_id}/equip/avatar")
    fun getAvatar(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : Call<AvatarDTO>

    @GET("servers/{server}/characters/{character_id}/equip/creature")
    fun getCreature(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : Call<CreatureDTO>

    @GET("servers/{server}/characters/{character_id}/equip/flag")
    fun getFlag(
        @Path("server") server: String,
        @Path("character_id") characterId: String
    ) : Call<FlagDTO>

    @GET("items")
    fun getSearchItems(
        @Query("itemName") itemName: String,
        @Query("wordType") wordType: String,
        @Query("q") q: String,
        @Query("limit") limit: Int = 30
    ) : Call<ItemSearchDTO>

    @GET("items")
    fun getSearchItemsTest(
        @Query("itemName") itemName: String,
        @Query("wordType") wordType: String,
        @Query("q") q: String,
        @Query("limit") limit: Int = 30
    ) : Call<ItemSearchDTO>

    @GET("items/{item_id}")
    fun getItemInfo(
        @Path("item_id") itemId: String
    ) : Call<ItemsDTO>

    @GET("skills/{jobId}")
    fun getSkills(
        @Path("jobId") jobId: String,
        @Query("jobGrowId") jobGrowId: String
    ) : Call<SkillDTO>

    @GET("skills/{jobId}/{skillId}")
    fun getSkillInfo(
        @Path("jobId") jodId: String,
        @Path("skillId") skillId: String
    ) : Call<SkillInfoDTO>

    @GET("servers/{serverId}/characters/{characterId}/equip/talisman")
    fun getTalisman(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String
    ) : Call<TalismanDTO>

    @GET("servers/{serverId}/characters/{characterId}/skill/buff/equip/equipment")
    fun getBuffEquip(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String
    ) : Call<BuffEquipDTO>

    @GET("servers/{serverId}/characters/{characterId}/skill/buff/equip/avatar")
    fun getBuffAvatar(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String
    ) : Call<BuffEquipDTO>

    @GET("servers/{serverId}/characters/{characterId}/skill/buff/equip/creature")
    fun getBuffCreature(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String
    ) : Call<BuffEquipDTO>

    @GET("servers/{serverId}/characters/{characterId}/timeline")
    fun getTimeLine(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String,
        @Query("next") next: String?,
        @Query("limit") limit: Int = 50
    ) : Call<TimeLineDTO>

    @GET("auction")
    fun getAuction(
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String,
        @Query("itemName") itemName: String,
        @Query("wordType") wordType: String = "front",
        @Query("q") q: String
    ) : Call<AuctionDTO>

    // https://api.neople.co.kr/df/servers/<serverId>/characters-fame?minFame=<minFame>&maxFame=<maxFame>&jobId=<jobId>&jobGrowId=<jobGrowId>&isAllJobGrow=<isAllJobGrow>&isBuff=<isBuff>&limit=<limit>&apikey=<apikey>
    @GET("servers/{serverId}/characters-fame")
    fun getFame(
        @Path("serverId") serverId: String = "all",
        @Query("minFame") minFame: Int? = null,
        @Query("maxFame") maxFame: Int? = null,
        @Query("jobId") jobId: String? = null,
        @Query("jobGrowId") jobGrowId: String? = null,
        @Query("isAllJobGrow") isAllJobGrow: Boolean = true,
//        @Query("isBuff") isBuff: Boolean = false,
        @Query("limit") limit: Int = 20
    ) : Call<FameDTO>

    @GET("servers/{serverId}/characters/{characterId}")
    fun getCharacterDefault(
        @Path("serverId") serverId: String,
        @Path("characterId") characterId: String
    ) : Call<CharacterRows>

    @GET("servers/{serverId}/characters/{characterId}/equip/mist-assimilation")
    fun getMistAssimilation(
        @Path("serverId") server: String,
        @Path("characterId") characterId: String
    ) : Call<MistAssimilationDTO>
}