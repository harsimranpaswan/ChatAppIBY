package com.workshop.chatapp.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.workshop.chatapp.Adapters.UsersAdapter
import com.workshop.chatapp.Models.Users
import com.workshop.chatapp.databinding.FragmentChatsBinding

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    lateinit var database: FirebaseDatabase
    var list: ArrayList<Users> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentChatsBinding.inflate(inflater,container,false)
        database=FirebaseDatabase.getInstance()

        val adapter: UsersAdapter= UsersAdapter(list,context)
        binding.chatsRecyclerView.adapter=adapter

        val layoutManager: LinearLayoutManager= LinearLayoutManager(context)
        binding.chatsRecyclerView.layoutManager=layoutManager

        database.getReference().child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for(dataSnapshot: DataSnapshot in snapshot.children){
                    val users: Users? = dataSnapshot.getValue(Users::class.java)
                    users?.userId=dataSnapshot.key
                    if(users?.userId.equals(FirebaseAuth.getInstance().uid)){
                        users?.userName="Myself"
                    }
                    //to hide
//                    if(!users?.userId.equals(FirebaseAuth.getInstance().uid)){
//                        list.add(users!!)
//                    }
                    list.add(users!!)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Your code for onCancelled goes here
            }
        })


        return binding.root
    }

}
