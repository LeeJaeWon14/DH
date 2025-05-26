package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BuffEquipDTO(

    @Expose
    @SerializedName("characterId")
    val characterId: String,
    @Expose
    @SerializedName("characterName")
    val characterName: String,
    @Expose
    @SerializedName("level")
    var level: Int,
    @Expose
    @SerializedName("jobId")
    val jobId: String,
    @Expose
    @SerializedName("jobGrowId")
    val jobGrowId: String,
    @Expose
    @SerializedName("jobName")
    val jobName: String,
    @Expose
    @SerializedName("jobGrowName")
    val jobGrowName: String,
    @Expose
    @SerializedName("adventureName")
    val adventureName: String,
    @Expose
    @SerializedName("guildId")
    val guildId: String,
    @Expose
    @SerializedName("guildName")
    val guildName: String,
    @Expose
    @SerializedName("skill")
    var skill: Skill
)

data class Skill(
    @Expose
    @SerializedName("buff")
    var buff: BuffSkill
)

data class BuffSkill(
    @Expose
    @SerializedName("skillInfo")
    var skillInfo: SkillInfo,
    @Expose
    @SerializedName("equipment")
    var equipment: List<BuffEquipment>
)

data class BuffEquipment(
    @Expose
    @SerializedName("slotId")
    val slotId: String,
    @Expose
    @SerializedName("slotName")
    val slotName: String,
    @Expose
    @SerializedName("itemId")
    val itemId: String,
    @Expose
    @SerializedName("itemName")
    val itemName: String,
    @Expose
    @SerializedName("itemType")
    val itemType: String,
    @Expose
    @SerializedName("itemTypeDetail")
    val itemTypeDetail: String,
    @Expose
    @SerializedName("itemAvailableLevel")
    var itemAvailableLevel: Int,
    @Expose
    @SerializedName("itemRarity")
    val itemRarity: String,
    @Expose
    @SerializedName("setItemId")
    val setItemId: String,
    @Expose
    @SerializedName("setItemName")
    val setItemName: String,
    @Expose
    @SerializedName("reinforce")
    var reinforce: Int,
    @Expose
    @SerializedName("amplificationName")
    val amplificationName: String,
    @Expose
    @SerializedName("refine")
    var refine: Int
)

data class SkillInfo(
    @Expose
    @SerializedName("skillId")
    val skillId: String,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("option")
    var option: Option
)

data class Option(
    @Expose
    @SerializedName("level")
    var level: Int,
    @Expose
    @SerializedName("desc")
    val desc: String,
    @Expose
    @SerializedName("values")
    var values: List<String>
)
