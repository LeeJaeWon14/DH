package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreatureDTO(

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
    @SerializedName("creature")
    var creature: Creature?
)

data class Creature(
    @Expose
    @SerializedName("itemId")
    val itemId: String,
    @Expose
    @SerializedName("itemName")
    val itemName: String,
    @Expose
    @SerializedName("itemRarity")
    val itemRarity: String,
    @Expose
    @SerializedName("clone")
    var cloneForCreature: CloneForCreature,
    @Expose
    @SerializedName("artifact")
    var artifact: List<Artifact>
)

data class Artifact(
    @Expose
    @SerializedName("slotColor")
    val slotColor: String,
    @Expose
    @SerializedName("itemName")
    val itemName: String,
    @Expose
    @SerializedName("itemAvailableLevel")
    var itemAvailableLevel: Int,
    @Expose
    @SerializedName("itemRarity")
    val itemRarity: String
)

data class CloneForCreature(
    @Expose
    @SerializedName("itemId")
    var itemId: String?,
    @Expose
    @SerializedName("itemName")
    val itemName: String?
)
