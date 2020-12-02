package com.example.closetagram.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import kotlinx.android.synthetic.main.fragment_alarm.*
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import java.util.*
import kotlin.collections.ArrayList

class AlarmFragment : Fragment() {
    var firestore: FirebaseFirestore? = null
    var fragmentView: View? = null
    var fragmentViewAdapter: UserFragmentRecycleriewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        var mainactivity = (activity as MainActivity)
        var contentList = ArrayList<ContentDTO>();
        fragmentView =
            LayoutInflater.from(activity).inflate(R.layout.fragment_alarm, container, false)
        firestore = FirebaseFirestore.getInstance()
        fragmentViewAdapter = UserFragmentRecycleriewAdapter()
        fragmentView?.alarmfragment_recyclerview?.adapter = fragmentViewAdapter
        fragmentView?.alarmfragment_recyclerview?.layoutManager = GridLayoutManager(activity, 3)
        return fragmentView
    }

    inner class UserFragmentRecycleriewAdapter() :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var contentFilterList: ArrayList<ContentDTO> = arrayListOf()

        init {
            firestore?.collection("images")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    //Sometime, This code return null of querySnapshot when it signout
                    if (querySnapshot == null) return@addSnapshotListener

                    //Get data
                    for (snapshot in querySnapshot.documents) {
                        if (snapshot.toObject(ContentDTO::class.java)!!.favoriteCount > 0) {
                            contentFilterList.add(snapshot.toObject(ContentDTO::class.java)!!)
                        }
                    }
                    notifyDataSetChanged()
                }
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
    }
}