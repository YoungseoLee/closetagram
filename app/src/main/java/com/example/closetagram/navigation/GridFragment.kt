package com.example.closetagram.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.closetagram.MainActivity
import com.example.closetagram.R
import com.example.closetagram.navigation.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_grid.*
import kotlinx.android.synthetic.main.fragment_grid.view.*
import java.util.*
import kotlin.collections.ArrayList

class GridFragment : Fragment() {
    var firestore: FirebaseFirestore? = null
    var fragmentView: View? = null
    var searchView: androidx.appcompat.widget.SearchView? = null
    var fragmentViewAdapter: UserFragmentRecycleriewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        var mainactivity = (activity as MainActivity)
        var contentList = ArrayList<ContentDTO>();
        fragmentView =
            LayoutInflater.from(activity).inflate(R.layout.fragment_grid, container, false)
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("images")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                //Sometime, This code return null of querySnapshot when it signout
                if (querySnapshot == null) return@addSnapshotListener

                //Get data
                for (snapshot in querySnapshot.documents) {
                    contentList.add(snapshot.toObject(ContentDTO::class.java)!!)
                }
            }
        fragmentViewAdapter = UserFragmentRecycleriewAdapter(contentList)
        fragmentView?.gridfragment_recyclerview?.adapter = fragmentViewAdapter
        fragmentView?.gridfragment_recyclerview?.layoutManager = GridLayoutManager(activity, 3)

        fragmentView?.content_search?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fragmentViewAdapter?.getFilter()?.filter(newText)
                return false
            }
        })
        return fragmentView
    }

    inner class UserFragmentRecycleriewAdapter(private var contentList: ArrayList<ContentDTO>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        Filterable {
        var contentFilterList: ArrayList<ContentDTO> = arrayListOf()

        init {
            contentFilterList = contentList
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var width = resources.displayMetrics.widthPixels / 3
            var imageview = ImageView(parent.context)
            imageview.layoutParams = LinearLayoutCompat.LayoutParams(width, width)
            return CustomviewHolder(imageview)
        }

        inner class CustomviewHolder(var imageview: ImageView) :
            RecyclerView.ViewHolder(imageview) {

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var imageview = (holder as CustomviewHolder).imageview
            Glide.with(holder.itemView.context).load(contentFilterList[position].imageUrl)
                .apply(RequestOptions().centerCrop()).into(imageview)
        }

        override fun getItemCount(): Int {
            return contentFilterList.size
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val charSearch = constraint.toString()
                    if (charSearch.isEmpty()) {
                        contentFilterList = contentList
                    } else {
                        val resultList = ArrayList<ContentDTO>()
                        for (row in contentList) {
                            var tagList = row.tags
                            for (tag in tagList) {
                                if (tag.toLowerCase().contains(charSearch.toLowerCase())) {
                                    resultList.add(row)
                                    break
                                }
                            }
                        }
                        contentFilterList = resultList
                    }
                    val filterResults = FilterResults()
                    filterResults.values = contentFilterList
                    return filterResults
                }

                @Suppress("UNCHECKED_CAST")
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    contentFilterList = results?.values as ArrayList<ContentDTO>
                    notifyDataSetChanged()

                }
            }
        }
    }
}