package com.cavss.foodcycling.model

import com.cavss.foodcycling.type.ItemType

data class ShareItemModel(
    var itemUID : String,
    var itemName : String,
    var itemImage : String,
    var itemGram : Int,
    var itemType : ItemType,
    var shareUID : String
){

}