package com.whalez.theteam.ui.main.team

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.whalez.theteam.R
import com.whalez.theteam.data.Team
import com.whalez.theteam.data.User
import com.whalez.theteam.utils.ConstValues.Companion.TAG
import com.whalez.theteam.utils.hideLoading
import com.whalez.theteam.utils.showLoading
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_team.*
import kotlinx.android.synthetic.main.fragment_team.loading_layout
import kotlinx.android.synthetic.main.player_row.view.*
import java.util.HashMap

class TeamFragment: Fragment() {

    companion object {
        const val CREATE_TEAM = 0
    }

    private val userUid = FirebaseAuth.getInstance().currentUser!!.uid
    private val usersRef = FirebaseDatabase.getInstance().getReference("/users/$userUid")

    private var teamUUID = ""

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

        checkTeam()
        /*
         * 소속 팀이 있는 경우
         */

        btn_leave_team.setOnClickListener {
            AlertDialog.Builder(
                    ContextThemeWrapper(
                        activity,
                        R.style.MyAlertDialogStyle
                    )
                ).setCancelable(false)
                .setMessage("소속 팀에서 탈퇴하시겠습니까?")
                .setPositiveButton("확인") { _, _ ->
                    leaveTeam()
                }.setNegativeButton("취소") { _, _ ->}
                .show()
        }

        /*
         * 소속 팀이 없는 경우
         */
        btn_create_new_team.setOnClickListener {
            val intent = Intent(this.context, CreateTeamActivity::class.java)
            startActivityForResult(intent, CREATE_TEAM)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "TFonActivityResult")

        if(requestCode == CREATE_TEAM){
            if(resultCode == Activity.RESULT_OK){

                refreshFragment()
            }
        }
    }


    private fun refreshFragment(){
        Log.d(TAG, "StartRefreshing")
        val ft = fragmentManager!!.beginTransaction()
        if(Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false)
        }
        ft.detach(this).attach(this).commit()
    }

    private fun checkTeam() {
        showLoading(loading_layout)
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(TAG, "db 에러 : ${p0.message}")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)!!
                teamUUID = user.team
                if(teamUUID.isNotEmpty()) haveOwnTeam()
                else haveNoTeam()
            }
        })
    }

    private fun haveOwnTeam(){
        no_team_layout.visibility = View.GONE
        my_team_layout.visibility = View.VISIBLE
        fetchMyTeam()
    }

    private fun haveNoTeam() {
        my_team_layout.visibility = View.GONE
        no_team_layout.visibility = View.VISIBLE
        hideLoading(loading_layout)
    }

    private fun fetchMyTeam(){

        val teamsRef = FirebaseDatabase.getInstance().getReference("/teams/$teamUUID")
        teamsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val team = p0.getValue(Team::class.java)!!
                if(team.teamLogoUrl.isNotEmpty()) {
                    val teamLogoUri = Uri.parse(team.teamLogoUrl)
                    Glide.with(this@TeamFragment)
                        .load(teamLogoUri)
                        .into(team_logo)
                }
                tv_team_name.text = team.teamName
                Log.d(TAG, "TEAMNAME: ${team.teamName}")
//                tv_team_introduce.text = team.teamIntroduce

                val adapter = GroupAdapter<GroupieViewHolder>()
                val members = team.teamMembers
                for(memberUid in members){
                    val memberName = getNameFromDB(memberUid)
                    adapter.add(TeamMember(memberName))
                }

                rv_list_of_players.adapter = adapter

                hideLoading(loading_layout)
            }

        })
    }

    private fun getNameFromDB(memberUid: String): String {
        var result = "no_name"
        val ref = FirebaseDatabase.getInstance().getReference("/users/$memberUid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)!!
                result = user.name
            }
        })

        return result
    }

    private fun leaveTeam(){
        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef = FirebaseDatabase.getInstance().getReference("/users/$userUid")
        val childUpdates = HashMap<String, Any>()
        childUpdates["team"] = ""
        userRef.updateChildren(childUpdates)
            .addOnSuccessListener {
                refreshFragment()
                Toast.makeText(this.context, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show()
            }

    }

}

class TeamMember(val name: String): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.player_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_player_name.text = name
    }

}