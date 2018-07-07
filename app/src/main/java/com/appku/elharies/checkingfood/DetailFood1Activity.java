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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DetailFood1Activity extends AppCompatActivity {
    DatabaseReference databaseReference;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormat;
    TextView tvJudulnya, tvNamaMakanan, tvTanggal, tvSisanya;
    Button btnEdit, btnHapus, btnInputBaru;
    public long sisaHariKadaluarsa = 0;
    public String tglKadaluarsa = null;
    public String kuncinya = null;
    public String nama = null, tanggal = null;
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

        if (tempToples1 == null) {
            System.out.println("Bukan 1");
        }

        if (tempToples2 == null) {
            System.out.println("Bukan 2");
        }

        if (tempToples3 == null) {
            System.out.println("Bukan 3");
        }

        if (tempToples1 != null) {
            toples = tempToples1;
        } else if (tempToples2 != null) {
            toples = tempToples2;
        } else if (tempToples3 != null) {
            toples = tempToples3;
        }

        System.out.println("NOMOR BERAPA TOPLESNYA: " + toples);

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

                    if (toples.equalsIgnoreCase(MainActivity.TOPLES1)) {
                        tvJudulnya.setText(R.string.toples_1);
                    } else if (toples.equalsIgnoreCase(MainActivity.TOPLES2)) {
                        tvJudulnya.setText(R.string.toples_2);
                    } else if (toples.equalsIgnoreCase(MainActivity.TOPLES3)) {
                        tvJudulnya.setText(R.string.toples_3);
                    }

                    System.out.println(dataSnapshot.getValue());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Food food = snapshot.getValue(Food.class);
                        food.setKey(snapshot.getKey());
                        System.out.println(snapshot.getKey());
                        tvNamaMakanan.setText("Nama Makanan: " + food.getNamaMakanan());
                        tvTanggal.setText(food.getTglKadaluarsa());
                        nama = food.getNamaMakanan();
                        tanggal = food.getTglKadaluarsa();
                        System.out.println("KEYNYA: " + food.getKey());
                        kuncinya = food.getKey();
                    }
                    try {
                        sisaHariKadaluarsa = hitungSisaHari(tanggal);
                        System.out.println("SISA HARI: "+sisaHariKadaluarsa);

                        tvSisanya.setText(sisaHariKadaluarsa+" hari lagi");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    editData();
                    deleteDataToples(toples);

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

        btnInputBaru.setVisibility(View.GONE);
        tvNamaMakanan.setVisibility(View.VISIBLE);
        tvTanggal.setVisibility(View.VISIBLE);
        tvSisanya.setVisibility(View.VISIBLE);

        btnEdit.setVisibility(View.VISIBLE);
        btnHapus.setVisibility(View.VISIBLE);

        databaseReference.child(toples)
                .push()
                .setValue(food)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tv.setText("");
                        tglKadaluarsa = null;
                        databaseReference.child(toples).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Food food = snapshot.getValue(Food.class);
                                    food.setKey(snapshot.getKey());
                                    System.out.println(snapshot.getKey());
                                    tvNamaMakanan.setText("Nama Makanan: " + food.getNamaMakanan());
                                    tvTanggal.setText(food.getTglKadaluarsa());
                                }
                                editData();
                                deleteDataToples(toples);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void deleteDataToples(final String toplesBerapa) {
        btnHapus = findViewById(R.id.btn_detail_hapusData);
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (databaseReference != null) {
                    databaseReference.child(toplesBerapa).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DetailFood1Activity.this, "Berhasil Dihapus", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    public void editData() {
        btnEdit = findViewById(R.id.btn_detail_editData);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (databaseReference != null) {
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

                    etNamaMakanan.setText(nama);
                    tvDialogTanggal.setText(tanggal);
                    tglKadaluarsa = tanggal;

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
                                submitDataAfterEdit(new Food(etNamaMakanan.getText().toString(), tglKadaluarsa), tvDialogTanggal);
                                dialog.cancel();
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
            }
        });
    }

    public void submitDataAfterEdit(Food food, final TextView tv) {
        databaseReference.child(toples)
                .child(kuncinya)
                .setValue(food)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tv.setText("");
                        tglKadaluarsa = null;
                        databaseReference.child(toples).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Food food = snapshot.getValue(Food.class);
                                    food.setKey(snapshot.getKey());
                                    System.out.println(snapshot.getKey());
                                    tvNamaMakanan.setText("Nama Makanan: " + food.getNamaMakanan());
                                    tvTanggal.setText(food.getTglKadaluarsa());
                                }
                                editData();
                                deleteDataToples(toples);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_LONG).show();
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
}
