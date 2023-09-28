package com.workshop.chatapp.Notifications
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.workshop.chatapp.ChatViewActivity

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(mRemoteMessage: RemoteMessage) {
        super.onMessageReceived(mRemoteMessage)

        val sent=mRemoteMessage.data["sent"]
        val user=mRemoteMessage.data["user"]

        val sharedPref=getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val currentOnlineUser=sharedPref.getString("currentUser","none")

        val firebaseUser=FirebaseAuth.getInstance().currentUser

        if(firebaseUser!=null && sent==firebaseUser.uid){
            if(currentOnlineUser!=user){
                //oreo notifications
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    sendOreoNotif(mRemoteMessage)
                }
                else{
                    sendNotif(mRemoteMessage)

                }
            }
        }

    }

    private fun sendOreoNotif(mRemoteMessage: RemoteMessage) {
        val user=mRemoteMessage.data["user"]
        val icon=mRemoteMessage.data["icon"]
        val title=mRemoteMessage.data["title"]
        val body=mRemoteMessage.data["body"]

        val notification=mRemoteMessage.notification
        //to send user to chatview activity on clicking the notification
        val j=user!!.replace("[\\D]".toRegex(),"").toInt()
        val intent=Intent(this,ChatViewActivity::class.java)

        val bundle=Bundle()
        bundle.putString("userId",user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT)
        val defaultSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val oreoNotif=OreoNotif(this)
        //double check for imported class
        val builder: Notification.Builder=oreoNotif.getOreoNotif(title,body,pendingIntent,defaultSound,icon)

        var i=0;
        if(j>0){
            i=j
        }
        oreoNotif.getManager!!.notify(i,builder.build())


    }

    private fun sendNotif(mRemoteMessage: RemoteMessage) {
        val user=mRemoteMessage.data["user"]
        val icon=mRemoteMessage.data["icon"]
        val title=mRemoteMessage.data["title"]
        val body=mRemoteMessage.data["body"]

        val notification=mRemoteMessage.notification
        //to send user to chatview activity on clicking the notification
        val j=user!!.replace("[\\D]".toRegex(),"").toInt()
        val intent=Intent(this,ChatViewActivity::class.java)

        val bundle=Bundle()
        bundle.putString("userId",user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT)
        val defaultSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder: NotificationCompat.Builder=NotificationCompat.Builder(this)
            .setSmallIcon(icon!!.toInt())
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)

        val notif=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var i=0;
        if(j>0){
            i=j
        }
        notif.notify(i,builder.build())

    }

}