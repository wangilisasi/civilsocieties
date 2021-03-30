package tz.co.vanuserve.civilsocieties.features.csos


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tz.co.vanuserve.civilsocieties.data.CivilSociety
import tz.co.vanuserve.civilsocieties.databinding.CsoItemBinding

class CivilSocietyAdapter : ListAdapter<CivilSociety, CivilSocietyAdapter.CivilSocietyViewHolder>(CivilSocietyComparator()){


    class CivilSocietyViewHolder(private val binding: CsoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(civilSociety: CivilSociety) {
            binding.apply {
                Glide.with(itemView)
                    .load(civilSociety.avatar)
                    .centerCrop()
                    .into(csoImage)


                csoName.text=civilSociety.name
                csoDesc.text=civilSociety.description
            }
        }
    }

    class CivilSocietyComparator:DiffUtil.ItemCallback<CivilSociety>(){
        override fun areItemsTheSame(oldItem: CivilSociety, newItem: CivilSociety)= oldItem.name==newItem.name

        override fun areContentsTheSame(oldItem: CivilSociety, newItem: CivilSociety)= oldItem==newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CivilSocietyViewHolder {
        val binding=CsoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CivilSocietyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CivilSocietyViewHolder, position: Int) {
        val currentItem=getItem(position)
        if(currentItem!=null){
            holder.bind(currentItem)
        }
    }
}