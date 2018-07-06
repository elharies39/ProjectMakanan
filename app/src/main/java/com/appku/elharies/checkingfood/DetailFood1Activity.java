package com.appku.elharies.checkingfood;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appku.elharies.checkingfood.model.Food;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailFood1Activity extends AppCompatActivity {
    DatabaseReference databaseReference;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormat;
    TextView tvJudulnya, tvNamaMakanan, tvTanggal, tvSisanya;
    Button btnEdit, btnHapus, btnInputBaru;
    String tglKadaluarsa = null;
    String toples = null, tempToples1 = null, tempToples2 = null, tempToples3 = null;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food1);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        tempToples1 = getIntent().getStringExtra(MainActivity.TOPLES1);
        tempToples2 = getIntent().getStringExtra(MainActivity.TOPLES2);
        tempToples3 = getIntent().getStringExtra(MainActivity.TOPLES3);

        if (tempToples1 == null){
            System.out.println("Bukan 1");
        }

        if (tempToples2 == null){
            System.out.println("Bukan 2");
        }

        if (tempToples3 == null){
            System.out.println("Bukan 3");
        }

        if (tempToples1 != null){
            toples = tempToples1;
        }else if (tempToples2 != null){
            toples = tempToples2;
        }else if (tempToples3 != null){
            toples = tempToples3;
        }

        System.out.println("NOMOR BERAPA TOPLESNYA: "+toples);

        tvJudulnya = findViewById(R.id.tv_detail_toples);
        tvNamaMakanan = findViewById(R.id.tv_detail_namaMakanan);
        tvTanggal = findViewById(R.id.tv_detail_tglKad);
        tvSisanya = findViewById(R.id.tv_detail_sisaHari);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(toples).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("ADA DATA: " + dataSnapshot.exists());
                System.out.println("KEY UID: " + dataSnapshot.getKey());
                if (dataSnapshot.exists()) {
                    System.out.println(dataSnapshot.getValue());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Food food = snapshot.getValue(Food.class);
                        food.setKey(snapshot.getKey());
                        System.out.println(snapshot.getKey());
                        tvNamaMakanan.setText("Nama Makanan: " + food.getNamaMakanan());
                        tvTanggal.setText(food.getTglKadaluarsa());
                    }

                    editData();

                } else {
                    tvJudulnya.setText("Hahhhh! Kosong!!!");
                    tvNamaMakanan.setVisibility(View.GONE);
                    tvTanggal.setVisibility(View.GONE);
                    tvSisanya.setVisibility(View.GONE);

                    btnInputBaru = findViewById(R.id.btn_detail_inputData);
                    btnEdit = findViewById(R.id.btn_detail_editData);
                    btnHapus = findViewById(R.id.btn_detail_hapusData);

                    btnInputBaru.setVisibility(View.VISIBLE);
                    btnEdit.setVisibility(View.GONE);
                    btnHapus.setVisibility(View.GONE);

                    btnInputBaru.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(DetailFood1Activity.this);
                            dialog.setContentView(R.layout.dialog_new_toples1);
                            dialog.setTitle("Dialog");
                            dialog.show();

                            final TextView tvDialogTanggal = dialog.findViewById(R.id.tv_dialog_tanggal);
                            final EditText etNamaMakanan = dialog.findViewById(R.id.et_dl_namaMakanan);
                            Button btnDialogTanggal = dialog.findViewById(R.id.btn_dialog_tanggal);
                            Button btnDialogSimpan = dialog.findViewById(R.id.btn_dialog_simpan);
                            Button btnDialogBatal = dialog.findViewById(R.id.btn_dialog_batal);

                    /*makanan.setNamaMakanan(etNamaMakanan.getText().toString());
                            makanan.setTglKadaluarsa(tglKadaluarsa);*/

                            btnDialogTanggal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showDateDialog(tvDialogTanggal);
                                }
                            });

                            btnDialogSimpan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!isEmpty(etNamaMakanan.getText().toString()) && !isEmpty(tglKadaluarsa)) {
                                        dialog.dismiss();
                                        submitDataToples(new Food(etNamaMakanan.getText().toString(), tglKadaluarsa), tvDialogTanggal);
                                    } else {
                                        Toast.makeText(DetailFood1Activity.this, "Data tidak boleh kosong", Toast.LENGTH_LONG).show();
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(tvDialogTanggal.getWindowToken(), 0);
                                    }

                                }
                            });

                            btnDialogBatal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    public void editData() {
        btnEdit = findViewById(R.id.btn_detail_editData);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showDateDialog(final TextView tv) {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(i, i1, i2);

                tv.setText(dateFormat.format(newDate.getTime()));
                Log.d("Tanggal", dateFormat.format(newDate.getTime()));

                //mendapatkan nilai tanggal sekarang
                /*String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        .format(new Date());
                Log.d("TANGGAL SEKARANG", ": "+tanggal);*/

                tglKadaluarsa = dateFormat.format(newDate.getTime());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        datePickerDialog.show();
    }

    public void submitDataToples(Food food, final TextView tv) {
        String keyFirebase = databaseReference.getRef().push().getKey();
        databaseReference.child(toples)
                .push()
                .setValue(food)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tv.setText("");
                        tglKadaluarsa = null;
                        Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
