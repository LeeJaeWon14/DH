package com.jeepchief.dh.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SkillInfoDTO(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("type")
    val type: String,
    @Expose
    @SerializedName("costType")
    val costType: String,
    @Expose
    @SerializedName("desc")
    val desc: String,
    @Expose
    @SerializedName("descDetail")
    val descDetail: String,
    @Expose
    @SerializedName("consumeItem")
    val consumeItem: ConsumeItem?,
    @Expose
    @SerializedName("descSpecial")
    val descSpecial: String,
    @Expose
    @SerializedName("maxLevel")
    var maxLevel: Int,
    @Expose
    @SerializedName("requiredLevel")
    var requiredLevel: Int,
    @Expose
    @SerializedName("requiredLevelRange")
    var requiredLevelRange: Int,
    @Expose
    @SerializedName("preRequiredSkill")
    val preRequiredSkill: String,
    @Expose
    @SerializedName("jobId")
    val jobId: String,
    @Expose
    @SerializedName("jobName")
    val jobName: String,
    @Expose
    @SerializedName("jobGrowLevel")
    var jobGrowLevel: List<JobGrowLevel>
//    @Expose
//    @SerializedName("levelInfo")
//    var levelInfo: LevelInfo
)

data class LevelInfo(
    @Expose
    @SerializedName("optionDesc")
    val optionDesc: String,
    @Expose
    @SerializedName("rows")
    var rows: List<SkillRows>
)

data class SkillRows(
    @Expose
    @SerializedName("level")
    var level: Int,
    @Expose
    @SerializedName("consumeMp")
    val consumeMp: Int,
    @Expose
    @SerializedName("coolTime")
    var coolTime: Double,
    @Expose
    @SerializedName("castingTime")
    val castingTime: Double,
    @Expose
    @SerializedName("optionValue")
    var optionValue: OptionValue
)

data class OptionValue(
    @Expose
    @SerializedName("value1")
    var value1: Int,
    @Expose
    @SerializedName("value2")
    val value2: Double,
    @Expose
    @SerializedName("value3")
    var value3: Int
)

data class JobGrowLevel(
    @Expose
    @SerializedName("jobGrowId")
    val jobGrowId: String,
    @Expose
    @SerializedName("jobGrowName")
    val jobGrowName: String,
    @Expose
    @SerializedName("masterLevel")
    var masterLevel: Int
)

data class ConsumeItem(
    @Expose
    @SerializedName("itemId")
    val itemId: String,

    @Expose
    @SerializedName("itemName")
    val itemName: String,

    @Expose
    @SerializedName("value")
    val value: Int
)
