package cn.edu.bistu.cs.menu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.bistu.cs.app.MyApplication;
import cn.edu.bistu.cs.bean.City;
import cn.edu.bistu.cs.databases.DBDao;
import cn.edu.bistu.cs.login.LoginActivity;
import cn.edu.bistu.cs.weather.R;
import cn.edu.bistu.cs.weather.WeatherActivity;

/**
 * Created by Vinci on 2017-5-11.
 */

public class MenuAcitvity extends Activity implements View.OnClickListener {


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载主界面的布局
        setContentView(R.layout.activity_menu);
        //加载返回按钮布局，并添加监听
        ImageView mBackBtn = (ImageView) findViewById(R.id.like_city_title_back);
        mBackBtn.setOnClickListener(this);
        //加载选择城市按钮布局，并设置监听
        Button addCityBtn = (Button) findViewById(R.id.btn_add_city);
        addCityBtn.setOnClickListener(this);
        //加载退出登陆按钮布局，并设置监听
        Button loginOutBtn = (Button) findViewById(R.id.btn_login_out);
        loginOutBtn.setOnClickListener(this);

        //获取SharedPreference里存储的username
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPreference", Activity.MODE_PRIVATE);
        String userName = sharedPreferences.getString("username", "");

        // 查询likeCity
        DBDao db = new DBDao(MenuAcitvity.this);
        //用于存储likecity里面的cityCode
        final ArrayList<String> likeCityCodes = db.findLikeCity(userName);

        // 获取数据库中城市信息并添加到mCityList中
        MyApplication mApplication = (MyApplication) this.getApplication();
        List<City> mCityList = mApplication.getCityList();

        // mArrayList用于存储likeCity城市名
        ArrayList<String> mArrayList = new ArrayList<>();

        for (int i = 0; i < likeCityCodes.size(); i++) {
            for (int j = 0; j < mCityList.size(); j++) {
                if (mCityList.get(j).getNumber().equals(likeCityCodes.get(i))) {
                    String likeCity = mCityList.get(j).getProvince() + "：" + mCityList.get(j).getCity();
                    mArrayList.add(likeCity);
                }
            }
        }
        // 添加适配器，加载城市名，形成列表
        ListView likeCityListLv = (ListView) findViewById(R.id.like_city_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MenuAcitvity.this, android.R.layout.simple_list_item_1, mArrayList);
        likeCityListLv.setAdapter(adapter);

        //为列表项目设置监听器
        likeCityListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String lickCityCode = likeCityCodes.get(position);
                //用SharedPreference 存储最近一次的
                SharedPreferences sharedPreferences = getSharedPreferences("CityCodePreference", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cityCode", lickCityCode);
                editor.commit();

                //在主活动中再通过所选择的城市查询天气
                Intent intent = new Intent(MenuAcitvity.this, WeatherActivity.class);
                Log.d("likeCityCode", lickCityCode);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.like_city_title_back:
                finish();
                break;
            case R.id.btn_add_city:
                //跳转到选择城市页面
                intent = new Intent(this, SelectCityActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_login_out:
                //清除"LoginPreference"配置文件的记住密码和自动登录

                SharedPreferences sharedPreferences = getSharedPreferences("LoginPreference", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("remember_password", false);
                editor.putBoolean("auto_login", false);
                editor.commit();
                //然后跳转到登录页面
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
