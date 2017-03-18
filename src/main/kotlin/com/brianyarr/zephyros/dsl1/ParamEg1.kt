package com.brianyarr.zephyros.dsl1

fun paramTemplate(): Template {
    return cfn {

        desc("AWS CloudFormation Sample Template DynamoDB_Table: This template demonstrates the creation of a DynamoDB table.  **WARNING** This template creates an Amazon DynamoDB table. You will be billed for the AWS resources used if you create a stack from this template.")

        parameter("HashKeyElementName") {
            Description = "HashType PrimaryKey Name"
            Type = "String"
        }

        parameter("HashKeyElementType") {
            Description = "HashType PrimaryKey Type"
            Type = "String"
        }

    }
}

