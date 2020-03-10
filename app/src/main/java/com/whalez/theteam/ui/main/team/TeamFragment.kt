package com.whalez.theteam.ui.main.team

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whalez.theteam.CreateTeamActivity
import com.whalez.theteam.R
import kotlinx.android.synthetic.main.fragment_team.*

class TeamFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_create_new_team.setOnClickListener {
            val intent = Intent(this.context, CreateTeamActivity::class.java)
            startActivity(intent)
        }
    }
}