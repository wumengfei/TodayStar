package com.ctbri.staroftoday;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.CountDownTimer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ctbri.staroftoday.data.User;
import com.ctbri.staroftoday.fragment.MyDialogFragment;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.CommonUtil;
import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.utils.SPUtil;
import com.umeng.analytics.MobclickAgent;


import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Murphy on 16/8/29.
 */
public class RegisterActivity extends FragmentActivity implements OnClickListener, DatePickerDialog.OnDateSetListener{


    @Bind(R.id.et_phone)
            EditText etPhone;
    @Bind(R.id.et_password)
            EditText etPassword;
    @Bind(R.id.et_baby_name)
            EditText etBabyName;
    @Bind(R.id.et_validate_code)
            EditText etValidateCode;
    @Bind(R.id.b_validate_code)
            Button bValidateCode;
    @Bind(R.id.tv_baby_birthday)
    TextView tvBabyBirthday;
    @Bind(R.id.b_register)
    Button bRegister;

    private String validateCode;
    private String getUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        tvBabyBirthday.setOnClickListener(this);
        bValidateCode.setOnClickListener(this);
        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //判断验证码
            case R.id.b_validate_code:
                String tel = validateTel(etPhone.getText().toString());
                RequestQueue mQueue = Volley.newRequestQueue(this);
                if (tel == null){
                    validateCode = CommonUtil.generateValidateCode() + "";
                    new TimeCount(60000,1000).start();
                    getUrl = Constants.YXT+"/rest/android/user/register?"+"validateCode="+validateCode
                            +"&accountNumber="+tel+"&registerType="+ null;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl, null,
                            new Response.Listener<JSONObject>(){
                                @Override
                                public void onResponse(JSONObject response) {
                                    try{
                                        String strState = response.getString("state");
                                        switch (Integer.parseInt(strState)){
                                            case 0:
                                                Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                                                break;
                                            case 1:
                                                Toast.makeText(RegisterActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                                                break;
                                            case 2:
                                                Toast.makeText(RegisterActivity.this, "该号码已注册", Toast.LENGTH_SHORT).show();
                                                break;
                                            case 3:
                                                Toast.makeText(RegisterActivity.this, "该号码已注册", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }catch (Exception e){
                                        System.out.println("request failed!");
                                    }
                                }
                            }, new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("request failed!");
                        }
                    });
                    mQueue.add(jsonObjectRequest);
//                    try {
//                        mQueue.add(new JsonObjectRequest(Constants.POST,
//                                JsonRequest.validateCode(String.valueOf(
//                                                CommonUtil.generateValidateCode()), tel, null
//                                ), new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject result) {
//
//                            }
//                        }));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_baby_birthday:
                Toast.makeText(RegisterActivity.this, "birthday", Toast.LENGTH_SHORT).show();
                MyDialogFragment.datePickerInstance().show(getFragmentManager(), "");
                break;
            case R.id.b_register:
                String password = etPassword.getText().toString().trim();
                String tel1 = etPhone.getText().toString().trim();
                String babyName = etBabyName.getText().toString().trim();
                String babyBirthday = tvBabyBirthday.getText().toString().trim();
                password = DigestUtils.md5Hex(password);
                String result = validate(password, tel1, babyName);
                RequestQueue volleyRequestQueue = Volley.newRequestQueue(this);
                JSONObject params = new JSONObject();
                if (result == null){
                    try {
                        params.put("password", password);
                        params.put("userName", tel1);
                        params.put("nickName", babyName);
                        params.put("birthday", babyBirthday);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST, Constants.YXT+"/rest/android/user/inputUserInfo", params,
                            new Response.Listener<JSONObject>(){

                                @Override
                                public void onResponse(JSONObject result) {
                                    int state;
                                    try{
                                        state = result.getInt("registerStatus");
                                        //message = result.getString("message");
                                        if (state == 1) {
                                            SPUtil sp = SPUtil.getInstance(RegisterActivity.this);
                                            JSONObject data = result.getJSONObject("data");
                                            sp.putString("password", data.getString("password"));
                                            sp.putString("userName", data.getString("username"));
                                            sp.putBoolean("login", true);

                                            // setResult(Activity.RESULT_OK);
                                            if (data.getString("rid").equals("7")){
                                                startActivity(new Intent(
                                                        RegisterActivity.this,
                                                        ParentsMainActivity.class));
                                                finish();
                                            }else if (data.getString("rid").equals("6")){
                                                startActivity(new Intent(
                                                        RegisterActivity.this,
                                                        TeacherMainActivity.class));
                                                finish();
                                            }else{
                                                Toast.makeText(RegisterActivity.this,
                                                        "注册失败", Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        } else {
                                            Toast.makeText(RegisterActivity.this,
                                                    "注册失败", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }catch(Exception e){
                                        System.out.println("request failed!");
                                    }
                                }
                            }, new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("request failed");
                        }
                    }
                    );
                    volleyRequestQueue.add(jsonObjectRequest);
                }else{
                    Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
                }



        }

    }

    private String validate(String password, String tel, String babyName) {
        if (TextUtils.isEmpty(password))
            return "请输入密码";
        if (TextUtils.isEmpty(tel))
            return "请输入正确的手机号";
        if (TextUtils.isEmpty(babyName))
            return "宝宝姓名不能为空";
        if (!etValidateCode.getText().toString().equals(validateCode))
            return "验证码不正确";
        return null;
    }

    private String validateTel(String tel) {
        if (TextUtils.isEmpty(tel))
            return "请输入正确的手机号";
        return null;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        //否则显示的月份比选择的月份少一个月
        int month = monthOfYear + 1;
        tvBabyBirthday.setText(year + "-" + month + "-" + dayOfMonth);
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            bValidateCode.setText("获取验证码");
            bValidateCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            bValidateCode.setClickable(false);//防止重复点击
            bValidateCode.setText(millisUntilFinished / 1000 + "s");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
