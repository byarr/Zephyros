package com.brianyarr.zephyros.dsl1

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert

class DynamoDbEg1Test {
    @org.junit.Test
    fun paramTemplate() {
        val mapper = jacksonObjectMapper()
        val expectedTree = mapper.readTree(javaClass.getResourceAsStream("/DynamoDbEg1.json"))

        val actual = mapper.convertValue(template(), JsonNode::class.java)

        Assert.assertEquals(expectedTree, actual)
    }

    fun template(): Template {
        return cfn {
            DynamoDB.Table("myDynamoDBTable") {
                attribute("Album", "S")
                attribute("Artist", "S")
                attribute("Sales", "N")
                attribute("NumberOfSongs", "N")
                key("Album", "HASH")
                key("Artist", "RANGE")
                throughput(readCapacityUnits = "5", writeCapcityUnits = "5")
                tableName("myTableName")

            }
        }
    }


}
