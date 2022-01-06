package com.nepplus.apipractice_20220106

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.nepplus.apipractice_20220106.api.APIList
import com.nepplus.apipractice_20220106.api.ServerAPI
import com.nepplus.apipractice_20220106.models.BasicResponse
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnSignUp.setOnClickListener {

//            입력한 아이디 ~ 폰번 추출.
            val inputEmail = edtEmail.text.toString()
            val inputPw = edtPassword.text.toString()
            val inputNickname = edtNickname.text.toString()
            val inputPhoneNum = edtPhone.text.toString()

//            API 호출 ( 회원가입 ) => ServerAPI + APIList 조합
            val retrofit = ServerAPI.getRetrofit()
            val apiList = retrofit.create(APIList::class.java)

//            추가된 회원가입 기능 활용
            apiList.putRequestSignUp(
                inputEmail,
                inputPw,
                inputNickname,
                inputPhoneNum
            ).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

//                    회원가입 요청 => DB에 가입 => 서버가 내려준 응답이 돌아왔을 떄 실행.

//                    code : 200만 성공. => response.isSuccessful => 코드값이 200이었나?

                    if (response.isSuccessful) {

//                        BasicResponse 형태로 자동 분석된 응답은 => 200으로 돌아왔을떄만 정상 동작
                        val br = response.body()!!  //  BasicResponse를 추출.

//                        회원가입 성공 처리만 => br 변수 이용 진행.
//                        이미 같은 구조에 대한 분서을 => 클래스 / 변수들로 만들어 둔 상태
//                        분석이 끝났다고 전제하고, 변수들을 가져다 사용

                        val signUpNickname = br.data.user.nick_name

                        Toast.makeText(this@SignUpActivity, "${signUpNickname}님 회원가입을 축하합니다.", Toast.LENGTH_SHORT).show()

                        finish()

                    }
                    else {
//                        400, 403, 404, 500 등등 모든 에러.

//                        문제 발생! => BasicResponse 정상 응답 제공 X.
                        val testStr = response.errorBody()!!.string()  //  에러의 경우는, 별도로 에러 본문 확인
                        Log.d("문제응답", testStr)

                    }


                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }


            })



        }

        btnEmailCheck.setOnClickListener {

            val inputEmail = edtEmail.text.toString()
            val retrofit = ServerAPI.getRetrofit()
            val apiList = retrofit.create(APIList::class.java)

            apiList.getRequestDuplCheck("EMAIL", inputEmail).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                    if (response.isSuccessful) {
//                        code : 200으로 성공 (사용 가능)
                        txtEmailCheckResult.text = "사용해도 좋은 이메일입니다."
                    }
                    else {
//                        그 외 : 사용하면 안되는 이메일
                        txtEmailCheckResult.text = "중복된 이메일입니다. 다른 이메일을 사용해 주십시오"
                    }


                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }


            })

        }

    }
}