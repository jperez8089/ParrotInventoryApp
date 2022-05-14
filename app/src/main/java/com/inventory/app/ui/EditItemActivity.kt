package com.inventory.app.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.inventory.app.R
import com.inventory.app.data.DatabaseHandler
import com.inventory.app.data.RecordModel
import com.inventory.app.databinding.ActivityEditItemBinding

class EditItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditItemBinding

    companion object {

        private var model: RecordModel? = null









                   fun newIntent(model: RecordModel, context: Context): Intent {
                         this.model = model



            return Intent(context, EditItemActivity::class.java)
        }
    }











    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.update_record)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.etItemName.setText(model!!.name)
        binding.etItemQuantity.setText(model!!.quantity.toString())
        binding.etItemDescription.setText(model!!.description)

        binding.btnUpdate.setOnClickListener {
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
            } else {
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
                DatabaseHandler.getInstance()
                    .updateItem(nameStr, quantityNum, descriptionStr, model!!.id)
                Toast.makeText(
                    this,
                    getString(R.string.update_record_success),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}