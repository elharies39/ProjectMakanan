package com.appku.elharies.checkingfood;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TOPLES1 = "toples1";
    public static final String TOPLES2 = "toples2";
    public static final String TOPLES3 = "toples3";

    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormat;
    Button btnToples1, btnToples2, btnToples3;
    TextView tvKeteranganStatus, tvSambung;
    DatabaseReference databaseReference, dbChecking;
    boolean ada;
    String tglKadaluarsa = null;
    Food makanan;

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

        btnToples1.setOnClickListener(this);
        btnToples2.setOnClickListener(this);
        btnToples3.setOnClickListener(this);
        tvSambung.setOnClickListener(this);


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_toples1:
                boolean isAda = isExistToples(TOPLES1);
                Log.d("CEK ADA",": "+isAda);
                if (isAda) {
                    databaseReference.child(TOPLES1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Food food = dataSnapshot.getValue(Food.class);

                            Log.d("DATA FIREBASE",": "+food.getNamaMakanan());
                            Log.d("KEY UID DATA",": "+dataSnapshot.getKey());

                            /*f.setKey(dataSnapshot.getKey());
                            Intent intent = new Intent(MainActivity.this, DetailFoodActivity.class);
                            intent.putExtra(TOPLES1, (Parcelable) food);
                            startActivity(intent);*/
                            Toast.makeText(MainActivity.this, "Ada", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                        }
                    });
                } else {
                    makanan = new Food();

                    final Dialog dialog = new Dialog(this);
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
                            if (!isEmpty(etNamaMakanan.getText().toString()) && !isEmpty(tglKadaluarsa)){
                                dialog.dismiss();
                                submitDataToples(new Food(etNamaMakanan.getText().toString(), tglKadaluarsa),tvDialogTanggal, TOPLES1);
                            }else{
                                Toast.makeText(MainActivity.this, "Data tidak boleh kosong",Toast.LENGTH_LONG).show();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(tvDialogTanggal.getWindowToken(),0);
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
                break;
            case R.id.btn_toples2:

                break;
            case R.id.btn_toples3:

                break;
            case R.id.tv_keteranganStatus:

                break;
        }
    }

    public boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    public boolean isExistToples(String jenisToples) {
        dbChecking = FirebaseDatabase.getInstance().getReference();
        dbChecking.child(jenisToples)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ada = dataSnapshot.exists();
                        Log.d("Toples", "Ada isi: " + dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println(databaseError.getDetails() + " " + databaseError.getMessage());
                    }
                });
        return ada;
    }

    public void showDateDialog(final TextView tv){
       Calendar calendar = Calendar.getInstance();
       datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
           @Override
           public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(i,i1,i2);

                tv.setText(dateFormat.format(newDate.getTime()));
                Log.d("Tanggal", dateFormat.format(newDate.getTime()));

                //mendapatkan nilai tanggal sekarang
                /*String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        .format(new Date());
                Log.d("TANGGAL SEKARANG", ": "+tanggal);*/

                tglKadaluarsa = dateFormat.format(newDate.getTime());
           }
       }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));

       datePickerDialog.show();
    }

    public void submitDataToples(Food food, final TextView tv, String jenisToples){
        String keyFirebase = databaseReference.getRef().push().getKey();
        Log.d("KEY UID", ": "+keyFirebase);
        databaseReference.child(jenisToples)
                .push()
                .setValue(food)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tv.setText("");
                        tglKadaluarsa = null;
                        Toast.makeText(MainActivity.this, "Sukses", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
