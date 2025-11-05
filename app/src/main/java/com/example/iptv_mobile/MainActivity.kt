package com.example.iptv_mobile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.Button
import io.objectbox.Box
import io.objectbox.android.Admin
import io.objectbox.kotlin.boxFor

class MainActivity : AppCompatActivity() {
    private lateinit var noteBox: Box<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        noteBox = ObjectBox.store.boxFor()

        // Add some sample notes
        if (noteBox.isEmpty) {
            noteBox.put(Note(text = "Note 1"))
            noteBox.put(Note(text = "Note 2"))
            noteBox.put(Note(text = "Note 3"))
        }

        findViewById<Button>(R.id.adminButton).setOnClickListener {
            Admin(ObjectBox.store).start(this)
        }
    }
}