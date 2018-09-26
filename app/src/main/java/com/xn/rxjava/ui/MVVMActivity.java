package com.xn.rxjava.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xn.rxjava.api.RetrofitManager;
import com.xn.rxjava.entity.Item;
import com.xn.rxjava.entity.ItemList;
import com.xn.rxjava.entity.Student;
import com.xn.rxjava.viewmodel.RealmServiceModel;
import com.xn.rxjava.viewmodel.RealmServiceModelFactory;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MVVMActivity extends AppCompatActivity implements OnClickListener {

    private TextView mask;
    private LinearLayout container;

    RealmServiceModel mRealmServiceModel;

    @Inject
    RealmServiceModelFactory mRealmServiceModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vvm);

        mRealmServiceModel = ViewModelProviders.of(this,mRealmServiceModelFactory).get(RealmServiceModel.class);

        this.mask = this.findViewById(R.id.mask);
        this.container = this.findViewById(R.id.container);

        this.findViewById(R.id.query).setOnClickListener(this);
        this.findViewById(R.id.add).setOnClickListener(this);
        this.findViewById(R.id.clear).setOnClickListener(this);
        this.findViewById(R.id.hotSearch).setOnClickListener(this);
        this.findViewById(R.id.keyQuery).setOnClickListener(this);

        mRealmServiceModel.progress().observe(this,this::progress);
        mRealmServiceModel.error().observe(this,this::error);
        mRealmServiceModel.students().observe(this,this::onStudentsChanged);
    }

    public void progress(boolean show){
        this.mask.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void error(String error){
        Toast.makeText(this,error,Toast.LENGTH_LONG).show();
    }

    /**
     * 查询所有学生数据，并更新界面
     */
    public void onStudentsChanged(List<Student> students) {
        container.removeAllViews();
        for (Student s : students) {
            container.addView(buildTextView(s.toString()));
        }
    }

    public TextView buildTextView(String text) {
        TextView txt = new TextView(this);
        txt.setText(text);
        txt.setTextColor(Color.BLACK);
        txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txt.setLayoutParams(lp);
        return txt;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                this.mRealmServiceModel.addStudent();
                break;
            case R.id.query:
                this.mRealmServiceModel.fetchAllStudent();
                break;
            case R.id.clear:
                this.mRealmServiceModel.removeStudents();
                break;
            case R.id.hotSearch:
                this.fetchHotPhrase();
                break;
            case R.id.keyQuery:
                EditText input = findViewById(R.id.content);
                String toQuery = input.getText().toString();
                this.searchVideos(toQuery);
                break;
        }
    }


    private void fetchHotPhrase(){
        this.progress(true);
        RetrofitManager.getHotPhrase().subscribe(t->{
            container.removeAllViews();
            for (String s : t) {
                container.addView(buildTextView(s.toString()));
            }
            progress(false);
        });
    }

    private void searchVideos(String query){
        this.progress(true);
        RetrofitManager.searchVideoInfo(10,new Random().nextInt(10),query).subscribe(item->{
            progress(false);
            fillVideoDatas(item);
        });
    }

    public void startPlay(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri name = Uri.parse(url);
        intent.setDataAndType(name, "video/mp4");
        this.startActivity(intent);
    }


    private void fillVideoDatas(ItemList item){
        if(item != null && item.itemList != null && ! item.itemList.isEmpty()){
            container.removeAllViews();
            for(Item tmp : item.itemList){
                if(tmp.data != null) {
                    View view = LayoutInflater.from(this).inflate(R.layout.video, null);
                    TextView title = view.findViewById(R.id.title);
                    title.setText(tmp.data.title);

                    ImageView image = view.findViewById(R.id.image);
                    ImageLoader.getInstance().displayImage(tmp.data.cover.feed,image);

                    view.setOnClickListener( v-> startPlay(tmp.data.playUrl));
                    view.findViewById(R.id.button).setOnClickListener( v-> startPlay(tmp.data.playUrl));

                    container.addView(view);
                }
            }
        }else{
            Toast.makeText(this,"没有搜索到相关内容!",Toast.LENGTH_LONG).show();
        }
    }
}
