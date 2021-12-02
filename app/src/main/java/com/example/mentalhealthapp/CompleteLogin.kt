package com.example.mentalhealthapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mentalhealthapp.ui.login.LoginFragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult

class CompleteLogin : AppCompatActivity() {
    var check: Boolean = false;
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()

    ) { res ->
        check=true
        this.onSignInResult(res)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_login)
        createSignInIntent()


    }

    override fun onResume() {
        super.onResume()
        if(check)
        {
            finish()
        }
       // createSignInIntent()


    }

    private fun createSignInIntent() {

        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.mipmap.ic_launcher) // Set logo drawable
            .setTheme(R.style.GreenTheme)
            .build()
        signInLauncher.launch(signInIntent)

        // [END auth_fui_create_intent]
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in

            var intent:Intent=Intent(this,MainActivity::class.java);
            startActivity(intent);
            finish();

            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }


}