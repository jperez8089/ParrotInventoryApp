package com.inventory.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.inventory.app.R
import com.inventory.app.data.DatabaseHandler
import com.inventory.app.data.RecordModel
import com.inventory.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //this is the first acitivty that the user actually sees
//using bindings

    //sources for more info on them
    //https://developer.android.com/topic/libraries/view-binding
    //https://medium.com/androiddevelopers/use-view-binding-to-replace-findviewbyid-c83942471fc
    //https://www.section.io/engineering-education/view-binding-in-android/
    //https://www.youtube.com/watch?v=z0F2QTAKsWU
    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<RecordModel>()
    private lateinit var adapter: RecordsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(applicationContext, AddNewRecordActivity::class.java))
        }






                //implementing the mthod-since this is an interface being used we need the functionality here
        adapter = RecordsListAdapter(list, object : RecordsListAdapterClickEvents {
            override fun editClicked(position: Int) {
                startActivity(EditItemActivity.newIntent(list[position], this@MainActivity))
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun deleteClicked(position: Int) {
                DatabaseHandler.getInstance().deleteItem(list[position].id)
                list.removeAt(position)
                adapter.notifyDataSetChanged()
                Toast.makeText(
                    applicationContext,
                    getString(R.string.delete_record_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })






        binding.recView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {//called everytime the ui is able to be viewed
        super.onResume()

        list.clear()
        list.addAll(DatabaseHandler.getInstance().getAllItems())//all items present loaded and lets
        //adapter know
        adapter.notifyDataSetChanged()
    }
}