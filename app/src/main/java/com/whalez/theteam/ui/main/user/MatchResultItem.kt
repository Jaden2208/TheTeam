package com.whalez.theteam.ui.main.user

import com.whalez.theteam.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class MatchResultItem : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.match_result_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // 나중에 각 아이템들에 대해 리스트에서 호출됨...
    }
}