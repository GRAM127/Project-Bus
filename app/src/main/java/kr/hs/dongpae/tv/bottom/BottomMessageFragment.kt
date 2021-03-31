package kr.hs.dongpae.tv.bottom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_bottom_message.*
import kr.hs.dongpae.tv.R


/*
Bottom Message Bar
    v.0
        기존 버스 시간표 역할
        새로운 디자인 적용
 */

class BottomMessageFragment : Fragment() {

    private val messageList = listOf(
        "제작 : 프로젝트 버스, k1a1",
        "현재 테스트 버전입니다. 오류가 발생할 수 있습니다.",
        "TV 옆 QR코드를 스캔하여 설문에 참여해 주세요."
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_message, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        text_bottom_message.text = messageList.joinToString("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t")
        text_bottom_message.isSelected = true

    }
}