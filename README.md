# Weather
简易android天气预报（带登录注册）


SQLite

SharedPreference

intent

adapter

parseXML

etc
实验二  基于Android的天气预报应用程序
一、	实验目的：
1、通过本实验，进一步熟悉Android Studio的用法
2、通过本实验，掌握Android系统中适配器视图（ListView）的使用方法
3、通过本实验，掌握Android系统中数据持久化的方法
4、通过本实验，掌握Android系统中网络编程的基础知识
5、通过本实验，掌握Android系统中服务的使用
二、	实验内容及要求：
1.	题目一：设计并实现一个天气预报程序，具体要求如下【80分】
1)	实现用户注册和登录界面，用户注册信息存储在SQLite数据库中
A.	截图展示：
 
图 二 1登录页面
 
图 二 2注册页面
 
图 二 3欢迎页面
 
图 二 4数据库用户表
B.	相关代码展示：
 
图 二 5登陆页面代码片段
 
图 二 6注册按钮
 
图 二 7欢迎页面代码片段
2)	设计并实现界面，允许用户选择并添加关注的地理位置，地理位置以县级行政机构为单位，用户关注的地理位置存储在SQLite数据库中，用户关注的地理位置信息表，应该至少包含用户名、关注的地理位置的省、市、县名称、关注的地理位置的天气代号等数据
关于在线天气API的补充说明
(1)访问http://www.weather.com.cn/data/list3/city.xml 获取省份ID，获取到的数据格式如下：
01|北京,02|上海,03|天津,04|重庆,05|黑龙江,06|吉林,07|辽宁,08|内蒙古,09|河北,10|山西,11|陕西,12|山东,13|新疆,14|西藏,15|青海,16|甘肃,17|宁夏,18|河南,19|江苏,20|湖北,21|浙江,22|安徽,23|福建,24|江西,25|湖南,26|贵州,27|四川,28|广东,29|云南,30|广西,31|海南,32|香港,33|澳门,34|台湾
从数据中可以看出，北京的省份ID为01，山东的省份ID为12
(2)根据省份ID，获取该省内的城市列表，访问如下地址：
http://www.weather.com.cn/data/list3/city[省份ID].xml
如查询山东省内的城市列表，可以访问如下地址：
http://www.weather.com.cn/data/list3/city12.xml，返回结果格式如下：
1201|济南,1202|青岛,1203|淄博,1204|德州,1205|烟台,1206|潍坊,1207|济宁,1208|泰安,1209|临沂,1210|菏泽,1211|滨州,1212|东营,1213|威海,1214|枣庄,1215|日照,1216|莱芜,1217|聊城
(3)根据城市ID，获取该城市下辖的县级行政单位的ID，可以访问如下地址：
http://www.weather.com.cn/data/list3/city[城市ID].xml
如查询威海市下辖县级行政机构的ID，可以访问如下地址：
http://www.weather.com.cn/data/list3/city1213.xml，返回结果格式如下：
121301|威海,121302|文登,121303|荣成,121304|乳山,121305|成山头,121306|石岛
(4)根据获得的县级行政机构ID，获取对应的天气代号，如查询成山头的天气，可以访问如下地址：
http://www.weather.com.cn/data/list3/city121305.xml，返回结果格式如下：
121305|101121305
其中的101121305，即为成山头对应的天气代号，访问如下地址，即可获取成山头的天气信息：
http://www.weather.com.cn/data/cityinfo/101121305.html，返回格式为JSON，UTF-8编码
{"weatherinfo":{"city":"成山头","cityid":"101121305","temp1":"1℃","temp2":"8℃","weather":"晴","img1":"n0.gif","img2":"d0.gif","ptime":"18:00"}}
其中的temp1是最低气温，temp2是最高气温，ptime表示天气数据发布的时间，img1和img2分别对应白天和夜间的天气图片，如n0.gif，对应的URL为：
http://m.weather.com.cn/img/n0.gif 

A.	截图展示：
 
