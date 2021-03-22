package com.example.repofinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.repofinder.databinding.EntryRepoBinding
import com.example.repofinder.network.Repository

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List].
 */
class Adapter(private val repoListener: RepoListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var repositories = listOf<Repository>()

    // Set ItemCount to repository list size
    override fun getItemCount() = repositories.size

    /* Create new [RecyclerView] item views */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return RepoViewHolder(
            DataBindingUtil.inflate(
                inflater, R.layout.entry_repo, parent, false
            )
        )
    }

    /* Replace the contents of a view */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RepoViewHolder).binding.apply {
            repository = repositories[position]
            executePendingBindings()
        }
    }

    /* ViewHolder inner class to handle click listener */
    inner class RepoViewHolder(var binding: EntryRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                repoListener.onRepoClicked(repositories[adapterPosition].url)
            }
        }
    }

    interface RepoListener {
        fun onRepoClicked(repoUrl: String)
    }

    fun populateRepos(repoList: List<Repository>) {
        repositories = repoList
        notifyDataSetChanged()
    }
}

