package com.jeepchief.dh.core.repository

import com.jeepchief.dh.core.network.DfService
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
import com.jeepchief.dh.core.util.Log
import retrofit2.await
import javax.inject.Inject

class DhApiRepository @Inject constructor(
    private val dfService: DfService
){
    suspend fun getServers(): ServerDTO =
        dfService.getServers().await()

    suspend fun getCharacters(serverId: String, characterName: String): CharacterDTO =
        dfService.getCharacters(serverId, characterName).await()

    suspend fun getJobs(): JobDTO =
        dfService.getJobs().await()

    suspend fun getCharacterStatus(server: String, id: String): StatusDTO =
        dfService.getCharacterStatus(server, id).await()

    suspend fun getEquipment(server: String, id: String): EquipmentDTO =
        dfService.getEquipment(server, id).await()

    suspend fun getAvatar(server: String, id: String): AvatarDTO =
        dfService.getAvatar(server, id).await()

    suspend fun getCreature(server: String, id: String): CreatureDTO =
        dfService.getCreature(server, id).await()

    suspend fun getFlag(server: String, id: String): FlagDTO =
        dfService.getFlag(server, id).await()
    
    suspend fun getSearchItems(itemName: String, wordType: String, q: String): ItemSearchDTO =
        dfService.getSearchItems(itemName, wordType, q).await()
    
    suspend fun getItemInfo(itemId: String): ItemsDTO =
        dfService.getItemInfo(itemId).await()

    suspend fun getSkills(jobId: String, jobGrowId: String): SkillDTO =
        dfService.getSkills(jobId, jobGrowId).await()

    suspend fun getSkillInfo(jobId: String, skillId: String): SkillInfoDTO =
        dfService.getSkillInfo(jobId, skillId).await()

    suspend fun getTalisman(serverId: String, characterId: String): TalismanDTO =
        dfService.getTalisman(serverId, characterId).await()

    suspend fun getBuffEquip(serverId: String, characterId: String): BuffEquipDTO =
        dfService.getBuffEquip(serverId, characterId).await()

    suspend fun getBuffAvatar(serverId: String, characterId: String): BuffEquipDTO =
        dfService.getBuffAvatar(serverId, characterId).await()

    suspend fun getBuffCreature(serverId: String, characterId: String): BuffEquipDTO =
        dfService.getBuffCreature(serverId, characterId).await()

    suspend fun getTimeLine(serverId: String, characterId: String, next: String?): TimeLineDTO =
        dfService.getTimeLine(serverId, characterId, next).await()

    suspend fun getAuction(sort: String = "unitPrice:desc", itemName: String, q: String): AuctionDTO =
        dfService.getAuction(
            sort = sort,
            itemName = itemName,
            q = q
        ).await()

    suspend fun getFame(fame: Int, jobId: String, jobGrowId: String): FameDTO {
        Log.d("""
            fame: $fame,
            jobId: $jobId,
            jobGrowId: $jobGrowId
        """.trimIndent())

        return dfService.getFame(
            maxFame = fame.takeIf { it != 0 },
            jobId = jobId.ifEmpty { null },
            jobGrowId = jobGrowId.ifEmpty { null }
        ).await()
    }

    suspend fun getCharacterDefault(serverId: String, characterId: String): CharacterRows =
        dfService.getCharacterDefault(serverId, characterId).await()

    suspend fun getMistAssimilation(serverId: String, characterId: String): MistAssimilationDTO =
        dfService.getMistAssimilation(serverId, characterId).await()
}