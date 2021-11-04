package org.example.permissions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.example.permissions.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // enables or disables the send button depending on if text has been entered
        binding.messageText.addTextChangedListener(SendButtonObserver(binding.sendButton))


    }
}