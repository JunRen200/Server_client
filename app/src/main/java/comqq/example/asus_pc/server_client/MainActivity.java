package comqq.example.asus_pc.server_client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Uri imgUri;
    private ImageView img;
    private RecyclerView recyclerView;
    private Button btn_post;
    private EditText edt_age;
    private EditText edt_name;
    private EditText edt_address;
    private RadioGroup edt_sex;
    private MyAdapter myAdapter;
    private Button btn_query;
    private Bitmap image ;
    private ArrayList<Person> arrayList=new ArrayList<Person>();
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==1){
                myAdapter.notifyDataSetChanged();
            }
            return true;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"9d1d3b1894af448f0cef004ed965c2d3");
       initConnection();
        setContentView(R.layout.activity_main);
        initConnection();
        initVIew();


    }

    private void initConnection() {
        BmobQuery<Person> query=new BmobQuery<Person>();
        query.setLimit(50);
        query.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> list, BmobException e) {
                arrayList.clear();
                if(e==null){
                    for(Person pp:list){
                        arrayList.add(pp);
                    }
                    Message msg=new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void initVIew() {
        img= (ImageView) findViewById(R.id.img);
        edt_age= (EditText) findViewById(R.id.edt_age);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_address = (EditText) findViewById(R.id.edt_address);
        btn_post = (Button) findViewById(R.id.bnt_post);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1004);
            }
        });
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person p1=new Person();
                p1.setAddress(edt_address.getText().toString());
                p1.setName(edt_name.getText().toString());
                p1.setAge(Integer.valueOf(edt_age.getText().toString()));

                //将bitmap转换成byte数组
                int size=image.getWidth()*image.getHeight()*4;
                ByteArrayOutputStream baos=new ByteArrayOutputStream(size);
                image.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] ima=baos.toByteArray();
                p1.add("img",ima);
             /*   BmobFile file=new BmobFile(new File(imgUri.toString()));
                    p1.setFile(file);*/

                p1.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {

                    }
                });
            }
        });
        btn_query= (Button) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initConnection();
            }
        });
        myAdapter=new MyAdapter(arrayList);
        LinearLayoutManager manager =new LinearLayoutManager(MainActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1004){
            imgUri = data.getData();
            //拿到图片后先裁剪
            Intent intent = new  Intent();
            intent.setAction("com.android.camera.action.CROP");
            intent.setDataAndType(imgUri, "image/*");
            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 1003);
        }
            if(requestCode==1003&&data!=null){
                Bundle bundle = data.getExtras();
                image = bundle.getParcelable("data");
                img.setImageBitmap(image);
            }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
