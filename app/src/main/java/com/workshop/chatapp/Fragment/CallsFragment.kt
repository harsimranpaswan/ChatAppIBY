package com.workshop.chatapp.Fragment

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.workshop.chatapp.VideoCallActivity
import com.workshop.chatapp.databinding.FragmentCallsBinding
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService


class CallsFragment : Fragment() {
    private lateinit var binding: FragmentCallsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCallsBinding.inflate(inflater, container, false)

        binding.buttonCall.setOnClickListener {
            if (binding.userId.text.toString().isNotEmpty()) {
                val userId = binding.userId.text.toString()
                startService(userId, userId)
                val intent = Intent(context, VideoCallActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            }
        }
        return binding.root
    }

    fun startService(userId: String, userN: String) {
        val application: Application = requireActivity().application
        val appID: Long = 1345181145
        val appSign: String = "4419ca5af910c8fa1173dfac93aff5e4cc40234a7ca4cf34c08e3e50439992c3"
        val userID: String = userId
        val userName: String = userN

        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()
        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true

        val notificationConfig = ZegoNotificationConfig()
        notificationConfig.sound = "zego_uikit_sound_call"
        notificationConfig.channelID = "CallInvitation"
        notificationConfig.channelName = "CallInvitation"

        ZegoUIKitPrebuiltCallInvitationService.init(
            application,
            appID,
            appSign,
            userID,
            userName,
            callInvitationConfig
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        ZegoUIKitPrebuiltCallInvitationService.unInit()
    }

}