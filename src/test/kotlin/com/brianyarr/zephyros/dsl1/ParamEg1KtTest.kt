package com.brianyarr.zephyros.dsl1

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Assert.*

class ParamEg1KtTest {
    @org.junit.Test
    fun paramTemplate() {
        val mapper = jacksonObjectMapper()
        val expectedTree = mapper.readTree(javaClass.getResourceAsStream("/ParamEg1.json"))

        val actual = mapper.convertValue(com.brianyarr.zephyros.dsl1.paramTemplate(), JsonNode::class.java)

        assertEquals(expectedTree, actual)
    }

}