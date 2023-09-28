package com.workshop.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.workshop.chatapp.Adapters.FragmentAdapter
import com.workshop.chatapp.Fragment.ProfileFragment
import com.workshop.chatapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityMainBinding
    lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //use firebase auth as well
        database=FirebaseDatabase.getInstance()

        val nameDB=database.getReference().child("Users")
            .child("userName")

        val name=intent.getStringExtra("name")
        if(name!=null) {
            binding.tvName.text = "Hi, " + name
        }
        else{
            binding.tvName.text= "Hi, "
        }

        profileFragment=ProfileFragment()

        binding.buttonProfile.setOnClickListener{
            profileFragment.show(supportFragmentManager,"")
        }

        binding.viewPager.adapter=FragmentAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)


    }


}