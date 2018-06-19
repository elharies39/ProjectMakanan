package com.appku.elharies.checkingfood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.appku.elharies.checkingfood.model.Food;

import java.util.ArrayList;

public class DetailFoodActivity extends AppCompatActivity {

    TextView tvDetailNamaMakanan, tvDetailTglKada;
    //TextView tvDetailSisa;
    Button btnDetailEdit, btnDetailHapus;
    ArrayList<Food> arrayFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);

        tvDetailNamaMakanan = findViewById(R.id.tv_detail_namaMakanan);
        tvDetailTglKada = findViewById(R.id.tv_detail_tglKad);
        btnDetailEdit = findViewById(R.id.btn_detail_editData);
        btnDetailHapus = findViewById(R.id.btn_detail_hapusData);

        boolean hasToples1Intent = getIntent().hasExtra(MainActivity.TOPLES1);
        Log.d("ALAMAT INTENT", ": "+hasToples1Intent);

        Food food = getIntent().getParcelableExtra(MainActivity.TOPLES1);
        tvDetailNamaMakanan.setText("Nama makanan: "+food.getNamaMakanan());
    }
}
