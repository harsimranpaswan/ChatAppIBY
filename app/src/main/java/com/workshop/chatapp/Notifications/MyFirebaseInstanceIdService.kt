package com.workshop.chatapp.Notifications

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging

import com.google.firebase.messaging.FirebaseMessagingService


class MyFirebaseInstanceIdService:FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        val firebaseUser= FirebaseAuth.getInstance().currentUser
        //replacement for firebase instance id
        val refreshToken=FirebaseMessaging.getInstance().token.toString()

        if(firebaseUser!=null){
            updateToken(refreshToken)
        }

        super.onNewToken(token)
    }

    private fun updateToken(refreshToken: String) {
        val firebaseUser= FirebaseAuth.getInstance().currentUser
        val ref=FirebaseDatabase.getInstance().getReference().child("Tokens")
        val token=Token(refreshToken!!)
        ref.child(firebaseUser!!.uid).setValue(token)
    }
}