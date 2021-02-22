package edu.utap.freecycle

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import edu.utap.freecycle.API.SessionManager

class MainActivity : AppCompatActivity() {

    //https://guides.codepath.com/android/Storing-Secret-Keys-in-Android
    var clientId: String? = BuildConfig.CLIENT_ID
    var redirectUri: String = BuildConfig.REDIRECT_URI
    var authorizationLink: String? = BuildConfig.AUTHORIZATION_LINK
    private var token: String = ""
    private val viewModel: MainViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initObservers()
        sessionManager = SessionManager(this)

        var old_token = sessionManager.fetchAuthToken()
        if (old_token != null) {
            token = old_token
            viewModel.setToken(token)
            viewModel.fetchUser()
        }

        val loginButton = findViewById<Button>(R.id.login)
        loginButton.setOnClickListener {
            fetchNewToken()
        }
    }

    private fun fetchNewToken() {
        Log.d("HELP!","AuthLink=" + authorizationLink)
        Log.d("HELP!","ClientId=" + clientId)
        Log.d("HELP!","redirectUri=" + redirectUri)

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authorizationLink + "?response_type=token&client_id=" + clientId + "&redirect_uri=" + redirectUri))
        startActivity(intent)
    }

    private fun initObservers() {
        viewModel.observeUser().observe(this, {
            if (it != null) {
                val message = "Hi, " + it.firstname + "!\nYour username is: " + it.username
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Not Logged in!", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.observerPosts().observe(this, {
            if (it != null) {
                val message = "Title = " + it[0].title
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "There are no posts!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val uri = getIntent().data
        val uriString = uri.toString()
        if (uri != null && uriString.startsWith(redirectUri)) {
            val freshToken = uriString.substringAfterLast("access_token=").substringBefore("&")
            if (uriString.contains("access_token")) {
                token = freshToken
                viewModel.setToken(token)
                sessionManager.saveAuthToken(token)
                Log.d(javaClass.simpleName, "Token=" + token)
            } else if (uri.getQueryParameter("error") != null) {
                // Show error message here
                Toast.makeText(this, uri.getQueryParameter("error"), Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d(javaClass.simpleName, "URI RESPONSE IS NULL")
        }
    }

    //https://prasanthvel.medium.com/android-searchview-in-actionbar-androidx-and-kotlin-3467ca8e6a14
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.search_icon)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                return true
            }
        })

        val searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        searchPlate.hint = "Search Posts"
        val searchPlateView: View =
            searchView.findViewById(androidx.appcompat.R.id.search_plate)
        searchPlateView.setBackgroundColor(
                ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray //CHANGE BACKGROUND COLOR HERE!!!!!!!!!!!!!!
                )
        )

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
// do your logic here                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return super.onCreateOptionsMenu(menu)
    }
}