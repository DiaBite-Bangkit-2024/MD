package com.capstone.diabite.view.chatbot

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.diabite.R
import com.capstone.diabite.db.ChatbotResponse

class ChatbotAdapter(var context: Context, var list: ArrayList<ChatbotResponse>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{

        const val GEMINI = 1
        const val USER = 0
    }

    private inner class GeminiViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val text : TypeWriter = itemView.findViewById(R.id.tv_gemini_response)

//        fun bind(position: Int){
//
//            val data  = list[position]
//            if(list.size-1 == position){
//                text.animateText(data.prompt)
//                text.setCharacterDelay(30)
//            }else{
//                text.setText(data.prompt)
//            }
//        }

        fun bind(position: Int) {
            val data = list[position]
            if (list.size - 1 == position) {
                text.animateText(data.prompt) {
                    // Notify completion if this is the active response
                    if (position == list.size - 1) {
                        (context as ChatbotActivity).onResponseAnimationComplete()
                    }
                }
                text.setCharacterDelay(30)
            } else {
                text.setText(data.prompt)
            }
        }

    }

    private inner class UserViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var text : TextView = itemView.findViewById(R.id.tv_user_response)

        fun bind(position: Int){
            val data = list[position]
            text.text = data.prompt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == GEMINI){
            GeminiViewHolder(LayoutInflater.from(context).inflate(R.layout.gemini_layout, parent,false))

        }else{
            UserViewHolder(LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].isUser
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].isUser == GEMINI){
            (holder as GeminiViewHolder).bind(position)
        }else{
            (holder as UserViewHolder).bind(position)
        }
    }
}