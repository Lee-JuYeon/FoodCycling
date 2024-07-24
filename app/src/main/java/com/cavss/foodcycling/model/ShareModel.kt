package com.cavss.foodcycling.model


data class ShareModel (
    var shareUID : String,
    var sharePlaceName : String,
    var shareItems : List<ShareItemModel>,
    )