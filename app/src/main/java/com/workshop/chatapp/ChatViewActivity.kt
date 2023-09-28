package com.workshop.chatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.workshop.chatapp.Adapters.ChatAdapter
import com.workshop.chatapp.Fragment.ProfileFragment
import com.workshop.chatapp.Models.Messages
import com.workshop.chatapp.Models.Users
import com.workshop.chatapp.Notifications.*
import com.workshop.chatapp.databinding.ActivityChatViewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChatViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatViewBinding
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var profileFragment: ProfileFragment
    lateinit var database: FirebaseDatabase

    var notify = false
    var apiService: APIService?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        apiService=Client.Client.getClient("https://fcm.googleapis.com/")!!.create(APIService::class.java)


        lateinit var messages: ArrayList<Messages>
        lateinit var chatAdapter: ChatAdapter

        val senderId = firebaseAuth.uid
        val recieverId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")
        val profilePic = intent.getStringExtra("profilePic")

        messages = ArrayList()
        chatAdapter = ChatAdapter(messages, this, recieverId)

        binding.chatsRecyclerView.adapter = chatAdapter
        val layoutManager: LinearLayoutManager = LinearLayoutManager(this)
        binding.chatsRecyclerView.layoutManager = layoutManager

        //to identify who is sender and who is reciever
        val senderRoom = senderId + recieverId
        val recieverRoom = recieverId + senderId

        binding.userName.text = userName
        Picasso.get().load(profilePic).placeholder(R.drawable.default_profile)
            .into(binding.profileImage)

        database.getReference().child("Chats").child(senderRoom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        val model: Messages? = dataSnapshot.getValue(Messages::class.java)
                        model?.messageId = dataSnapshot.key
                        messages.add(model!!)
                        chatAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Your code for onCancelled goes here
                }
            })

        binding.buttonSend.setOnClickListener {
            notify=true
            val messageText = binding.enterMessage.text.toString()
            val model: Messages = Messages(senderId, messageText)
            model.timestamp = Date().time
            binding.enterMessage.text = null

            database.getReference().child("Chats").child(senderRoom).push().setValue(model)
                .addOnSuccessListener {
                    database.getReference().child("Chats").child(recieverRoom).push()
                        .setValue(model)
                        .addOnSuccessListener {
                        }
                }

            //implement notifications here
            //firebaserUser!!.uid in place of sender room
            val reference = database.getReference().child("Users").child(firebaseAuth!!.uid.toString())
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(Users::class.java)
                    if (notify) {
                        sendNotification(recieverId, userName, messageText)
                    }
                    notify = false
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }

        binding.buttonBack.setOnClickListener {
            backButton()
        }

        profileFragment = ProfileFragment()
        binding.profileImage.setOnClickListener {
            profileFragment.show(supportFragmentManager, "")
        }
        binding.userName.setOnClickListener {
            profileFragment.show(supportFragmentManager, "")
        }

    }

    override fun onBackPressed() {
        backButton()
    }

    fun backButton(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun sendNotification(recieverId: String?, userName: String?, messageText: String?) {
        val ref=database.getReference().child("Tokens")
        val query=ref.orderByKey().equalTo(recieverId)
        query.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(dataSnapshot in snapshot.children){
                    val token: Token?=dataSnapshot.getValue(Token::class.java)
                    val data=Data(FirebaseAuth.getInstance().uid.toString(),
                        R.mipmap.ic_launcher.toString(),
                    "$userName: $messageText", "New Message",recieverId.toString())
                    //CHECK FOR BUG
                    val sender= Sender(data!!,token!!.getToken().toString())

                    apiService!!.sendNotification(sender)
                        .enqueue(object : Callback<MyResponse>{
                            override fun onResponse(
                                call: Call<MyResponse>,
                                response: Response<MyResponse>
                            ) {
                                if(response.code()==200){
                                    if(response.body()!!.success!==1){
                                        Toast.makeText(this@ChatViewActivity,"Notification Not Sent",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}