package com.inventory.app.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inventory.app.data.RecordModel
import com.inventory.app.databinding.SingleRowItemBinding

class RecordsListAdapter(
    private val list: List<RecordModel>,
    val listener: RecordsListAdapterClickEvents
) : RecyclerView.Adapter<RecordsListAdapter.ViewClass>() {

    inner class ViewClass(val binding: SingleRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewClass {
        val layoutInflater = LayoutInflater.from(parent.context)



        val binding = SingleRowItemBinding.inflate(layoutInflater, parent, false)
        return ViewClass(binding)
    }

    @SuppressLint("SetTextI18n")


    override fun onBindViewHolder(holder: ViewClass, position: Int) {
        val model = list[position]
        holder.binding.tvName.text = model.name
        holder.binding.tvQuantity.text = "Quantity: ${model.quantity}"
        holder.binding.tvDescription.text = "Desc: ${model.description}"

        holder.binding.ivDelete.setOnClickListener {
            listener.deleteClicked(position)
        }
            //here is where the icons kidn of ocme to life
        holder.binding.ivEdit.setOnClickListener {
            listener.editClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
