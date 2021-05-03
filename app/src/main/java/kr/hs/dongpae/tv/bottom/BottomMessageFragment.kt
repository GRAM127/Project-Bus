package kr.hs.dongpae.tv.bottom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_bottom_message.*
import kr.hs.dongpae.tv.FirebaseDatabaseMap
import kr.hs.dongpae.tv.R


/*
Bottom Message Bar
    v.0
        기존 버스 시간표 역할
        새로운 디자인 적용
 */

class BottomMessageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_message, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val databaseReference = FirebaseDatabase.getInstance().reference

        val textMap = FirebaseDatabaseMap<String?>(databaseReference.child("bottom_message"))
        textMap.setOnUpdateListener {
            text_bottom_message.text = textMap.get().toList().map { it.second }.joinToString("\t\t\t\t\t\t\t\t")
        }

        text_bottom_message.isSelected = true

    }
}