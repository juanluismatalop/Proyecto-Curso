package com.example.proyectocurso

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var buttonCalculadora: Button
    private lateinit var buttonAden: Button
    private lateinit var buttonEmergencias: Button
    private lateinit var buttonAlarma: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonCalculadora = findViewById(R.id.button_calculadora)
        buttonAden = findViewById(R.id.button_aden)
        buttonEmergencias = findViewById(R.id.button_emergencias)
        buttonAlarma = findViewById(R.id.button_alarma)

        initEvent()
    }

    private fun initEvent() {

        buttonCalculadora.setOnClickListener {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_APP_CALCULATOR)
            }
            startActivity(intent)
        }

        buttonAden.setOnClickListener {
            val url = "https://www.facebook.com/AsociacionDeDiabeticosDeJaen/?locale=es_ES"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        buttonEmergencias.setOnClickListener {
            requestCallPermission()
        }

        buttonAlarma.setOnClickListener {
            setAlarmIn15Minutes()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openCallActivity()
        } else {
            Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestCallPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED -> {
                openCallActivity()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

    private fun openCallActivity() {
        val intent = Intent(this, CallActivity::class.java)
        startActivity(intent)
    }

    private fun requestExactAlarmPermission() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
            Toast.makeText(this, "Debes permitir el acceso a alarmas exactas.", Toast.LENGTH_LONG).show()
        } else {
            setAlarmIn15Minutes()
        }
    }
    //comentario
    private fun setAlarmIn15Minutes() {
        requestExactAlarmPermission()
    }
}
