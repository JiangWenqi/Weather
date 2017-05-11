package cn.edu.bistu.cs.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.bistu.cs.databases.DBDao;
import cn.edu.bistu.cs.weather.R;

/**
 * Created by Vinci on 2017-5-4.
 */

public class LoginActivity extends Activity {

    private DBDao dao;
    private Intent intent;
    private EditText et_user, et_pass;
    private Button btn_login, btn_reg;
    private CheckBox rem_pw, auto_login;
    private String userName, userPass;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化控件
        initWidgets();
        //实例化对象
        sharedPreferences = getSharedPreferences("LoginPreference", Activity.MODE_PRIVATE);
        //判断记住密码多选框的状态
        if (sharedPreferences.getBoolean("remember_password", false)) {
            //设置默认是记录密码状态
            rem_pw.setChecked(true);
            et_user.setText(sharedPreferences.getString("username", ""));
            et_pass.setText(sharedPreferences.getString("password", ""));
            //判断自动登陆多选框状态
            if (sharedPreferences.getBoolean("auto_login", false)) {
                //设置默认是自动登录状态
                auto_login.setChecked(true);
                // 用户名
                userName = et_user.getText().toString();
                //跳转界面
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                intent.putExtra("username", userName);
                startActivity(intent);
                finish();
            }
        }
        /**
         * 登录按钮
         */
        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userName = et_user.getText().toString();
                userPass = et_pass.getText().toString();
                /** 判断输入框值是否为空 */
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userPass)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    // 实例化数据库操作类对象
                    dao = new DBDao(LoginActivity.this);
                    // 执行登录操作
                    int rows = dao.login(userName, userPass);
                    // 判断是否登录成功
                    if (rows > 0) {
                        //登陆成功之后跳转到欢迎页面
                        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                        //如果选择了记住密码
                        if (rem_pw.isChecked()) {
                            //用SharedPreference 存储最近一次的userName和userpass
                            sharedPreferences = getSharedPreferences("LoginPreference", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", userName);
                            editor.putString("password", userPass);
                            editor.commit();
                            //跳转到欢迎页面
                            intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                            intent.putExtra("username", userName);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /*
          注册按钮
         */
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //监听记住密码多选框按钮事件
        rem_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rem_pw.isChecked()) {
                    sharedPreferences.edit().putBoolean("remember_password", true).commit();
                } else {
                    sharedPreferences.edit().putBoolean("remember_password", false).commit();
                }
            }
        });

        //监听自动登录多选框事件
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_login.isChecked()) {
                    rem_pw.setChecked(true);
                    sharedPreferences.edit().putBoolean("auto_login", true).commit();
                    sharedPreferences.edit().putBoolean("remember_password", true).commit();
                } else {
                    sharedPreferences.edit().putBoolean("auto_login", false).commit();
                }
            }
        });

    }

    /**
     * @return void    返回类型
     * @Title: initWidgets
     * @Description: TODO(初始化控件)
     */
    private void initWidgets() {
        et_user = (EditText) findViewById(R.id.et_user);
        et_pass = (EditText) findViewById(R.id.et_pass);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_reg = (Button) findViewById(R.id.btn_register);
        rem_pw = (CheckBox) findViewById(R.id.login_remember_password);
        auto_login = (CheckBox) findViewById(R.id.login_auto);
    }
}
