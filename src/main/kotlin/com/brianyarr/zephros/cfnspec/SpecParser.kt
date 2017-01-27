package com.brianyarr.zephros.cfnspec

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

data class Property(val Documentation: String, val PrimitiveType: String?, val Type: String?, val Required: Boolean, val DuplicatesAllowed: Boolean?, val ItemType: String?, val PrimitiveItemType: String?, val UpdateType: String) {
    fun getKotlinType(): String {
        if (Type == null && PrimitiveType == null) {
            throw IllegalStateException()
        }
        if (Type != null && PrimitiveType != null) {
            throw IllegalStateException()
        }

        if (ItemType != null && PrimitiveItemType != null) {
            throw IllegalStateException()
        }

        var type = Type ?: "" + PrimitiveType?: ""
        if (ItemType != null || PrimitiveItemType != null) {
            type = type + "<" + (ItemType ?: "") + (PrimitiveItemType ?: "") + ">"
        }
        if (!Required) {
            type += "?"
        }
        return type
    }
}

data class PropertyType(val Documentation: String, val Properties: Map<String, Property>)

//not sure attributes has to be a map of maps, did try Map of objects, didnt work TODO fix
data class ResourceType(val Documentation: String, val Attributes: Map<String, Map<String, String>>?, val Properties: Map<String, Property>, val AdditionalProperties: Boolean = false)

data class Spec(val PropertyTypes: Map<String, PropertyType>, val ResourceTypes: Map<String, ResourceType>, val ResourceSpecificationVersion: String)


object SpecParser {

    fun parseSpec(): Spec {
        val mapper = jacksonObjectMapper()
        val s = mapper.readValue<Spec>(File("src/main/resources/specification.json"))
        return s
    }

}


fun main(args: Array<String>) {
    val mapper = jacksonObjectMapper()
    val s = mapper.readValue<Spec>(File("src/main/resources/specification.json"))

    val resourceType = s.ResourceTypes["AWS::S3::Bucket"]!!

    println(resourceType.Attributes)

}