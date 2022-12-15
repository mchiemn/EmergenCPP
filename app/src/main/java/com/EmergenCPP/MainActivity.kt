package com.EmergenCPP

import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity() {
    private lateinit var userInputText: EditText
    private lateinit var translateButton: Button
    private lateinit var flashButton: Button
    private lateinit var btnToggleDark: Button
    private lateinit var alertButton: Button
    private lateinit var morseCode: TextView
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init() //Initialize buttons and text
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun init() {
        userInputText = findViewById(R.id.UserInputText)
        translateButton = findViewById(R.id.TranslateButton)
        flashButton = findViewById(R.id.FlashButton)
        morseCode = findViewById(R.id.MorseCode)
        btnToggleDark = findViewById(R.id.btnToggleDark)
        alertButton = findViewById(R.id.AlertButton)

        //Saving State of app using SHaredPreferences
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false)
        var isAlertModeOn = false

        //When user reopens app after mode
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            btnToggleDark.setText(getString(R.string.disable_dark))
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            btnToggleDark.setText(getString(R.string.enable_dark))
        }

        //When user taps the dark mode button
        btnToggleDark.setOnClickListener(View.OnClickListener {
            if (isDarkModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isDarkModeOn", false)
                editor.apply()
                btnToggleDark.setText(getString(R.string.enable_dark))
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isDarkModeOn", true)
                editor.apply()
                btnToggleDark.setText(getString(R.string.disable_dark))
            }
        })


        //Translate to Morse code on click
        translateButton.setOnClickListener(View.OnClickListener {
            morseCode.setText("") //Clear TextView for another translation
            translate()
            flashButton.setVisibility(View.VISIBLE)
            Toast.makeText(this@MainActivity, getString(R.string.translated_toast), Toast.LENGTH_SHORT).show()
        })

        //Use flashlight to flash Morse code on click
        flashButton.setOnClickListener(View.OnClickListener {
            try {
                flashMorse()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        })

        alertButton.setOnClickListener(View.OnClickListener {
            if (isAlertModeOn) {
                alertMode(false)
                isAlertModeOn = false
                alertButton.setText(getString(R.string.enable_alert))
            } else {
                alertMode(true)
                isAlertModeOn = true
                alertButton.setText(getString(R.string.disable_alert))
            }
        })
    }

    //Method to translate text to morse
    fun translate() {
        val userString = userInputText.text.toString()
        //Loop through message and translate
        for (n in 0 until userString.length) {
            when (userString[n]) {
                ' ' -> morseCode.append("/ ")
                'a', 'A' -> morseCode.append(".- ")
                'b', 'B' -> morseCode.append("-... ")
                'c', 'C' -> morseCode.append("-.-. ")
                'd', 'D' -> morseCode.append("-.. ")
                'e', 'E' -> morseCode.append(". ")
                'f', 'F' -> morseCode.append("..-. ")
                'g', 'G' -> morseCode.append("--. ")
                'h', 'H' -> morseCode.append(".... ")
                'i', 'I' -> morseCode.append(".. ")
                'j', 'J' -> morseCode.append(".--- ")
                'k', 'K' -> morseCode.append("-.- ")
                'l', 'L' -> morseCode.append(".-.. ")
                'm', 'M' -> morseCode.append("-- ")
                'n', 'N' -> morseCode.append("-. ")
                'o', 'O' -> morseCode.append("--- ")
                'p', 'P' -> morseCode.append(".--. ")
                'q', 'Q' -> morseCode.append("--.- ")
                'r', 'R' -> morseCode.append(".-. ")
                's', 'S' -> morseCode.append("... ")
                't', 'T' -> morseCode.append("- ")
                'u', 'U' -> morseCode.append("..- ")
                'v', 'V' -> morseCode.append("...- ")
                'w', 'W' -> morseCode.append(".-- ")
                'x', 'X' -> morseCode.append("-..- ")
                'y', 'Y' -> morseCode.append("-.-- ")
                'z', 'Z' -> morseCode.append("--.. ")
                '1' -> morseCode.append(".---- ")
                '2' -> morseCode.append("..--- ")
                '3' -> morseCode.append("...-- ")
                '4' -> morseCode.append("....- ")
                '5' -> morseCode.append("..... ")
                '6' -> morseCode.append("-.... ")
                '7' -> morseCode.append("--... ")
                '8' -> morseCode.append("---.. ")
                '9' -> morseCode.append("----. ")
                '0' -> morseCode.append("----- ")
                '.' -> morseCode.append(".-.-.- ")
                ',' -> morseCode.append("--..-- ")
                '?' -> morseCode.append("..--.. ")
                '!' -> morseCode.append("-.-.-- ")
                '"' -> morseCode.append(".-..-. ")
                '\'' -> morseCode.append(".----. ")
                '(' -> morseCode.append("-.--. ")
                ')' -> morseCode.append("-.--.- ")
                '&' -> morseCode.append(".-... ")
                ':' -> morseCode.append("---... ")
                ';' -> morseCode.append("-.-.-. ")
                '/' -> morseCode.append("-..-. ")
                '_' -> morseCode.append("..--.- ")
                '=' -> morseCode.append("-...- ")
                '+' -> morseCode.append(".-.-. ")
                '-' -> morseCode.append("-....- ")
                '$' -> morseCode.append("...-..- ")
                '@' -> morseCode.append(".--.-. ")
            }
        }
    }


    //Method to flash Morse code
    @RequiresApi(Build.VERSION_CODES.M)
    @Throws(InterruptedException::class)
    fun flashMorse() {
        if (morseCode.text.toString().isEmpty()) {
            Toast.makeText(this@MainActivity, getString(R.string.no_text_toast), Toast.LENGTH_SHORT)
                .show()
        } else {
            val morse = morseCode.text.toString()
            for (n in 0 until morse.length) {
                val m = morse[n].toString()
                when (m) {
                    "." -> {
                        flashOn()
                        Thread.sleep(250)
                        flashOff()
                        Thread.sleep(250)
                    }
                    "-" -> {
                        flashOn()
                        Thread.sleep(750)
                        flashOff()
                        Thread.sleep(250)
                    }
                    "/" -> Thread.sleep(500)
                    else -> Thread.sleep(250)
                }
            }
        }
    }

    //Method to flash Morse code
    @RequiresApi(Build.VERSION_CODES.M)
    @Throws(InterruptedException::class)
    fun alertMode(status: Boolean) {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if(status){
            val pattern = longArrayOf(0, 1000, 1000)
            vibrator.vibrate(pattern, 0)
            flashOn()
        }
        else{
            vibrator.cancel()
            flashOff()
        }
    }

    //Method to turn get access and turn on flash
    @RequiresApi(Build.VERSION_CODES.M)
    fun flashOn() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            try {
                val camera = getSystemService(CAMERA_SERVICE) as CameraManager
                camera?.setTorchMode("0", true)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }

    //Method to turn get access and turn off flash
    @RequiresApi(Build.VERSION_CODES.M)
    fun flashOff() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            try {
                val camera = getSystemService(CAMERA_SERVICE) as CameraManager
                camera?.setTorchMode("0", false)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val GET_FROM_GALLERY = 1
    }
}