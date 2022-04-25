package com.example.films.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.films.data.User
import com.example.films.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

   // private val mMainActivityViewModel : MainActivityViewModel = MainActivityViewModel()

    private val signInLauncher = registerForActivityResult( //cоздали обьект авторизации экрана
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res) // запуск самого экрана
    }// отсюда переходим потом в fun  onSignInResult

    private lateinit var database: DatabaseReference //создали обьект для записи в бд
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Choose authentication providers
        database = Firebase.database.reference // иницилизации базы данных
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build() //cписок регистрации который мы используем
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build() //создали интент для экрана firebase auth
        signInLauncher.launch(signInIntent) //запустили экран firebase auth
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.resultCode // результат с экрана  firebase auth
        if (result.resultCode == RESULT_OK) { // если результат ОК

            // Successfully signed in
            val authUser =
                FirebaseAuth.getInstance().currentUser //создаем обьект текущего пользователя firebase auth
            authUser?.let { // если он существует то сохраняем в бд
                val email = it.email?.toString() //  извелкаем мыло
                val uid = it.uid //  извелкам индефекатор юзера
                val firebaseUser = email?.let { it1 ->
                    User(
                        it1,
                        uid
                    )
                } //  создаем обьект юзер с параметрами мыло и индефикатор
                database.child("users").child(uid)
                    .setValue(firebaseUser) //  cохраняем юзера в firebase realtime
                val intent = Intent(this@MainActivity, MoviesActivity::class.java)
                startActivity(intent)

            }


        } else if (result.resultCode == RESULT_CANCELED) { // если резльтат !ОК должны обработать ошибку
            Toast.makeText(
                this@MainActivity,
                "Something wrong with registration ",
                Toast.LENGTH_SHORT
            ).show()
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        } else {
            // do not do anything
        }
    }
}