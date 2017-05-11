package cn.edu.bistu.cs.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.bistu.cs.weather.R;
import cn.edu.bistu.cs.weather.WeatherActivity;

/**
 * Created by Vinci on 2017-5-4.
 */

public class WelcomeActivity extends Activity {

    private TextView tv_notice;          // 提示
    private String userName;         // 登录成功的用户名
    private long exitTime;               // 退出时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        //获取SharedPreference里存储的username
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPreference", Activity.MODE_PRIVATE);
        userName = sharedPreferences.getString("username", "");
        //如果目前没有SharedPreference里没有username传过来，就从Intent里获取
        if (userName.equals("")) {
            //从Intent获取username
            userName = this.getIntent().getExtras().getString("username");
            if (userName.equals("")) {
                //如果Intent里面也没有用户名的话，就返回登陆页面;
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                //如果Intent有uername的话就把uername加载到SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", userName);
                editor.commit();

                 /* 获取当前小时，进行相应提示 */
                setNotice();
                //设置定时器自动跳转
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(WelcomeActivity.this, WeatherActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 888);
            }
        } else {//如果目前没有SharedPreference里有username传过来，就直接显示提示信息
        /* 获取当前小时，进行相应提示 */
            setNotice();
            //设置定时器自动跳转
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, WeatherActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 888);
        }
    }


    /**
     * return void    返回类型
     * Title: setNotice
     * Description: TODO(获取当前小时，进行相应提示)
     */
    private void setNotice() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        Log.d("date", "hour:" + hour);
        if (hour > 0 && hour < 7) {
            tv_notice.setText(userName + "，早上好，欢迎登录！");
        } else if (hour >= 7 && hour < 12) {
            tv_notice.setText(userName + "，上午好，欢迎登录！");
        } else if (hour >= 12 && hour < 13) {
            tv_notice.setText(userName + "，中午好，欢迎登录！");
        } else if (hour >= 13 && hour < 19) {
            tv_notice.setText(userName + "，下午好，欢迎登录！");
        } else {
            tv_notice.setText(userName + "，晚上好，欢迎登录！");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

