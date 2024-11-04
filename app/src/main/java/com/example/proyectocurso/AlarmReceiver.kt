package com.example.proyectocurso

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val mediaPlayer: MediaPlayer = MediaPlayer.create(context, alarmSound)

        mediaPlayer.start()

        Toast.makeText(context, "¡Alarma activada!", Toast.LENGTH_SHORT).show()
    }
}