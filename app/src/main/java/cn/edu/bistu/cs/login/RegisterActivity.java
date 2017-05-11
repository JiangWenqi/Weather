package cn.edu.bistu.cs.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.bistu.cs.weather.R;
import cn.edu.bistu.cs.databases.DBDao;

/**
 * Created by Vinci on 2017-5-4.
 */

public class RegisterActivity extends Activity {

    private DBDao dao;
    private Intent mIntent;
    private EditText et_user;
    private EditText et_pass;
    private EditText et_repass;
    private Button btn_login;
    private Button btn_reg;
    private String userName;
    private String userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        // 初始化控件
        initWidgets();

        /**
         * 登录按钮
         */
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(mIntent);
                finish();
            }
        });

        /**
         * 注册按钮
         */
        btn_reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userName = et_user.getText().toString();
                userPass = et_pass.getText().toString();
                /** 判断输入框值是否为空 */
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userPass)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!userPass.equals(et_repass.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "两次密码不同", Toast.LENGTH_SHORT).show();
                } else {
                    // 实例化数据库操作类对象
                    dao = new DBDao(RegisterActivity.this);
                    // 执行注册操作
                    long rows = dao.register(userName, userPass);
                    // 判断是否注册成功
                    if (rows > 0) {
                        Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                        //注册成功之后跳转到登陆界面
                        mIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(mIntent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                    }
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
        et_repass = (EditText) findViewById(R.id.et_repass);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_reg = (Button) findViewById(R.id.btn_register);
    }

}
