package com.capstone.diabite.view.auth

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.capstone.diabite.R
import com.google.android.material.textfield.TextInputLayout

class CustomEditText(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatEditText(context, attrs) {
    var isPassword: Boolean = false
    var isEmail: Boolean = false
    var isName: Boolean = false
    var isOtp: Boolean = false
    var isConfirm: Boolean = false
    var passwordReference: CustomEditText? = null


    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val parentLayout = parent.parent as? TextInputLayout
                when {
                    isPassword -> {
                        parentLayout?.error =
                            if (text.isNullOrEmpty()) context.getString(R.string.password_required)
                            else if ((s?.length ?: 0) < 5) context.getString(R.string.password_invalid)
                            else null
                    }

                    isConfirm -> {
                        parentLayout?.error = when {
                            text.isNullOrEmpty() -> context.getString(R.string.confirm_required)
                            passwordReference?.text.toString() != text.toString() -> context.getString(R.string.passwords_do_not_match)
                            else -> null
                        }
                    }

                    isEmail -> {
                        parentLayout?.error =
                            if (text.isNullOrEmpty()) context.getString(R.string.email_required)
                            else if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) context.getString(R.string.email_invalid)
                            else null
                    }

                    isName -> {
                        parentLayout?.error =
                            if (s.toString().isEmpty()) context.getString(R.string.name_required) else null
                    }

                    isOtp -> {
                        parentLayout?.error =
                            if (s.toString().isEmpty()) context.getString(R.string.otp_required) else null
                    }
                }
            }
        })
    }
}
