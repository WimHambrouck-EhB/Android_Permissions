package org.example.permissions

import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView

class SendButtonObserver(private val sendButton: ImageView) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // not needed
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // when button text changes, check if the text isn't empty
        // if not empty: enable the button and display purple send icon
        // if empty: disable the button and display gray send icon
        if (s.toString().trim().isNotEmpty()) {
            sendButton.isEnabled = true
            sendButton.setImageResource(R.drawable.ic_outline_send_24)
        } else {
            sendButton.isEnabled = false
            sendButton.setImageResource(R.drawable.ic_outline_send_gray_24)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        // not needed
    }
}