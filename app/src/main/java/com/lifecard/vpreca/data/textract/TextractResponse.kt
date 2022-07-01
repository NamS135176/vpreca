package com.lifecard.vpreca.data.textract

import com.google.gson.annotations.SerializedName


data class TextractResponse(
    @SerializedName("result")
    val result: TextractResponseBlocks
)

data class TextractResponseBlocks(
    @SerializedName("Blocks")
    val blocks: List<TextractResponseBlock>
)

data class TextractResponseBlock(
    @SerializedName("BlockType")
    val blockType: String,
    @SerializedName("Text")
    var text: String?,
)
