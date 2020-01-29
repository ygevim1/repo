package com.example.myott11

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.coroutines.delay


class AnimatedWord(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs)
{
    private var mText: String
    private var mDelay: Long
    private var mLetterSize : Int

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AnimatedWord_attributes,
            0, 0
        ).apply {

            try {
                mText = getString(R.styleable.AnimatedWord_attributes_text).toString().reversed()
                mDelay = getInt(R.styleable.AnimatedWord_attributes_delay, 0).toLong()
                mLetterSize = getInt(R.styleable.AnimatedWord_attributes_letter_size, 0)
            } finally {
                recycle()
            }
        }

        visibility = INVISIBLE

        create()
    }


    private fun setParameters(view : ImageView) {
        val lp = LinearLayout.LayoutParams(
            mLetterSize,
            mLetterSize
        )
        lp.setMargins(-(mLetterSize/5), 0, -(mLetterSize/5), 0)
        view.setLayoutParams(lp)
        // TODO: "This is Super Ugly, we need to fix the letters margins"
    }

    private fun create() {
        for (letter in mText) {
            val letterView = ImageView(context)
            val address = Helper(context).getAnimation3(letter.toString())
            letterView.setImageResource(address)
            letterView.visibility = INVISIBLE
            addView(letterView)
            setParameters(letterView)
        }
    }

    suspend fun draw() {
        visibility = VISIBLE
        // Since it's Hebrew, we need to draw from right to left (from the end to start)
        for (i in 0 until getChildCount()) {
            val letterView = getChildAt(getChildCount() - i - 1) as ImageView
            if (i>0)
                delay(mDelay)

            letterView.visibility = VISIBLE
            val avd = letterView.drawable as AnimatedVectorDrawable
            avd.start()
        }
    }
}