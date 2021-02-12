package org.mousehole.stuff_sellingmadeeasy.view.ui.fragment

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.mousehole.stuff_sellingmadeeasy.R
import org.mousehole.stuff_sellingmadeeasy.model.data.NewUser
import org.mousehole.stuff_sellingmadeeasy.view.ui.activity.LoginActivity


// 2- Register the user and save info in the FireDatabase
// so much fun here
class RegisterFragment : Fragment() {


    private lateinit var emailRegister : EditText
    private lateinit var verifyEmail : EditText
    private lateinit var passwordRegister : EditText
    private lateinit var verifyPassword : EditText
    private lateinit var registerbutton: FloatingActionButton


    private lateinit var registerDelegate: RegisterDelegate
    interface RegisterDelegate {
        fun registerNewUser (newUser: NewUser)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerDelegate = (context as LoginActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View= inflater.inflate(R.layout.register_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailRegister = view.findViewById(R.id.email_register)
        verifyEmail = view.findViewById(R.id.verify_email_register)
        passwordRegister = view.findViewById(R.id.register_password)
        verifyPassword = view.findViewById(R.id.verify_register_password)

        registerbutton = view.findViewById(R.id.register_button)

        registerbutton.setOnClickListener {

            if (checkInput()){
                val newUser =
                    NewUser(
                        emailRegister.text.toString().trim(),
                        passwordRegister.text.toString().trim()
                    )
                registerDelegate.registerNewUser(newUser)
                clearTextFields()
                childFragmentManager?.popBackStack()
                showToast("Register Successfull")
            }
        }
    }
    private fun clearTextFields() {

        emailRegister.text.clear()
        verifyEmail.text.clear()
        passwordRegister.text.clear()
        verifyPassword.text.clear()
    }

    private fun checkInput(): Boolean {

        when{
            emailRegister.text.toString().trim().isEmpty() ->{
                showToast("hmm! user name must be filled")
                return false
            }
            passwordRegister.text.toString().trim().isEmpty() ->{
                showToast("Aha!! You need a password!")
                return false
            }
            emailRegister.text.toString().trim() != verifyEmail.text.toString().trim() ->{
                showToast("Hmm! is this email yours? it doesn't match!")
                return false
            }
            passwordRegister.text.toString().trim() != verifyPassword.text.toString().trim() ->{
                showToast("Aha! Passwords don't match !")
                return false
            }
        }
        return true

    }

    private fun showToast(message: String) {

        context?.let {
            Toast.makeText(it, message , Toast.LENGTH_SHORT).show()
        }
    }
}