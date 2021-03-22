package com.example.repofinder

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

/**
 * This Activity controls the flow of the UI.
 */
class MainActivity : AppCompatActivity(), Adapter.RepoListener {

    private lateinit var viewModel: ViewModel
    private lateinit var repoAdapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadGif()

        // Get ViewModel from providers with activity scope
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        // Setup recyclerView and adapter
        repoAdapter = Adapter(this)
        recyclerView.adapter = repoAdapter

        // Observe live data in viewModel
        viewModel.repositories.observe(this, { repos ->
            repoAdapter.populateRepos(repos)
            if (repos.isNotEmpty()) {
                // Swap gif with repositories
                img_gif.visibility = View.GONE
            }
        })

        // Display an error message if something goes wrong
        viewModel.errorMessage.observe(this, Observer {
            return@Observer Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    // Loads gif inside the webView
    @SuppressLint("SetJavaScriptEnabled")
    private fun loadGif() {
        img_gif.setBackgroundColor(0)
        img_gif.settings.javaScriptEnabled = true
        val data = "<iframe src=\"https://giphy.com/embed/26u4nJPf0JtQPdStq\" " +
                "width=\"100%\" height=\"150\" frameBorder=\"0\" style='pointer-events:none; border-radius:10px;'></iframe>"
        img_gif.loadData(data, "text/html; charset=utf-8", "UTF-8")
    }

    // Performs search based on input
    fun submitSearch(view: View) {
        val keywords = text_input.text.toString()

        if (keywords.isEmpty()) {
            return Toast.makeText(this, R.string.empty_input, Toast.LENGTH_LONG).show()
        }

        hideKeyboard()
        text_input.clearFocus()
        viewModel.initSearch()
        viewModel.searchRepositories(keywords)
    }

    // View repository detailed info in browser
    override fun onRepoClicked(repoUrl: String) {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(repoUrl)
        }.also { startActivity(it) }
    }
}