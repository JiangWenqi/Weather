package cn.edu.bistu.cs.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.bistu.cs.app.MyApplication;
import cn.edu.bistu.cs.bean.City;

/**
 * Created by Vinci on 2017-4-13.
 */

public class SelectCityActivity extends Activity implements View.OnClickListener {
    EditText edit_search;

    private String updateCityCode = "-1";
    private List<City> mCityList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_select_city);
        //加载返回按钮布局，并添加监听
        ImageView mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        //加载搜索框和搜索按钮,并设置文本框变化监听
        edit_search = (EditText) findViewById(R.id.search_edit);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCityList = MyApplication.getInstance().getCityList();
                for (int i = 0; i < mCityList.size(); i++) {

                }
                Log.d("ChangedListener", s.toString());
            }

            //输入有变化调用该方法
            @Override
            public void afterTextChanged(Editable s) {
                //设置查询数据库并显示在列表的操作

                Log.d("Change", s.toString());
            }
        });

        // 获取城市名列表
        ListView cityListLv = (ListView) findViewById(R.id.list_city);
        MyApplication mApplication = (MyApplication) this.getApplication();
        mCityList = mApplication.getCityList();
        ArrayList<String> mArrayList = new ArrayList<>();
        for (int i = 0; i < mCityList.size(); i++) {
            String cityName = mCityList.get(i).getCity();
            mArrayList.add(cityName);
        }
        // 添加适配器，加载城市名，形成列表
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectCityActivity.this, android.R.layout.simple_list_item_1, mArrayList);
        cityListLv.setAdapter(adapter);

        //为列表项目设置监听器
        cityListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String updateCityCode = mCityList.get(position).getNumber();

                //用SharedPreference 存储最近一次的
                SharedPreferences sharedPreferences = getSharedPreferences("CityCodePreference", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cityCode", updateCityCode);
                editor.commit();

                //在主活动中再通过所选择的城市查询天气
                Intent intent = new Intent(SelectCityActivity.this, WeatherActivity.class);
                Log.d("cityCode", updateCityCode);
                startActivity(intent);
                finish();
            }
        });
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            default:
                break;
        }
    }
}

