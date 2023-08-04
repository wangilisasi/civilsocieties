package tz.co.vanuserve.civilsocieties.features.csos


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import tz.co.vanuserve.civilsocieties.data.CivilSociety
import tz.co.vanuserve.civilsocieties.databinding.CsoItemBinding

class CivilSocietyAdapter(private val listener:OnItemClickListener) : ListAdapter<CivilSociety, CivilSocietyAdapter.CivilSocietyViewHolder>(CivilSocietyComparator()){


    inner class CivilSocietyViewHolder(private val binding: CsoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //Implement the listener
        init{
            binding.root.setOnClickListener{
                val position=bindingAdapterPosition
                if(position!=RecyclerView.NO_POSITION){     //prevent app accessing item at index that doesnt exist
                    val item=getItem(position)
                    if(item!=null){
                        listener.onItemClick(item)
                    }
                }

            }
        }

        fun bind(civilSociety: CivilSociety) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(civilSociety.avatar)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(csoImage)


                csoName.text=civilSociety.name
                csoDesc.text=civilSociety.description
                regionTag.text=civilSociety.region
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

    interface OnItemClickListener{
        fun onItemClick(civilSociety:CivilSociety)
    }
}