package ke.co.willynganga.googlesignin

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import ke.co.willynganga.googlesignin.databinding.ActivityMainBinding
import ke.co.willynganga.googlesignin.repository.MainRepository
import ke.co.willynganga.googlesignin.viewmodel.MainViewModel
import ke.co.willynganga.googlesignin.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainViewModel

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsClient: GoogleSignInClient

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", "onStart: ${account.idToken}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainRepository = MainRepository()
        val viewModelFactory =
            MainViewModelFactory(application, mainRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)

        setupGoogleSignIn()
        setupOnClickListener()
    }

    private fun setupGoogleSignIn() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        gsClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setupOnClickListener() {
        binding.signInButton.setOnClickListener {
            signIn()
        }

        binding.logOut.setOnClickListener {
            signOut()
        }
    }

    private fun signIn() {
        val intent = gsClient.signInIntent
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResultTask(task)
            }
        }

    private fun handleSignInResultTask(task: Task<GoogleSignInAccount>?) {
        try {
            val account = task?.getResult(ApiException::class.java)

//            viewModel.sendIdToken(account?.idToken!!)

//            observeResponse()

            Toast.makeText(this, "Signed in successfully!", Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", "IdToken: ${account?.idToken}")

        } catch (ex: ApiException) {
            Log.w("MainActivity", "Fail code: ${ex.statusCode}", ex)
        }
    }

    private fun observeResponse() {
        viewModel.sendIdTokenResponse.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun signOut() {
        gsClient.signOut()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Signed out!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { ex ->
                Toast.makeText(this, ex.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}