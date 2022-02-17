package com.example.qr_kode_scanner

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import java.util.*

private const val CAMERA_REQUIEST_CODE = 101

class MainActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SetUpPremation()
        codeScanner()
    }





    private fun codeScanner(){
        codeScanner =CodeScanner(this, findViewById(R.id.scanner_view))
        codeScanner.apply {
            camera =CodeScanner.CAMERA_BACK
            formats= CodeScanner.ALL_FORMATS
            autoFocusMode= AutoFocusMode.SAFE
            scanMode= ScanMode.CONTINUOUS
            isAutoFocusEnabled= true
            isFlashEnabled =false

            decodeCallback= DecodeCallback {
                runOnUiThread {
                    val text_View = findViewById<TextView>(R.id.text_view)
                    text_View.text = it.text
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main"," there is an error ${it.message}" )
                }
            }


        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()

    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }

    private fun SetUpPremation(){
        val permission =ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED){
            MakeRequest()
        }
    }
    private fun MakeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUIEST_CODE)
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUIEST_CODE ->{
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "you need the camera permission to be able to use this app ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}