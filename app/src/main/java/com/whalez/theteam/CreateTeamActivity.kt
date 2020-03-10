package com.whalez.theteam

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.whalez.theteam.data.Team
import com.whalez.theteam.ui.sign.RegisterPagerAdapter.Companion.photoUri
import com.whalez.theteam.utils.ConstValues
import com.whalez.theteam.utils.ConstValues.Companion.TAG
import com.whalez.theteam.utils.hideLoading
import com.whalez.theteam.utils.showLoading
import kotlinx.android.synthetic.main.activity_create_team.*
import java.util.*

class CreateTeamActivity : AppCompatActivity() {

    private var teamLogoUri: Uri? = null
    private var teamName = ""
    private var teamOwner = ""
    private var teamIntroduce = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_team)

        team_logo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        btn_cancel.setOnClickListener { finish() }

        btn_create.setOnClickListener {
            teamName = et_team_name.text.toString().trim()
            teamOwner = et_team_owner.text.toString().trim()
            if(teamOwner.isEmpty()) teamOwner = et_team_owner.hint.toString()
            teamIntroduce = et_team_introduce.text.toString()

            if(teamName.isEmpty()){
                Toast.makeText(this, "팀 명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performCreateTeam()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            teamLogoUri = data.data
            Glide.with(this)
                .load(teamLogoUri)
                .into(team_logo)
        }
    }

    private fun performCreateTeam() {

        showLoading(loading_layout)

        uploadImageToFirebaseStorage()

    }

    private fun uploadImageToFirebaseStorage() {
        if (teamLogoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(photoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "사진 저장 완료 : ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener { url ->
                    saveTeamToFirebaseDatabase(url.toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "사진 저장 실패 : ${it.message}")
            }
    }

    private fun saveTeamToFirebaseDatabase(teamLogoUrl: String){
        val ref = FirebaseDatabase.getInstance().getReference("/team")

        // 존재하는 teamName 인지 먼저 확인해야됨!
        // 여기서 말고 그전에 존재하는 팀인지 확인하는 버튼을 만드는게 더 나아 보임.
        val team = Team(teamName, teamOwner, teamIntroduce, teamLogoUrl)
        ref.setValue(team)
            .addOnSuccessListener {
                Log.d(TAG, "firebase DB에 저장됨!")
            }
            .addOnFailureListener {
                hideLoading(loading_layout)
                Toast.makeText(this, "firebase db 저장 실패: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
