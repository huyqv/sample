package wee.digital.sample.ui.model

import wee.digital.library.extension.list

class StoreConversation : ObjectMapper {

    var conversations: List<String>? = null

    override fun toMap(): Map<String, Any?> {
        return mapOf(
            "chatIds" to conversations
        )
    }

    companion object {

        fun fromMap(map: Map<String, Any>): StoreConversation {
            return StoreConversation().also {
                it.conversations = map.list("conversations")
            }
        }
    }
}