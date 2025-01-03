package com.example.strongest.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class AuthenticationHandler {
    private var auth: FirebaseAuth

    init {
        auth = Firebase.auth
    }

    fun didUserLogin(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    fun getUserInfo(): FirebaseUser? {
        val user = Firebase.auth.currentUser
        user?.let {
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl
            val emailVerified = it.isEmailVerified
            val uid = it.uid
        }
        return user
    }

    fun signOut() {
        Firebase.auth.signOut()
    }
}