package com.whalez.theteam.ui.main.team

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.whalez.theteam.CreateTeamActivity
import com.whalez.theteam.R
import com.whalez.theteam.data.Team
import com.whalez.theteam.data.User
import com.whalez.theteam.utils.ConstValues.Companion.TAG
import com.whalez.theteam.utils.hideLoading
import com.whalez.theteam.utils.showLoading
import kotlinx.android.synthetic.main.activity_create_team.*
import kotlinx.android.synthetic.main.fragment_team.*
import kotlinx.android.synthetic.main.fragment_team.loading_layout

class TeamFragment: Fragment() {

    companion object {
        const val CREATE_TEAM = 0
    }

    private val userUid = FirebaseAuth.getInstance().currentUser!!.uid
    private val ref = FirebaseDatabase.getInstance().getReference("/users/$userUid")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "TFonCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "TFonActivityCreated")

        // 소속 팀이 있는 경우
        if(haveOwnTeam()){
            showLoading(loading_layout)
            no_team_layout.visibility = View.GONE
            my_team_layout.visibility = View.VISIBLE
            setMyTeam()
            hideLoading(loading_layout)
        } 
        // 소속 팀이 없는 경우
        else {
            my_team_layout.visibility = View.GONE
            no_team_layout.visibility = View.VISIBLE

            btn_create_new_team.setOnClickListener {
                val intent = Intent(this.context, CreateTeamActivity::class.java)
                startActivityForResult(intent, CREATE_TEAM)
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CREATE_TEAM){
            if(resultCode == Activity.RESULT_OK){
                setMyTeam()
            }
        }
    }

    private fun haveOwnTeam(): Boolean {
        var result = false
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Log.d(TAG, "db 에러 : ${p0.message}")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)!!
                result = user.team.isNotEmpty()
            }
        })

        return result
    }

    private fun setMyTeam(){

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val team = p0.getValue(Team::class.java)!!
                val teamLogoUri = Uri.parse(team.teamLogoUrl)

                Glide.with(this@TeamFragment)
                    .load(teamLogoUri)
                    .into(team_logo)
                tv_team_name.text = team.teamName
//                tv_team_introduce.text = team.teamIntroduce
                // rv_list_of_players <-- 여기에 선수 명단 DB 에서 불러오기
                val members = team.teamMembers
                for(player in members){
                    // 이 안에서 작업 할지 아니면 list 자체를 adapter 에 넣는게 가능할지?.
                    // 어댑터도 만들어야 된다.
                }

                hideLoading(loading_layout)
            }

        })
    }

}