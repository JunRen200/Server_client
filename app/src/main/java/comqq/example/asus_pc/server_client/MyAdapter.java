package comqq.example.asus_pc.server_client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by asus-pc on 2017/3/3.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {
    ArrayList<Person> list=new ArrayList<Person>();

    MyAdapter(ArrayList<Person> list){
        this.list=list;
    }
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =View.inflate(parent.getContext(),R.layout.item_student,null);
        Holder holder=new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        /*byte[] ima=list.get(position).getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(ima, 0, ima.length);
        if(bitmap!=null) {
            holder.img.setImageBitmap(bitmap);
        }*/
        holder.txt_name.setText(list.get(position).getName());
        holder.txt_address.setText(list.get(position).getAddress());
        holder.txt_age.setText(list.get(position).getAge()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView txt_address;
        private TextView txt_name;
        private TextView txt_age;
        private ImageView img;
        public Holder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.img);
            txt_address= (TextView) itemView.findViewById(R.id.txt_address);
            txt_name= (TextView) itemView.findViewById(R.id.txt_name);
            txt_age= (TextView) itemView.findViewById(R.id.txt_age);
        }
    }
}
