package com.example.mybutterknife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apt_annotation.BindView;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.btn_click)
    Button btnClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // MainActivityViewBinding.bind(this);
        ButterKnife.bind(this);
        tvContent.setText("APT Success");
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
