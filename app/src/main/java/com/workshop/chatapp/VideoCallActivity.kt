package com.workshop.chatapp

import android.R.attr.button
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.workshop.chatapp.databinding.ActivityVideoCallBinding
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import java.util.*


class VideoCallActivity : AppCompatActivity() {

    lateinit var voiceButton: ZegoSendCallInvitationButton
    lateinit var videoButton: ZegoSendCallInvitationButton
    lateinit var recieverId: EditText

    private lateinit var binding: ActivityVideoCallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        voiceButton=binding.audioCallBtn
        videoButton=binding.videoCallBtn
        recieverId=binding.recieverId

        val userId=intent.getStringExtra("userId")
        recieverId.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val targetUserId = recieverId.text.toString().trim()
                    voiceCall(targetUserId, targetUserId)
                    videoCall(targetUserId, targetUserId)

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    fun voiceCall(targetUserID:String,targetUserName:String){
        voiceButton.setIsVideoCall(false)
        voiceButton.setResourceID("zego_uikit_call")
        voiceButton.setInvitees(Collections.singletonList(ZegoUIKitUser(targetUserID, targetUserName)))
    }
    fun videoCall(targetUserID:String,targetUserName:String){
        videoButton.setIsVideoCall(true)
        videoButton.setResourceID("zego_uikit_call")
        videoButton.setInvitees(Collections.singletonList(ZegoUIKitUser(targetUserID, targetUserName)))

    }
}