package com.brianyarr.zephyros.dsl1

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.PropertyNamingStrategy.UPPER_CAMEL_CASE
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.omg.CORBA.Object

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder("AWSTemplateFormatVersion", "Description", "Parameters")
class Template {

    @get:JsonProperty("AWSTemplateFormatVersion")
    val AWSTemplateFormatVersion: String = "2010-09-09"
    @get:JsonProperty("Description")
    var Description: String? = null
    @get:JsonProperty("Parameters")
    val Parameters = linkedMapOf<String, Parameter>()
    @get:JsonProperty("Resources")
    val Resources = linkedMapOf<String, Resource>()

    @get:JsonIgnore
    val DynamoDB = DynamoWrapper()

    fun desc(d: String) {
        this.Description = d
    }

    fun parameter(name: String, init: Parameter.() -> Unit): Parameter {
        val p = Parameter()
        p.init()
        this.Parameters.put(name, p)
        return p
    }

    fun DynamoDB_Table(name: String, init: DynamodbTable.() -> Unit): DynamodbTable {
        val d = DynamodbTable()
        d.init()
        this.Resources.put(name, d)
        return d
    }

    inner class DynamoWrapper {
        fun Table(name: String, init: DynamodbTable.() -> Unit): DynamodbTable {
            val d = DynamodbTable()
            d.init()
            Resources.put(name, d)
            return d
        }
    }
}


@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder("Type", "Properties")
open class Resource(@JsonProperty("Type") val type: String) {
//    val properties = linkedMapOf<String, Any>()
}

class DynamodbTable: Resource("AWS::DynamoDB::Table") {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
//    @JsonPropertyOrder(alphabetic = false)
    class DynamoDBProperties {
        val attributeDefinitions: MutableList<AttributeDefinition> = mutableListOf()
        val keySchema: MutableList<KeyDefinition> = mutableListOf()
        var provisionedThroughput: ProvisionedThroughput? = null
        var tableName: String? = null

    }

    var properties = DynamoDBProperties()

    fun attribute(name: String, type: String) {
        properties.attributeDefinitions.add(AttributeDefinition(name, type))
    }

    fun key(name: String, type: String) {
        properties.keySchema.add(KeyDefinition(name, type))
    }

    fun tableName(name: String) {
        properties.tableName = name
    }

    fun throughput(readCapacityUnits: String, writeCapcityUnits: String) {
        properties.provisionedThroughput = ProvisionedThroughput(readCapacityUnits, writeCapcityUnits)
    }


}

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
class AttributeDefinition(var AttributeName: String, var AttributeType: String)

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
class KeyDefinition(var AttributeName: String, var KeyType: String)

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
class ProvisionedThroughput(var ReadCapacityUnits: String, var WriteCapacityUnits: String)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder("Description", "Type", "Parameters")
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
class Parameter() {

    var Description: String? = null
    var Type: String? = null
    var AllowedPattern: String? = null
    var MinLength: Integer? = null
    var MaxLength: Integer? = null
    var ConstraintDescription: String? = null

}

fun cfn(init: Template.() -> Unit): Template {
    val cfn = Template()
    cfn.init()
    return cfn
}