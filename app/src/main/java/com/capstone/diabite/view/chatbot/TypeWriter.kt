package com.capstone.diabite.view.chatbot

import android.content.Context
import android.os.Handler
import android.util.AttributeSet


class TypeWriter : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    
    private var textToAnimate: CharSequence? = null
    private var index = 0
    private var characterDelay: Long = 500
    private val handler = Handler()
    private var animationCallback: (() -> Unit)? = null

    private val characterAdder: Runnable = object : Runnable {
        override fun run() {
            textToAnimate?.let {
                if (index < it.length) {
                    append(it[index].toString())
                    index++
                    postDelayed(this, characterDelay)
                } else {
                    animationCallback?.invoke()
                }
            }

//            text = mText!!.subSequence(0, index++)
//            if (index <= mText!!.length) {
//                handler.postDelayed(this, delay)
//            }
        }
    }

    fun animateText(text: CharSequence, onComplete: (() -> Unit)? = null) {
        textToAnimate = text
        animationCallback = onComplete
        index = 0
        setText("")
        handler.removeCallbacks(characterAdder)
        handler.postDelayed(characterAdder, characterDelay)
    }

    fun setCharacterDelay(millis: Long) {
        characterDelay = millis
    }
}