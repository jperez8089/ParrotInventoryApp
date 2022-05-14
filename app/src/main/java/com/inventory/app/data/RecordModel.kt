package com.inventory.app.data

// this is the model that has the id name and qty and description

data class RecordModel(
    var id: Int,
    var name: String,
    var quantity: Double,
    var description: String
)