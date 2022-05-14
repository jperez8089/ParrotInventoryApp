package com.inventory.app.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.inventory.app.R
import com.inventory.app.data.DatabaseHandler
import com.inventory.app.databinding.ActivityAddNewRecordBinding

class AddNewRecordActivity : AppCompatActivity() {

    //we get here once user clicks from prev.

    private lateinit var binding: ActivityAddNewRecordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_record) // record
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


            // takles val
        binding.btnAdd.setOnClickListener {
            val nameStr = binding.etItemName.text.toString().trim()
            val quantityStr = binding.etItemQuantity.text.toString().trim()
            val descriptionStr = binding.etItemDescription.text.toString().trim()

            var quantityNum = 0.0
            var isError = false

            if (descriptionStr.isEmpty()) {
                isError = true
                binding.etItemDescription.requestFocus()
                binding.etItemDescription.error = getString(R.string.item_description_error)
            } else {
                binding.etItemDescription.clearFocus()
            }

            if (quantityStr.isEmpty()) {
                isError = true
                binding.etItemQuantity.requestFocus()
                binding.etItemQuantity.error = getString(R.string.item_quantity_error)
            } else {//checking for valid number
                try {
                    quantityNum = quantityStr.toDouble()
                    if (quantityNum <= 0) {
                        isError = true
                        binding.etItemQuantity.requestFocus()
                        binding.etItemQuantity.error = getString(R.string.item_quantity_less_error)
                    } else {
                        binding.etItemQuantity.clearFocus()
                    }
                } catch (e: Exception) {
                    isError = true
                    binding.etItemQuantity.requestFocus()
                    binding.etItemQuantity.error = getString(R.string.item_quantity_in_valid_error)
                }
            }

            if (nameStr.isEmpty()) {
                isError = true
                binding.etItemName.requestFocus()
                binding.etItemName.error = getString(R.string.item_name_error)
            } else {
                binding.etItemName.clearFocus()
            }

            if (!isError) {
                DatabaseHandler.getInstance().saveItem(nameStr, quantityNum, descriptionStr)
                Toast.makeText(//save and shwo toast
                    this,
                    getString(R.string.add_record_success),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {//overide default and check if user clicks
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}