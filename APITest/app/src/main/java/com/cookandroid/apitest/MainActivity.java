package com.cookandroid.apitest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView status1;
    EditText pm_value;
    Toast toast;
    ViewGroup group;
    TextView msgTextView;

    boolean initem = false;
    boolean inDataTime = false;
    boolean inPm10Value = false;


    String dataTime = null;
    String pm10Value = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.enableDefaults();
        status1 = (TextView)findViewById(R.id.result);
        pm_value = (EditText)findViewById(R.id.pm_value);


        try {
            URL url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?serviceKey=4lgg5BF2Ki7ELuo4n1YMf8%2Foe6q6FR7gvTvnQk1Jz346%2BTqHv0fiVDqjUdvQDUvQ2OaS3MuEcx3glOJwVmjx7A%3D%3D&numOfRows=1&pageNo=1&stationName=%EC%A2%85%EB%A1%9C%EA%B5%AC&dataTerm=daily&ver=1.3");

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), "utf-8");

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작");
            Log.i("파싱", "파싱시작");

            while(parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("dataTime")) {
                            inDataTime = true;
                        }

                        if(parser.getName().equals("pm10Value")) {
                            inPm10Value = true;
                        }


                        if(parser.getName().equals("message")) {
                            status1.setText(status1.getText()+"에러");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if(inDataTime) {
                            dataTime = parser.getText();
                            inDataTime = false;
                        }

                        if(inPm10Value) {
                            pm10Value = parser.getText();
                            inPm10Value = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")) {
                            status1.setText(status1.getText() + "측정일 : " + dataTime +
                                    "\n미세먼지 농도 : " + pm10Value +"\n");
                            initem = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }


        } catch(Exception e){
            status1.setText(e.getMessage());
        }

        pm_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                if(pm_value.getText().toString().equals("")) {
                    //Toast.makeText(getApplicationContext(),"값을 입력해라", Toast.LENGTH_LONG).show();
                } else {
                    if(Integer.parseInt(pm10Value)<Integer.parseInt(pm_value.getText().toString())) {
                        toast = Toast.makeText(getApplicationContext(),"창문 열어",Toast.LENGTH_SHORT);
                        group = (ViewGroup)toast.getView();
                        msgTextView = (TextView)group.getChildAt(0);
                        msgTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 100);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });



    }
}
