package com.sajjad.base.domain.model

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Message")
class Message : ParseObject() {

    var text: String?
        get() = getString(TEXT)
        set(value) {
            if (value == null) {
                remove(TEXT)
            } else {
                put(TEXT, value)
            }
        }

    var to: String?
        get() = getString(TO_USERNAME)
        set(value) {
            if (value == null) {
                remove(TO_USERNAME)
            } else {
                put(TO_USERNAME, value)
            }
        }

    var from: String?
        get() = getString(FROM_USERNAME)
        set(value) {
            if (value == null) {
                remove(FROM_USERNAME)
            } else {
                put(FROM_USERNAME, value)
            }
        }

    companion object {
        const val TEXT = "text"
        const val FROM_USERNAME = "fromUserName"
        const val TO_USERNAME = "toUserName"
    }
}