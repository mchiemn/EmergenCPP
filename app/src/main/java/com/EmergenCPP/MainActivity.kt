package com.EmergenCPP

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var userInputText: EditText
    private lateinit var translateButton: Button
    private lateinit var flashButton: Button
//    var uploadImage: Button? = null
    private lateinit var btnToggleDark: Button
    private lateinit var morseCode: TextView
//    var bitmap: Bitmap? = null
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
//        uploadImage = findViewById(R.id.UploadButton)
        flashButton = findViewById(R.id.FlashButton)
        morseCode = findViewById(R.id.MorseCode)
        btnToggleDark = findViewById(R.id.btnToggleDark)

        //Saving State of app using SHaredPreferences
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false)

        //When user reopens app after mode
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            btnToggleDark.setText("Disable Dark Mode")
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            btnToggleDark.setText("Enable Dark Mode")
        }

        //When user taps the dark mode button
        btnToggleDark.setOnClickListener(View.OnClickListener {
            if (isDarkModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isDarkModeOn", false)
                editor.apply()
                btnToggleDark.setText("Enable Dark Mode")
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isDarkModeOn", true)
                editor.apply()
                btnToggleDark.setText("Disable Dark Mode)")
            }
        })


        //Translate to Morse code on click
        translateButton.setOnClickListener(View.OnClickListener {
            morseCode.setText("") //Clear TextView for another translation
            translate()
            flashButton.setVisibility(View.VISIBLE)
            Toast.makeText(this@MainActivity, "Translated!", Toast.LENGTH_SHORT).show()
        })

        //Use flashlight to flash Morse code on click
        flashButton.setOnClickListener(View.OnClickListener {
            try {
                flashMorse()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        })

//        //Button to upload image and get text from the image
//        uploadImage.setOnClickListener(View.OnClickListener {
//            startActivityForResult(
//                Intent(
//                    Intent.ACTION_PICK,
//                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
//                ), GET_FROM_GALLERY
//            )
//        })
    }

    //Method to translate text to morse
    fun translate() {
        val userString = userInputText.text.toString()
        //Loop through message and translate
        for (n in 0 until userString.length) {
            when (userString[n]) {
                ' ' -> morseCode!!.append("/ ")
                'a', 'A' -> morseCode!!.append(".- ")
                'b', 'B' -> morseCode!!.append("-... ")
                'c', 'C' -> morseCode!!.append("-.-. ")
                'd', 'D' -> morseCode!!.append("-.. ")
                'e', 'E' -> morseCode!!.append(". ")
                'f', 'F' -> morseCode!!.append("..-. ")
                'g', 'G' -> morseCode!!.append("--. ")
                'h', 'H' -> morseCode!!.append(".... ")
                'i', 'I' -> morseCode!!.append(".. ")
                'j', 'J' -> morseCode!!.append(".--- ")
                'k', 'K' -> morseCode!!.append("-.- ")
                'l', 'L' -> morseCode!!.append(".-.. ")
                'm', 'M' -> morseCode!!.append("-- ")
                'n', 'N' -> morseCode!!.append("-. ")
                'o', 'O' -> morseCode!!.append("--- ")
                'p', 'P' -> morseCode!!.append(".--. ")
                'q', 'Q' -> morseCode!!.append("--.- ")
                'r', 'R' -> morseCode!!.append(".-. ")
                's', 'S' -> morseCode!!.append("... ")
                't', 'T' -> morseCode!!.append("- ")
                'u', 'U' -> morseCode!!.append("..- ")
                'v', 'V' -> morseCode!!.append("...- ")
                'w', 'W' -> morseCode!!.append(".-- ")
                'x', 'X' -> morseCode!!.append("-..- ")
                'y', 'Y' -> morseCode!!.append("-.-- ")
                'z', 'Z' -> morseCode!!.append("--.. ")
                '1' -> morseCode!!.append(".---- ")
                '2' -> morseCode!!.append("..--- ")
                '3' -> morseCode!!.append("...-- ")
                '4' -> morseCode!!.append("....- ")
                '5' -> morseCode!!.append("..... ")
                '6' -> morseCode!!.append("-.... ")
                '7' -> morseCode!!.append("--... ")
                '8' -> morseCode!!.append("---.. ")
                '9' -> morseCode!!.append("----. ")
                '0' -> morseCode!!.append("----- ")
                '.' -> morseCode!!.append(".-.-.- ")
                ',' -> morseCode!!.append("--..-- ")
                '?' -> morseCode!!.append("..--.. ")
                '!' -> morseCode!!.append("-.-.-- ")
                '"' -> morseCode!!.append(".-..-. ")
                '\'' -> morseCode!!.append(".----. ")
                '(' -> morseCode!!.append("-.--. ")
                ')' -> morseCode!!.append("-.--.- ")
                '&' -> morseCode!!.append(".-... ")
                ':' -> morseCode!!.append("---... ")
                ';' -> morseCode!!.append("-.-.-. ")
                '/' -> morseCode!!.append("-..-. ")
                '_' -> morseCode!!.append("..--.- ")
                '=' -> morseCode!!.append("-...- ")
                '+' -> morseCode!!.append(".-.-. ")
                '-' -> morseCode!!.append("-....- ")
                '$' -> morseCode!!.append("...-..- ")
                '@' -> morseCode!!.append(".--.-. ")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Detect the requested codes
//        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK) {
//            val selectedImage = data!!.data
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        imageToText() //Run method to detect text from image
    }

//    fun imageToText() {
//        val image: InputImage = InputImage.fromBitmap(bitmap, 0)
//        val recognizer: TextRecognizer = TextRecognition.getClient()
//        val result: Task<Text> =
//            recognizer.process(image).addOnSuccessListener(object : OnSuccessListener<Text?>() {
//                fun onSuccess(visionText: Text) {
//                    //Task is completed successfully, run processTextBlock method
//                    processTextBlock(visionText)
//                }
//            }).addOnFailureListener(object : OnFailureListener() {
//                fun onFailure(e: Exception) {
//                    //Task has failed with an exception
//                    e.printStackTrace()
//                }
//            })
//    }

    //Method to process the text to a string variable
//    private fun processTextBlock(result: Text) {
//        val resultText: String = result.getText()
//        for (block in result.getTextBlocks()) {
//            val blockText: String = block.getText()
//            val blockCornerPoints: Array<Point> = block.getCornerPoints()
//            val blockFrame: Rect = block.getBoundingBox()
//            for (line in block.getLines()) {
//                val lineText: String = line.getText()
//                val lineCornerPoints: Array<Point> = line.getCornerPoints()
//                val lineFrame: Rect = line.getBoundingBox()
//                for (element in line.getElements()) {
//                    val elementText: String = element.getText()
//                    val elementCornerPoints: Array<Point> = element.getCornerPoints()
//                    val elementFrame: Rect = element.getBoundingBox()
//                }
//            }
//        }
//        //Set the userInputText field to text that is detected from image
//        userInputText!!.setText(resultText)
//    }

    //Method to flash Morse code
    @RequiresApi(Build.VERSION_CODES.M)
    @Throws(InterruptedException::class)
    fun flashMorse() {
        if (morseCode!!.text.toString().isEmpty()) {
            Toast.makeText(this@MainActivity, "No Morse Code to Translate", Toast.LENGTH_SHORT)
                .show()
        } else {
            val morse = morseCode!!.text.toString()
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