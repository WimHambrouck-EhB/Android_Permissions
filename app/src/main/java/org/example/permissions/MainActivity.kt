package org.example.permissions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.example.permissions.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var requestSMSPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendButton.isEnabled = false

        // register the permissions callback
        requestSMSPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                sendSMS()
            } else {
                // permission denied, check if user needs an explanation

                // shouldShowRequestPermission only works from API23 (Marshmallow) and up
                // older API versions ask for permissions when installing the app, so this case
                // won't come up, because if the user has denied the permission,
                // the app wouldn't be installed in the first place
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                        showSMSPermissionRationale()
                    } else {
                        val snackbar = Snackbar.make(
                            binding.root,
                            getString(R.string.snackbar_enable_permission),
                            BaseTransientBottomBar.LENGTH_INDEFINITE
                        )

                        snackbar
                            .setAction(getString(R.string.app_settings)) {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            }
                            .show()
                    }
                }
            }
        }

        // enables or disables the send button depending on if message text has been entered
        binding.messageText.addTextChangedListener(SendButtonObserver(binding.sendButton))

        binding.sendButton.setOnClickListener { sendSMS() }
    }


    private fun sendSMS() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // permission was granted, send the sms
            val phoneNumber = "5554" // 5554 = default emulator number

            val message = binding.messageText.text.toString() // get content of EditText

            // create and start the intent to send a text message
            val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$phoneNumber"))
            smsIntent.putExtra("sms_body", message)
            startActivity(smsIntent)
        } else {
            requestSMSPermissionLauncher.launch(Manifest.permission.SEND_SMS)
        }
    }

    private fun showSMSPermissionRationale() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.permission_alert_title))
            .setMessage(getString(R.string.permission_alert_message))
            .setNeutralButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }

        builder.show()
    }


}