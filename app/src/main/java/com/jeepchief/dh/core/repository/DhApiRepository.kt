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
import com.jeepchief.dh.core.network.dto.ServerDTO
import com.jeepchief.dh.core.network.dto.SkillDTO
import com.jeepchief.dh.core.network.dto.SkillInfoDTO
import com.jeepchief.dh.core.network.dto.StatusDTO
import com.jeepchief.dh.core.network.dto.TalismanDTO
import com.jeepchief.dh.core.network.dto.TimeLineDTO
import com.jeepchief.dh.core.util.Log
import javax.inject.Inject

class DhApiRepository @Inject constructor(
    private val dfService: DfService
){
    suspend fun getServers(): ServerDTO {
        val result = dfService.getServers()

        return if(result.isSuccessful) {
            Log.d("getServers API success")
            result.body() ?: ServerDTO(listOf())
        } else {
            Log.d("getServers API failure")
            throw IllegalStateException()
        }
    }

    suspend fun getCharacters(serverId: String, characterName: String): CharacterDTO {
        val result = dfService.getCharacters(serverId, characterName)

        return if(result.isSuccessful) {
            Log.d("getCharacters API success")
            result.body() ?: CharacterDTO(listOf())
        } else {
            Log.d("getCharacters API failure")
            throw IllegalStateException()
        }
    }

    suspend fun getJobs(): JobDTO {
        val result = dfService.getJobs()

        return if(result.isSuccessful) {
            Log.d("getJobs API success")
            result.body() ?: JobDTO(listOf())
        } else {
            Log.d("getJobs API failure")
            throw IllegalStateException("")
        }
    }

    suspend fun getCharacterStatus(server: String, id: String): StatusDTO {
        val result = dfService.getCharacterStatus(server, id)

        return if(result.isSuccessful) {
            Log.d("getCharacterStatus API success")
            result.body() ?: StatusDTO()
        } else {
            Log.d("getCharacterStatus API failure")
            throw IllegalStateException()
        }
    }

    suspend fun getEquipment(server: String, id: String): EquipmentDTO {
        val result = dfService.getEquipment(server, id)

        return if(result.isSuccessful) {
            Log.d("getEquipment API success")
            result.body() ?: EquipmentDTO()
        } else {
            Log.d("getEquipment API failure")
            throw IllegalStateException()
        }
    }

    suspend fun getAvatar(server: String, id: String): AvatarDTO {
        val result = dfService.getAvatar(server, id)

        return if(result.isSuccessful) {
            Log.d("getAvatar API success")
            result.body() ?: AvatarDTO()
        } else {
            Log.d("getAvatar API failure")
            throw IllegalStateException()
        }
    }

    suspend fun getCreature(server: String, id: String): CreatureDTO {
        val result = dfService.getCreature(server, id)

        return if(result.isSuccessful) {
            Log.d("getCreature API success")
            result.body() ?: CreatureDTO()
        } else {
            Log.d("getCreature API failure")
            throw IllegalStateException()
        }
    }

    suspend fun getFlag(server: String, id: String): FlagDTO {
        val result = dfService.getFlag(server, id)

        return if(result.isSuccessful) {
            Log.d("getFlag API success")
            result.body() ?: FlagDTO()
        } else {
            Log.d("getFlag API failure")
            throw IllegalStateException()
        }
    }
    
    suspend fun getSearchItems(itemName: String, wordType: String, q: String): ItemSearchDTO {
        val result = dfService.getSearchItems(itemName, wordType, q)
        
        return if(result.isSuccessful) {
            Log.d("getSearchItems API success")
            result.body() ?: ItemSearchDTO(listOf())
        } else {
            Log.d("getSearchItems API failure")
            throw IllegalStateException()
        }
    }
    
    suspend fun getItemInfo(itemId: String): ItemsDTO {
        val result = dfService.getItemInfo(itemId)
        
        return if(result.isSuccessful) {
            Log.d("getItemInfo API success")
            result.body() ?: ItemsDTO()
        } else {
            Log.d("getItemInfo API failure")
            throw IllegalStateException()
        }
    }
    
    suspend fun getSkills(jobId: String, jobGrowId: String): SkillDTO {
        val result = dfService.getSkills(jobId, jobGrowId)
        
        return if(result.isSuccessful) {
            Log.d("getSkills API success")
            result.body() ?: SkillDTO(listOf())
        } else {
            Log.d("getSkills API failure")
            throw IllegalStateException()
        }
    }
    
    suspend fun getSkillInfo(jobId: String, skillId: String): SkillInfoDTO {
        val result = dfService.getSkillInfo(jobId, skillId)
        
        return if(result.isSuccessful) {
            Log.d("getSkillInfo API success")
            result.body() ?: TODO()
        } else {
            Log.d("getSkillInfo API failure")
            throw IllegalStateException()
        }
    }

    suspend fun getTalisman(serverId: String, characterId: String): TalismanDTO {
        val result = dfService.getTalisman(serverId, characterId)

        return if(result.isSuccessful) {
            Log.d("getTalisman API success")
            result.body() ?: TalismanDTO()
        } else {
            Log.d("getTalisman API failure")
            throw IllegalStateException()
        }
    }

    suspend fun getBuffEquip(serverId: String, characterId: String): BuffEquipDTO {
        val result = dfService.getBuffEquip(serverId, characterId)

        return if(result.isSuccessful) {
            Log.d("getBuffEquip API success")
            result.body() ?: BuffEquipDTO()
        } else {
            Log.d("getBuffEquip API failure")
            throw IllegalStateException()
        }
    }

    suspend fun getTimeLine(serverId: String, characterId: String): TimeLineDTO {
        val result = dfService.getTimeLine(serverId, characterId)

        return if(result.isSuccessful) {
            Log.d("getTimeLine API success")
            result.body() ?: TimeLineDTO()
        } else {
            Log.d("getTimeLine API failure")
            throw IllegalStateException()
        }
    }

    suspend fun getAuction(sort: String = "unitPrice:desc", itemName: String, q: String): AuctionDTO {
        val result = dfService.getAuction(
            sort = sort,
            itemName = itemName,
            q = q
        )

        return if(result.isSuccessful) {
            Log.d("getAuction API success")
            result.body() ?: AuctionDTO()
        } else {
            Log.d("getAuction API failure")
            throw IllegalStateException()
        }
    }

    suspend fun getFame(fame: Int, jobId: String, jobGrowId: String): FameDTO {
        val result = dfService.getFame(maxFame = fame.takeIf { it != 0 }, jobId = jobId.ifEmpty { null }, jobGrowId = jobGrowId.ifEmpty { null })
        Log.d("""
            fame: $fame,
            jobId: $jobId,
            jobGrowId: $jobGrowId
        """.trimIndent())

        return if(result.isSuccessful) {
            Log.d("getFame API success")
            result.body() ?: FameDTO()
        } else {
            Log.d("getFame API failure")
            throw IllegalStateException()
        }
    }

    suspend fun getCharacterDefault(serverId: String, characterId: String): CharacterRows {
        val result = dfService.getCharacterDefault(serverId, characterId)
        Log.d("""
            getCharacterDefault()
            severId : $serverId
            characterId: $characterId
        """.trimIndent())

        return if(result.isSuccessful) {
            Log.d("getCharacterDefault API success")
            result.body() ?: CharacterRows()
        } else {
            Log.d("getCharacterDefault API failure")
            throw IllegalStateException()
        }
    }
}