图 二 8天气界面
B.	相关代码展示：
a)	天气页布局文件：
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <!--顶部工具栏-->
    <RelativeLayout
        android:id="@+id/title"
        android:layout_width="fill_parent"
        android:layout_height="45.0dip"
        android:background="#ffcd2626"
        android:gravity="center_vertical">

        <ImageView
            android:id="@+id/title_menu"
            android:layout_width="45.0dip"
            android:layout_height="45.0dip"
            android:src="@drawable/title_city" />

        <ImageView
            android:id="@+id/city_seperator"
            android:layout_width="1.0dip"
            android:layout_height="40dip"
            android:layout_marginTop="2.0dip"
            android:layout_toRightOf="@id/title_menu"
            android:background="#A71717" />

        <TextView
            android:id="@+id/title_city_name"
            android:layout_width="fill_parent"
            android:layout_height="fill_parent"
            android:layout_toRightOf="@id/city_seperator"
            android:gravity="center_vertical"
            android:paddingLeft="5dip"
            android:text="北京天气"
            android:textColor="#ffffffff"
            android:textSize="22.0sp" />


        <ImageView
            android:id="@+id/title_update_btn"
            android:layout_width="45.0dip"
            android:layout_height="45.0dip"
            android:layout_alignParentRight="true"
            android:layout_gravity="center"
            android:src="@drawable/title_update"
            android:visibility="visible" />


        <ImageView
            android:id="@+id/title_share"
            android:layout_width="45.0dip"
            android:layout_height="45.0dip"
            android:layout_toLeftOf="@id/title_update_btn"
            android:src="@drawable/title_share" />

        <ImageView
            android:id="@+id/title_location"
            android:layout_width="45.0dip"
            android:layout_height="45.0dip"
            android:layout_toLeftOf="@id/title_share"
            android:src="@drawable/base_action_bar_action_city" />
    </RelativeLayout>

    <!-- 今日天气信息 -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@id/title"
        android:background="@drawable/biz_plugin_weather_shenzhen_bg"
        android:orientation="vertical">

        <RelativeLayout
            android:id="@+id/weather_today"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <!-- 城市名称 -->

            <TextView
                android:id="@+id/city"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentLeft="true"
                android:layout_alignParentTop="true"
                android:layout_marginLeft="15.0dip"
                android:singleLine="true"
                android:text="北京"
                android:textColor="@android:color/white"
                android:textSize="40.0sp" />
            <!-- 发布时间 -->
            <TextView
                android:id="@+id/time"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentLeft="true"
                android:layout_below="@id/city"
                android:layout_marginLeft="15.0dip"
                android:layout_marginTop="5dip"
                android:singleLine="true"
                android:text="今天18:25发布"
                android:textColor="@android:color/white"
                android:textSize="15.0sp" />
            <!-- 湿度信息 -->
            <TextView
                android:id="@+id/humidity"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentLeft="true"
                android:layout_below="@id/time"
                android:layout_marginLeft="15.0dip"
                android:singleLine="true"
                android:text="湿度:57%"
                android:textColor="@android:color/white"
                android:textSize="15.0sp" />

            <!-- PM2.5整体信息块 -->
            <LinearLayout
                android:id="@+id/pm2_5_content"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentRight="true"
                android:layout_alignParentTop="true"
                android:orientation="vertical">

                <!-- PM2.5详情 -->

                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:paddingBottom="6.0dip"
                    android:paddingLeft="12.0dip"
                    android:paddingRight="12.0dip"
                    android:paddingTop="6.0dip">
                    <!-- PM2.5文字详情 -->
                    <LinearLayout
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:orientation="vertical">
                        <!-- PM2.5 -->
                        <TextView
                            android:id="@+id/pm2_5"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:maxLines="1"
                            android:text="PM2.5"
                            android:textColor="@android:color/white"
                            android:textSize="12.0sp" />
                        <!-- PM2.5数值 -->
                        <TextView
                            android:id="@+id/pm_data"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:maxLines="1"
                            android:text="220"
                            android:textColor="@android:color/white"
                            android:textSize="30.0sp" />
                    </LinearLayout>
                    <!-- PM2.5图片 -->
                    <ImageView
                        android:id="@+id/pm2_5_img"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_marginLeft="5.0dip"
                        android:src="@drawable/biz_plugin_weather_0_50" />
                </LinearLayout>
                <!-- 污染等级(重度污染) -->
                <TextView
                    android:id="@+id/pm2_5_quality"
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:singleLine="true"
                    android:text="重度污染"
                    android:textColor="@android:color/white"
                    android:textSize="20.0sp" />

            </LinearLayout>

            <LinearLayout
                android:id="@+id/today_weather_info"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_alignParentLeft="true"
                android:layout_below="@id/humidity"
                android:layout_centerInParent="true"
                android:layout_marginTop="5.0dip"
                android:orientation="horizontal">

                <!-- 天气情况图片示例 -->
                <ImageView
                    android:id="@+id/weather_img"
                    android:layout_width="188dp"
                    android:layout_height="122dp"
                    android:layout_alignParentLeft="true"
                    android:src="@drawable/biz_plugin_weather_qing" />

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:orientation="vertical">
                    <!-- 今日星期 -->
                    <TextView
                        android:id="@+id/week_today"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_alignParentTop="true"
                        android:layout_marginLeft="5dip"
                        android:layout_toRightOf="@id/weather_img"
                        android:maxLines="1"
                        android:text="今天 星期三"
                        android:textColor="@android:color/white"
                        android:textSize="20sp" />
                    <!-- 温度范围 -->
                    <TextView
                        android:id="@+id/temperature"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_below="@id/week_today"
                        android:layout_marginLeft="5dip"
                        android:layout_toRightOf="@id/weather_img"
                        android:maxLines="1"
                        android:text="-2℃~7℃"
                        android:textColor="@android:color/white"
                        android:textSize="20sp" />
                    <!-- 天气状况 -->
                    <TextView
                        android:id="@+id/climate"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_below="@id/temperature"
                        android:layout_marginLeft="5dip"
                        android:layout_toRightOf="@id/weather_img"
                        android:maxLines="1"
                        android:text="多云转晴"
                        android:textColor="@android:color/white"
                        android:textSize="20.0sp" />
                    <!-- 风力信息 -->
                    <TextView
                        android:id="@+id/wind"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_below="@id/climate"
                        android:layout_marginLeft="5dip"
                        android:layout_toRightOf="@id/weather_img"
                        android:maxLines="1"
                        android:text="微风"
                        android:textColor="@android:color/white"
                        android:textSize="20.0sp" />

                </LinearLayout>

            </LinearLayout>
        </RelativeLayout>

    </LinearLayout>
