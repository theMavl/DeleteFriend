package com.mavl.deletefriend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> friends;
    ArrayList<String> phones;
    static final private int RESPONSE = 0;
    static final private int MODE_VIEW = 1;
    static final private int MODE_ADD = 2;

    ListView listFriends;
    ArrayAdapter aFriends;

    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        friends = new ArrayList<>();
        phones = new ArrayList<>();
        friends.add(0, "Нулевой друг");
        phones.add(0, "+0000000000");
        listFriends = (ListView) findViewById(R.id.listFriends);
        aFriends = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, friends);
        listFriends.setAdapter(aFriends);

        listFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intent = new Intent(MainActivity.this, deepInfo.class);
                intent.putExtra("mode", MODE_VIEW);
                intent.putExtra("name", friends.get(pos));
                intent.putExtra("phone", phones.get(pos));
                intent.putExtra("ID", pos);
                startActivityForResult(intent, RESPONSE);
            }
        });

        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, deepInfo.class);
                intent.putExtra("mode", MODE_ADD);
                intent.putExtra("ID", friends.size());
                //friends.add(friends.size(), "");
                //phones.add(phones.size(), "");
                startActivityForResult(intent, RESPONSE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESPONSE) {
            if (resultCode == RESULT_OK) {
                String action = data.getStringExtra("ACTION");
                if (action.equals("DELETE")) {
                    int ID = data.getIntExtra("ID", -1);
                    try {
                        friends.remove(ID);
                        phones.remove(ID);
                    } catch (Exception e) {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
                if (action.equals("EDIT")) {
                    int ID = data.getIntExtra("ID", -1);
                    String name = data.getStringExtra("NAME");
                    String phone = data.getStringExtra("PHONE");
                    try {
                        friends.set(ID, name);
                        phones.set(ID, phone);
                    } catch (Exception e) {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
                if (action.equals("ADD")) {
                    int ID = data.getIntExtra("ID", -1);
                    String name = data.getStringExtra("NAME");
                    String phone = data.getStringExtra("PHONE");
                    try {
                        friends.add(ID, name);
                        phones.add(ID, phone);
                    } catch (Exception e) {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
                aFriends.notifyDataSetInvalidated();
            }

        }
    }
}
