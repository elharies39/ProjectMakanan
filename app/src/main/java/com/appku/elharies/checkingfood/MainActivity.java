package com.appku.elharies.checkingfood;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appku.elharies.checkingfood.model.Food;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TOPLES1 = "toples1";
    public static final String TOPLES2 = "toples2";
    public static final String TOPLES3 = "toples3";

    SimpleDateFormat dateFormat;
    Button btnToples1, btnToples2, btnToples3;
    TextView tvKeteranganStatus, tvSambung;
    DatabaseReference databaseReference;
    public String tglKadaluarsa = null, tgl2 = null, tgl3 = null;
    public String namaMakanan = null, nama2 = null, nama3 = null;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        btnToples1 = findViewById(R.id.btn_toples1);
        btnToples2 = findViewById(R.id.btn_toples2);
        btnToples3 = findViewById(R.id.btn_toples3);

        tvKeteranganStatus = findViewById(R.id.tv_statusKoneksi);
        tvSambung = findViewById(R.id.tv_keteranganStatus);

        showNotification(TOPLES1);
        showNotification(TOPLES2);
        showNotification(TOPLES3);

        btnToples1.setOnClickListener(this);
        btnToples2.setOnClickListener(this);
        btnToples3.setOnClickListener(this);
        tvSambung.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_toples1:
                pindahHalamanToples(TOPLES1);
                break;
            case R.id.btn_toples2:
                pindahHalamanToples(TOPLES2);
                break;
            case R.id.btn_toples3:
                pindahHalamanToples(TOPLES3);
                break;
            case R.id.tv_keteranganStatus:

                break;
        }
    }

    public void pindahHalamanToples(String jenisToples) {
        Intent intent = new Intent(MainActivity.this, DetailFood1Activity.class);
        intent.putExtra(jenisToples, jenisToples);
        startActivity(intent);
    }

    public void showNotification(final String jenisToples) {
        databaseReference.child(jenisToples).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Food food = snapshot.getValue(Food.class);

                        if (jenisToples.equalsIgnoreCase("toples1")) {
                            tglKadaluarsa = food.getTglKadaluarsa();
                            namaMakanan = food.getNamaMakanan();
                        }else if (jenisToples.equalsIgnoreCase("toples2")){
                            tgl2 = food.getTglKadaluarsa();
                            nama2 = food.getNamaMakanan();
                        }else if (jenisToples.equalsIgnoreCase("toples3")){
                            tgl3 = food.getTglKadaluarsa();
                            nama3 = food.getNamaMakanan();
                        }else{
                            System.out.println("TIDAK ADA TOPLES");
                        }

                    }

                    try {
                        String tgl = null;
                        String mkn = null;
                        if (jenisToples.equalsIgnoreCase("toples1")){
                            tgl = tglKadaluarsa;
                            mkn = namaMakanan;
                            tglKadaluarsa = null;
                            namaMakanan = null;
                        }else if (jenisToples.equalsIgnoreCase("toples2")){
                            tgl = tgl2;
                            mkn = nama2;
                            tgl2 = null;
                            nama2 = null;
                        }else if (jenisToples.equalsIgnoreCase("toples3")){
                            tgl = tgl3;
                            mkn = nama3;
                            tgl3 = null;
                            nama3 = null;
                        }else{
                            System.out.println("TIDAK ADA TOPLES");
                        }

                        long sisaKadaluarsa = hitungSisaHari(tgl);
                        if (sisaKadaluarsa == 3) {
                            getNotif(jenisToples, mkn, "Masa Kadaluarsa Kurang 3 Hari Lagi"
                                    , R.drawable.ic_warning_lime_800_24dp);
                        } else if (sisaKadaluarsa == 0) {
                            getNotif(jenisToples, mkn, "Telah Kadaluarsa",
                                    R.drawable.ic_sentiment_very_dissatisfied_red_500_24dp);
                            hapusDataMakanan(jenisToples);
                        } else {

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public long hitungSisaHari(String tanggalK) throws ParseException {
        String tanggalSekarang = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .format(new Date());
        Date tglKadaluarsa = dateFormat.parse(tanggalK);
        Date tglSekarang = dateFormat.parse(tanggalSekarang);

        //long sisaHari = Math.abs(tglKadaluarsa.getTime() - tglSekarang.getTime());
        return TimeUnit.MILLISECONDS.toDays(Math.abs(tglKadaluarsa.getTime() - tglSekarang.getTime()));
    }

    public void getNotif(String namaToples, String makanan, String isiPemberitahuan, int ikon) {
        NotificationCompat.Builder notification = new NotificationCompat
                .Builder(this)
                .setSmallIcon(ikon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        ikon))
                .setContentTitle(namaToples)
                .setAutoCancel(true)
                .setContentText(makanan + " " + isiPemberitahuan);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat
                .from(this);
        notificationManagerCompat.notify(1, notification.build());
    }

    public void hapusDataMakanan(final String jenisToples) {
        if (databaseReference != null) {
            databaseReference.child(jenisToples).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MainActivity.this, jenisToples + " Terhapus", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
