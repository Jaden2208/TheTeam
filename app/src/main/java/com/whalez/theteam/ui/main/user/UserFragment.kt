package com.whalez.theteam.ui.main.user

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.whalez.theteam.R
import com.whalez.theteam.data.User
import com.whalez.theteam.utils.ConstValues.Companion.TAG
import com.whalez.theteam.utils.finishAndGoToLoginActivity
import com.whalez.theteam.utils.hideLoading
import com.whalez.theteam.utils.showLoading
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "UserFragment : onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "UserFragment : onStop")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "UserFragment : onAttach")
//        fetchUserProfile()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "UserFragment : onCreate")
//        fetchUserProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "UserFragment : onActivityCreated")

//        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
//        val db = FirebaseDatabase.getInstance().reference


        fetchUserProfile()

//        fetchMatchResults()
//
//        fetchManagerComments()
//
//        fetchTeammatesComments()

        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(MatchResultItem())
        adapter.add(MatchResultItem())
        adapter.add(MatchResultItem())
        adapter.add(MatchResultItem())
        rv_match_result.adapter = adapter

        btn_logout.setOnClickListener {
            AlertDialog.Builder(
                    ContextThemeWrapper(
                        activity,
                        R.style.MyAlertDialogStyle
                    )
                ).setCancelable(false)
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    finishAndGoToLoginActivity(activity!!.applicationContext, activity!!)
                    Toast.makeText(this.context, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
                }.setNegativeButton("취소") { _, _ ->}
                .show()

        }
    }

    private fun fetchUserProfile() {

        showLoading(loading_layout)

        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance()
            .getReference("/users/$userUid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)!!
                val photoUri = Uri.parse(user.profileImageUrl)
                var userTeam = user.team
                if(userTeam.isEmpty()) userTeam = "소속 팀이 없습니다."
                Glide.with(this@UserFragment)
                    .load(photoUri)
                    .into(profile_image)
                tv_name.text = user.name
                tv_team.text = userTeam
                hideLoading(loading_layout)
            }

        })


    }
//  https://www.youtube.com/watch?v=SuRiwVF5bzs&list=PL0dzCUj1L5JE-jiBHjxlmXEkQkum_M3R-&index=4
//    private fun fetchMatchResults() {
//        val ref = FirebaseDatabase.getInstance().getReference("/users")
//        ref.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onDataChange(p0: DataSnapshot) {
//                p0.children.forEach {
//                    Log.d(TAG, it.toString())
//                }
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//    }
}