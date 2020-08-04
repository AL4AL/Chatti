package com.sajjad.chat.conversation.domain.model

import com.parse.ParseObject
import com.sajjad.base.domain.model.Message

class Conversation : ParseObject() {

    var username: String?
        get() = getString(USERNAME)
        set(value) {
            if (value === null) {
                remove(USERNAME)
            } else {
                put(USERNAME, value)
            }
        }

    var lastMessage: Message? = null

    companion object {
        const val USERNAME = "userName"
    }
}