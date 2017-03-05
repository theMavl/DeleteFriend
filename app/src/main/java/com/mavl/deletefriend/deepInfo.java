package com.mavl.deletefriend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class deepInfo extends AppCompatActivity {

    LinearLayout lyView;
    LinearLayout lyEdit;
    LinearLayout lyDef;
    LinearLayout lySure;
    TextView txtViewName;
    TextView txtViewPhone;
    TextView txtAction;
    EditText txtInputName;
    EditText txtInputPhone;
    Button btnEdit;
    Button btnDel;
    Button btnYes;
    Button btnNo;
    Button btnSave;
    int mode;
    int ID;
    boolean hasAction = false;

    Intent intent;
    Intent ansIntent;

    String name;
    String phone;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!hasAction) {
            setResult(RESULT_CANCELED);
        }
        else
            setResult(RESULT_OK, ansIntent);
        Log.d("deep", "Let's die");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_info);

        lyView = (LinearLayout) findViewById(R.id.lyView);
        lyEdit = (LinearLayout) findViewById(R.id.lyEdit);
        lyDef = (LinearLayout) findViewById(R.id.lyDef);
        lySure = (LinearLayout) findViewById(R.id.lySure);
        txtViewName = (TextView) findViewById(R.id.txtViewName);
        txtViewPhone = (TextView) findViewById(R.id.txtViewPhone);
        txtAction = (TextView) findViewById(R.id.txtAction);
        txtInputName = (EditText) findViewById(R.id.txtInputName);
        txtInputPhone = (EditText) findViewById(R.id.txtInputPhone);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnYes = (Button) findViewById(R.id.btnYes);
        btnNo = (Button) findViewById(R.id.btnNo);
        btnSave = (Button) findViewById(R.id.btnSave);

        intent = getIntent();
        mode = intent.getIntExtra("mode", 0);
        Log.d("deep", "Mode "+mode);
        ID = intent.getIntExtra("ID", -1);


        switch (mode) {
            case 1:
                name = intent.getStringExtra("name");
                phone = intent.getStringExtra("phone");
                prepView();
                printViewInfo();
                break;
            case 2:
                prepEdit();
                printEditInfo();
                break;
            default:
                Log.d("deep", "No such mode!");
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepEdit();
                printEditInfo();
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepSure();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepView();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ansIntent = new Intent();
                ansIntent.putExtra("ACTION", "DELETE");
                ansIntent.putExtra("ID", ID);
                setResult(RESULT_OK, ansIntent);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm;
                if (txtInputName.getText().toString().equals(name) && txtInputPhone.getText().toString().equals(phone)) {
                    printViewInfo();
                    prepView();
                    txtInputName.clearFocus();
                    txtInputPhone.clearFocus();
                    imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtInputName.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(txtInputPhone.getWindowToken(), 0);
                    return;
                }

                ansIntent = new Intent();
                name = txtInputName.getText().toString();
                phone = txtInputPhone.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Имя не может быть пустым", Toast.LENGTH_SHORT).show();
                    txtInputName.requestFocus();
                    return;
                } else if (phone.equals("")) {
                    Toast.makeText(getApplicationContext(), "Номер не может быть пустым", Toast.LENGTH_SHORT).show();
                    txtInputPhone.requestFocus();
                    return;
                }

                ansIntent.putExtra("ID", ID);
                ansIntent.putExtra("NAME", name);
                ansIntent.putExtra("PHONE", phone);

                if (mode == 1) {
                    ansIntent.putExtra("ACTION", "EDIT");
                    setResult(RESULT_OK, ansIntent);
                    printViewInfo();
                    prepView();
                    hasAction = true;
                    txtInputName.clearFocus();
                    txtInputPhone.clearFocus();
                    Toast.makeText(getApplicationContext(), "Изменено успешно", Toast.LENGTH_SHORT).show();
                    imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtInputName.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(txtInputPhone.getWindowToken(), 0);
                }
                else if (mode == 2) {
                    ansIntent.putExtra("ACTION", "ADD");
                    setResult(RESULT_OK, ansIntent);
                    Toast.makeText(getApplicationContext(), "Друг добавлен", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    void prepView() {
        lyEdit.setVisibility(View.GONE);
        lySure.setVisibility(View.GONE);
        lyDef.setVisibility(View.VISIBLE);
        lyView.setVisibility(View.VISIBLE);
    }

    void prepEdit() {
        if (mode == 1)
            txtAction.setText("Редактирование");
        else
            txtAction.setText("Новый друг");
        lyView.setVisibility(View.GONE);
        lyEdit.setVisibility(View.VISIBLE);
        txtInputName.requestFocus();
    }

    void prepSure() {
        lyDef.setVisibility(View.GONE);
        lySure.setVisibility(View.VISIBLE);
    }

    void printViewInfo() {
        txtViewName.setText(name);
        txtViewPhone.setText(phone);
    }

    void printEditInfo() {
        txtInputName.setText(name);
        txtInputPhone.setText(phone);
    }
}
