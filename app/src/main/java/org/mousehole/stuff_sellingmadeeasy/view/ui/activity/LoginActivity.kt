package org.mousehole.stuff_sellingmadeeasy.view.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.mousehole.stuff_sellingmadeeasy.R
import org.mousehole.stuff_sellingmadeeasy.model.data.NewUser
import org.mousehole.stuff_sellingmadeeasy.view.ui.fragment.RegisterFragment


//1- First login Activity where the user either login or sign up

class LoginActivity : AppCompatActivity(), RegisterFragment.RegisterDelegate{

    private val registerFragment : RegisterFragment = RegisterFragment()

    private lateinit var emailLogin : EditText
    private lateinit var passwordLogin : EditText
    private lateinit var buttonLogin: FloatingActionButton
    private lateinit var registerLogin: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        emailLogin = findViewById(R.id.sign_in_email_main)
        passwordLogin = findViewById(R.id.sign_in_password_main)
        buttonLogin = findViewById(R.id.sign_in_Button_main)
        registerLogin = findViewById(R.id.signup_register_main)

        FirebaseAuth.getInstance().currentUser?.let {

            if (it.isEmailVerified)
                openStuffMain()
            else
                showMessage("Verify your Email")
        }


        buttonLogin.setOnClickListener {
            if (checkInput()) {
                val emailAddress = emailLogin.text.toString().trim()
                val password = passwordLogin.text.toString().trim()

                FirebaseAuth
                    .getInstance()
                    .signInWithEmailAndPassword(emailAddress,password)
                    .addOnCompleteListener {

                        if(it.isComplete && it.isSuccessful && FirebaseAuth.getInstance().currentUser?.isEmailVerified == true){
                            openStuffMain()
                        }else {
                            showMessage(it.exception?.localizedMessage ?: "Error Logging in")
                        }

                    }
            }
        }



        registerLogin.setOnClickListener {

            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                .add(R.id.sign_in_frame,registerFragment)
                .addToBackStack(registerFragment.tag)
                .commit()
        }




    }

    private fun checkInput(): Boolean {

        when {
            emailLogin.text.toString().trim().isEmpty() -> {
                showMessage("Aha!! Trying to login without an email")
            }
            passwordLogin.text.toString().trim().isEmpty()->{
                showMessage("aha! trying to login without a password!")
            }
        }
        return true

    }

    private fun openStuffMain(){
        startActivity(Intent(this, StuffMainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })

    }

    private fun showMessage(message: String){

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun registerNewUser(newUser: NewUser) {

        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(newUser.userName, newUser.userPassword)
            .addOnCompleteListener {

                if(it.isComplete && it.isSuccessful){

                    if(FirebaseAuth.getInstance().currentUser?.isEmailVerified == true){
                        openStuffMain()

                    } else {
                        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                    }

                }else {
                    showMessage(it.exception?.localizedMessage ?: "hmm! Sign Up failed. TRY AGAIN")
                }

            }
    }
}