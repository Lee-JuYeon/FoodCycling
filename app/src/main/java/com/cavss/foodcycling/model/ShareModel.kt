package com.cavss.foodcycling.model

import com.cavss.foodcycling.ShareItem

data class ShareModel (
    var shareUID : String,
    var sharePlaceName : String,
    var shareItems : List<ShareItem>,
    )