</RelativeLayout>

b)	数据获取
 
图 二 9从网络上获取数据
c)	对天气进行解析：
/**
     * 对天气XML进行解析
     */

    private TodayWeather parseXML(String xmlData) {
        TodayWeather todayWeather = null;

        int fengliCount = 0;
        int fengxiangCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            Log.d(TAG, "start parse xml");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //文档开始位置
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("parse", "start doc");
                        break;
                    //标签元素开始位置
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                Log.d("city", xmlPullParser.getText());
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                Log.d("updatetime", xmlPullParser.getText());
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                Log.d("wendu", xmlPullParser.getText());
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("fengli", xmlPullParser.getText());
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                Log.d("shidu", xmlPullParser.getText());
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("fengxiang", xmlPullParser.getText());
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                Log.d("pm25", xmlPullParser.getText());
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                Log.d("quelity", xmlPullParser.getText());
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("date", xmlPullParser.getText());
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("high", xmlPullParser.getText());
                                todayWeather.setHigh(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("low", xmlPullParser.getText());
                                todayWeather.setLow(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("type", xmlPullParser.getText());
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

d)	数据填充（更新首页）
    void updateTodayWeather(TodayWeather todayWeather) {
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度：" + todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:" + todayWeather.getFengli());
        if (todayWeather.getPm25() != null) {
            int pm25 = Integer.parseInt(todayWeather.getPm25());
            if (pm25 <= 50) {
                PM25Img.setImageResource(R.drawable.biz_plugin_weather_0_50);
            } else if (pm25 >= 51 && pm25 <= 100) {
                PM25Img.setImageResource(R.drawable.biz_plugin_weather_51_100);
            } else if (pm25 >= 101 && pm25 <= 150) {
                PM25Img.setImageResource(R.drawable.biz_plugin_weather_101_150);
            } else if (pm25 >= 151 && pm25 <= 200) {
                PM25Img.setImageResource(R.drawable.biz_plugin_weather_151_200);
            } else if (pm25 >= 201 && pm25 <= 300) {
                PM25Img.setImageResource(R.drawable.biz_plugin_weather_201_300);
            }
        }
        if (todayWeather.getType() != null) {
            Log.d("type", todayWeather.getType());
            switch (todayWeather.getType()) {
                case "晴":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
                    break;
                case "阴":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
                    break;
                case "雾":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
                    break;
                case "多云":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
                    break;
                case "小雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
                    break;
                case "中雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
                    break;
                case "大雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
                    break;
                case "阵雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
                    break;
                case "雷阵雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                    break;
                case "雷阵雨加暴":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
                    break;
                case "暴雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
                    break;
                case "大暴雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
                    break;
                case "特大暴雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
                    break;
                case "阵雪":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
                    break;
                case "暴雪":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
                    break;
                case "大雪":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
                    break;
                case "小雪":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
                    break;
                case "雨夹雪":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
                    break;
                case "中雪":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
                    break;
                case "沙尘暴":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
                    break;
                default:
                    break;
            }
        }
        if (todayWeather.getType() != null) {
            Log.d("type", todayWeather.getType());
            switch (todayWeather.getType()) {
                case "晴":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
                    break;
                case "阴":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
                    break;
                case "雾":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
                    break;
                case "多云":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
                    break;
                case "小雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
                    break;
                case "中雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
                    break;
                case "大雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
                    break;
                case "阵雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
                    break;
                case "雷阵雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                    break;
                case "雷阵雨加暴":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
                    break;
                case "暴雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
                    break;
                case "大暴雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
                    break;
                case "特大暴雨":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
                    break;
                case "阵雪":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
                    break;
                case "暴雪":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
                    break;
                case "大雪":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
                    break;
                case "小雪":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
                    break;
                case "雨夹雪":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
                    break;
                case "中雪":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
                    break;
                case "沙尘暴":
                    weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
                    break;
                default:
                    break;
            }
        }
        Toast.makeText(WeatherActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();
    }
3)	以ListView的形式显示用户关注的地理位置列表，点击某个地理位置，可以进入新的页面显示该地理位置的天气信息，至少需要显示天气描述(“晴”、“多云”等)、最高气温、最低气温、发布时间等。显示的天气数据需要使用服务在后台获取。
A.	截图展示： 

 
图 二 10选择城市界面
 
图 二 11用户关注城市页面
B.	相关代码展示：
 
图 二 12城市列表布局文件
 
图 二 13城市列表加载


2.	题目二【20分】：
1）在登录界面中，增加“记住密码”复选框，记住的用户密码存储在SharedPreferences中，若用户取消了“记住密码”复选框的选择，需要从SharedPreferences中清除记录的用户名和密码
2）在SQLite数据库中增加一个表，缓存读取到的天气信息，在天气信息显示界面中，如果后台刷新天气信息的服务尚未返回数据，则显示缓存的天气信息。这个表的结构应包含天气API的补充说明中JSON的各字段。
A.	截图展示：
 
图 二 14记住密码
B.	相关代码展示：
 
图 二 15自动登陆代码片段
 
图 二 16自动登陆代码片段（二）

三、	实验总结：
此次试验写的不是很顺利，很多地方都不太懂，看了很多博客和相关的文章才磕磕碰碰把这次实验做完，但是程序还有很多不足的地方：
	密码没有加密，sharedPreferences的值明文传送；
	城市列表里的城市还是过去的数据，有些城市都取消了；
	搜索框动态搜索显示还没做出来；
	很多逻辑衔接的不那么自然；
	数据库表的建立也比较仓促等问题。
不过这次实验的收获还是很多，尤其是熟悉了Android Studio这个大工具，里面对代码要求非常规范，尤其我又受不了warning，所以对语法又进行了一些复习。还有就是利用log文件进行调试。
再就是熟悉了布局文件，知道了写一个好的android应用是相当费时间，
	还有很多地方需要改进。

具体代码以附件打包传送
