package com.example.lotterymachine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var isAlreadyStarted: Boolean = false

    private val finalNumberSet: HashSet<Int> = hashSetOf()

    private val numberPickerChooseNumber: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPickerChooseNumber).apply {
            minValue = 1
            maxValue = 45
        }
    }

    private val tvBallList: List<TextView> by lazy {
        listOf(
            findViewById(R.id.tvFirstNumber),
            findViewById(R.id.tvSecondNumber),
            findViewById(R.id.tvThirdNumber),
            findViewById(R.id.tvFourthNumber),
            findViewById(R.id.tvFifthNumber),
            findViewById(R.id.tvSixthNumber),
        )
    }
    private val btnAddNumber: Button by lazy {
        findViewById(R.id.btnAddNumber)
    }
    private val btnStart: Button by lazy {
        findViewById(R.id.btnStart)
    }
    private val btnClear: Button by lazy {
        findViewById(R.id.btnClear)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirst()
        initStartButton()
        initAddButton()
        initClearButton()
    }

    private fun initFirst() {
        numberPickerChooseNumber // 호출만 해주는 이유 : lazy 하게 선언을 해서 사용되기 전에 범위가 초기화가 되지 않는다.
    }

    private fun initStartButton() {
        btnStart.setOnClickListener {

            if (isAlreadyStarted) {
                AlertDialog.Builder(this)
                    .setMessage("다시 돌리시겠어요?")
                    .setPositiveButton("확인") { _, _ ->
                        finalNumberSet.clear()
                        makeFullNumberSet()
                        finalNumberSet.forEachIndexed { idx, number ->
                            val textView = tvBallList[idx]
                            textView.text = number.toString()
                            setBallBackground(textView, number)
                        }
                    }
                    .setNegativeButton("취소") { _, _ -> }
                    .create()
                    .show()
            }

            isAlreadyStarted = true
            makeFullNumberSet()
            finalNumberSet.forEachIndexed { idx, number ->
                val textView = tvBallList[idx]
                textView.text = number.toString()
                setBallBackground(textView, number)
            }
        }
    }

    private fun makeFullNumberSet() {
        val random = Random // 나노타임 들어가나요 ? 들어가네요
        while (finalNumberSet.size != 6)
            finalNumberSet.add(random.nextInt(45) + 1) // (0~44) + 1
        finalNumberSet.sorted()
        Log.d("MainActivity", "$finalNumberSet")
    }

    private fun initAddButton() {
        btnAddNumber.setOnClickListener {
            if (isAlreadyStarted) {
                Toast.makeText(this, "초기화 후에 사용하세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (finalNumberSet.size >= 6) {
                Toast.makeText(this, "더 이상 추가할 수 없습니다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (finalNumberSet.contains(numberPickerChooseNumber.value)) {
                Toast.makeText(this, "동일한 숫자는 추가 불가능 합니다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val textView = tvBallList[finalNumberSet.size]
            val number = numberPickerChooseNumber.value
            finalNumberSet.add(number)
            textView.text = number.toString()
            setBallBackground(textView, number)
        }
    }

    private fun initClearButton() {
        btnClear.setOnClickListener {
            isAlreadyStarted = false
            finalNumberSet.clear()
            tvBallList.forEach { textView ->
                setBallHide(textView)
            }
        }

    }

    private fun setBallBackground(textView: TextView, num: Int) {
        when (num) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.yellowball)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.blueball)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.redball)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.grayball)
            in 41..45 -> textView.background = ContextCompat.getDrawable(this, R.drawable.greenball)
        }
        textView.isVisible = true
    }

    private fun setBallHide(textView: TextView) {
        textView.isVisible = false
    }

}
