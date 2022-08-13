package com.jeepchief.dh.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SkillDTO(
    @Expose
    @SerializedName("skills")
    val skills: List<Skills>
)

data class Skills(
    @Expose
    @SerializedName("skillId")
    val skillId: String,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("requiredLevel")
    var requiredLevel: Int,
    @Expose
    @SerializedName("type")
    val type: String,
    @Expose
    @SerializedName("costType")
    val costType: String
)