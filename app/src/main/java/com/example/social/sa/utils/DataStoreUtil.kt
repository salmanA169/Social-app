package com.example.social.sa.utils

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore


val Context.datastore by preferencesDataStore("data")
