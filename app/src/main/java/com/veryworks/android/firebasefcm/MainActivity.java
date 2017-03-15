package com.veryworks.android.firebasefcm;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference userRef;
    EditText editMsg,editId,editPw;
    TextView textToken;
    ListView listView;
    ListAdapter adapter;
    List<User> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 데이터베이스 연결
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");

        // 위젯
        editId = (EditText) findViewById(R.id.editId);
        editPw = (EditText) findViewById(R.id.editPw);
        editMsg = (EditText) findViewById(R.id.editMsg);
        textToken = (TextView) findViewById(R.id.textToken);

        // 사용자 리스트 세팅
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListAdapter(this, datas);
        listView.setAdapter(adapter);

        // 리스트를 클릭하면 사용자정보중에 token 값을 화면에 뿌린다
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = datas.get(position);
                textToken.setText(user.getToken());
            }
        });
    }

    public void sendNotification(View view){

        String msg = editMsg.getText().toString();
        if(!"".equals(msg)){ // 입력값이 있으면 노티를 날려준다

        }
    }

    public void signIn(View view){
        final String id = editId.getText().toString();
        final String pw = editPw.getText().toString();

        // DB 1. 파이어베이스로 child(id) 레퍼런스에 대한 쿼리를 날린다.
        userRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {

            // DB 2.파이어베이스는 데이터쿼리가 완료되면 스냅샷에 담아서 onDataChange 를 호출해준다
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    String fbPw = dataSnapshot.child("password").getValue().toString();

                    if(fbPw.equals(pw)){
                        addToken();
                        setList();
                    }else{
                        Toast.makeText(MainActivity.this, "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "User 가 없습니다", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addToken(){
        final String id = editId.getText().toString();
        userRef.child(id).child("token").setValue(getToken());
    }

    public void setList(){
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datas.clear();
                Log.i("data",dataSnapshot.toString());
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    user.setId(data.getKey());
                    datas.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public String getToken(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("Token===",token);
        return token;
    }
}


// 리스트 아답터
class ListAdapter extends BaseAdapter {
    Context context;
    List<User> datas;
    LayoutInflater inflater;

    public ListAdapter(Context context, List<User> datas){
        this.context = context;
        this.datas = datas;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.item_list, null);
        User user = datas.get(position);
        TextView userId = (TextView) convertView.findViewById(R.id.userId);
        userId.setText(user.getId());
        return convertView;
    }
}