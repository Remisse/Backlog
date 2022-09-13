package com.github.backlog.viewmodel

import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class ProfileViewModel(private val preferences: SharedPreferences) : ViewModel() {

    val profileImage: Flow<Uri?> = flow {
        val result = preferences.getString("profile-image", "")
            .takeIf { it != "" }
            ?.let { Uri.parse(it) }
        emit(result)
    }
    val profileName: Flow<String?> = flow {
        val result = preferences.getString("profile-name", "")
        emit(result)
    }
    val profileBio: Flow<String?> = flow {
        val result = preferences.getString("profile-bio", "")
        emit(result)
    }

    fun saveProfileImage(uri: Uri) {
        viewModelScope.launch {
            with(preferences.edit()) {
                putString("profile-image", uri.toString())
                apply()
                profileImage.combine(flowOf(uri)) { _, new ->
                    new
                }
            }
        }
    }

    fun saveProfileName(name: String) {
        viewModelScope.launch {
            with(preferences.edit()) {
                putString("profile-name", name)
                apply()
                profileName.combine(flowOf(name)) { _, new ->
                    new
                }
            }
        }
    }

    fun saveProfileBio(bio: String) {
        viewModelScope.launch {
            with(preferences.edit()) {
                putString("profile-bio", bio)
                apply()
                profileName.combine(flowOf(bio)) { _, new ->
                    new
                }
            }
        }
    }
}

class ProfileViewModelFactory(private val preferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
