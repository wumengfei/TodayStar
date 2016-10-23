package com.ctbri.staroftoday;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ctbri.staroftoday.MyApplication;
import com.ctbri.staroftoday.net.JsonRequest;
import com.ctbri.staroftoday.utils.Constants;
import com.ctbri.staroftoday.utils.SPUtil;
import com.ctbri.staroftoday.R;
import com.umeng.analytics.MobclickAgent;


/**

 * @ description:��¼ҳ��
 */
public class LoginActivity extends Activity implements OnClickListener {
	private EditText etName, etPsw;
	private TextView red_hint;
	private RequestQueue mQueue;
	private String userName, password;

	protected void onCreate(Bundle savedInstanceState) {
		mQueue = Volley.newRequestQueue(this);
		setContentView(R.layout.activity_login);
		etName = (EditText) findViewById(R.id.et_name);
		etPsw = (EditText) findViewById(R.id.et_password);
		red_hint=(TextView)findViewById(R.id.red_hint);
		findViewById(R.id.bt_login).setOnClickListener(this);
		findViewById(R.id.bt_register).setOnClickListener(this);
		
		super.onCreate(savedInstanceState);		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login:
			userName = etName.getText().toString();
			password = etPsw.getText().toString();
			userName = userName.trim();
			password = password.trim();
			Map<Boolean, String> validateResult = validateUserNameAndPassword(
					userName, password);
			String errorInfo = validateResult.get(false);
			if (errorInfo != null) {
				Toast.makeText(this, errorInfo, Toast.LENGTH_SHORT).show();
			} else {
				password = DigestUtils.md5Hex(password);
				try {
					mQueue.add(new JsonObjectRequest(
							Constants.HOST+"login", JsonRequest
									.loginRequest(userName, password),
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject result) {
									int state;
									//String message;
									try {
										state = result.getInt("state");
										//message = result.getString("message");
										if (state == 1) {
											JSONObject data = result
													.getJSONObject("data");
											MyApplication.uid = data
													.getString("uid");
											MyApplication.rid = data
													.getString("rid");
											MyApplication.kid = data
													.getString("kid");
											MyApplication.cid = data
													.getString("cid");
											MyApplication.userName = userName;
											MyApplication.passWord = password;
											cacheLoginInfo();
											// setResult(Activity.RESULT_OK);
											if (data.getString("rid").equals("7")){
												startActivity(new Intent(
														LoginActivity.this,
														ParentsMainActivity.class));
												finish();
											}else if (data.getString("rid").equals("6")){
											startActivity(new Intent(
													LoginActivity.this,
													TeacherMainActivity.class));
											finish();
											}else{												
												Toast.makeText(LoginActivity.this,
														"登陆失败", Toast.LENGTH_SHORT)
														.show();
											}
										} else {										
											red_hint.setVisibility(View.VISIBLE);											
										}
									} catch (JSONException e) {
										e.printStackTrace();
										Toast.makeText(LoginActivity.this,
												e.getMessage(),
												Toast.LENGTH_SHORT).show();
									}
								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									Toast.makeText(LoginActivity.this,
											error.getMessage(),
											Toast.LENGTH_SHORT).show();
								}
							}));
					// mQueue.start();
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(LoginActivity.this, "请连接网络",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.bt_register:
			//Toast.makeText(LoginActivity.this, "点击注册", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	// �����û�������뵽����
	private void cacheLoginInfo() {
		
		SPUtil sp = SPUtil.getInstance(this);
		sp.putString(MyApplication.KEY_USERNAME, userName);
		sp.putString(MyApplication.KEY_PASSWORD, password);
		sp.putString(MyApplication.KEY_USERID, MyApplication.uid);
		sp.putString(MyApplication.KEY_KID, MyApplication.kid);
		sp.putString(MyApplication.KEY_RID, MyApplication.rid);
		sp.putString(MyApplication.KEY_CID, MyApplication.cid);
	}

	private Map<Boolean, String> validateUserNameAndPassword(String userName,
			String password) {
		Map<Boolean, String> result = new HashMap<Boolean, String>();
		result.put(true, "");
		if (userName != null && password != null) {
			if (userName.trim().equals("")) {
				result.clear();
				result.put(false, "请输入用户名！");
				return result;
			}
			if (password.trim().equals("")) {
				result.clear();
				result.put(false, "请输入密码！");
				return result;
			}
		}
		return result;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				|| keyCode == KeyEvent.KEYCODE_ENTER) {
			if (getCurrentFocus().getId() == R.id.bt_login) {
				findViewById(R.id.bt_login).performClick();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
