package com.developers.apollographqlexample

import android.support.v7.widget.RecyclerView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_repo.view.*

class ReposAdapter(var items: List<GetUserRepos.Node>?,val context: Context) : RecyclerView.Adapter<ReposAdapter.RepoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RepoViewHolder {
        return RepoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_repo, parent, false))
    }

    override fun getItemCount(): Int {
        return if(items == null)
                0
        else
                items!!.count()
    }

    override fun onBindViewHolder(holder: RepoViewHolder?, position: Int) {
        holder?.repoName?.text = items!![position].name()
        holder?.repoDate?.text = items!![position].createdAt() as String?
    }

    fun setPosts(repos: List<GetUserRepos.Node>){
        items = repos
        notifyDataSetChanged()
    }
     class RepoViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
         val repoName = itemView?.repo_name
         val repoDate = itemView?.repo_date
     }
}