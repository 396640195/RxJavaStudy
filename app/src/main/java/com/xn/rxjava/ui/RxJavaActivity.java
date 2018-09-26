package com.xn.rxjava.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import com.xn.rxjava.entity.Student;
import com.xn.rxjava.service.*;

public class RxJavaActivity extends AppCompatActivity implements OnClickListener {

    private TextView nameTextView;
    private TextView indexTextView;
    private TextView rmNameTextView;
    private LinearLayout container;
    private RealmService RealmService = new RealmService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.nameTextView = this.findViewById(R.id.name);
        this.indexTextView = this.findViewById(R.id.index);
        this.rmNameTextView = this.findViewById(R.id.rm_name);
        container = this.findViewById(R.id.container);

        this.findViewById(R.id.addStudent).setOnClickListener(this);
        this.findViewById(R.id.remove).setOnClickListener(this);
        this.findViewById(R.id.update).setOnClickListener(this);
        this.findViewById(R.id.Merge).setOnClickListener(this);
        this.findViewById(R.id.Concat).setOnClickListener(this);
        this.findViewById(R.id.Compose).setOnClickListener(this);
        queryStudent();
    }

    /**
     * 查询所有学生数据，并更新界面
     */
    public void queryStudent() {
        container.removeAllViews();
        RealmService.findAllStudent()
                .subscribe(students -> {
                    for (Student s : students) {
                        container.addView(buildTextView(s.toString()));
                    }
                });
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

    /**
     * 增加
     */
    public void addStudent() {
        Student a = new Student();
        a.setAge(14);
        a.setAddress("中国北京");
        a.setName("浩子");

        Student b = new Student();
        b.setAge(15);
        b.setAddress("深圳小牛在线");
        b.setName("陈真");

        Student c = new Student();
        c.setAge(16);
        c.setAddress("中国上海");
        c.setName("陈永和");

        RealmService.addStudent(a).subscribe(this::queryStudent);
        RealmService.addStudent(b).subscribe(this::queryStudent);
        RealmService.addStudent(c).subscribe(this::queryStudent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addStudent:
                this.addStudent();
                break;
            case R.id.remove:
                String rmName = rmNameTextView.getText().toString();
                RealmService
                        .deleteStudentBy(rmName)
                        .subscribe(this::queryStudent);
                break;
            case R.id.update:
                String name = nameTextView.getText().toString();
                String index = indexTextView.getText().toString();
                if (TextUtils.isEmpty(index)) {
                    Toast.makeText(this, "索引号必需填写", Toast.LENGTH_LONG).show();
                }
                RealmService
                        .updateStudentBy(Integer.valueOf(index), name)
                        .subscribe(this::queryStudent);
                break;
            case R.id.Concat:
                this.concat();
                break;

            case R.id.Merge:
                this.queryStudentByAge();
                break;

            case R.id.Compose:
                backPressure();
                break;
        }
    }

    public void merge() {
        container.removeAllViews();
        Single<Student> s15 = RealmService.findStudentBy(15);
        Single<Student> s14 = RealmService.findStudentBy(14);

        s14.mergeWith(s15).subscribe(s -> {
            container.addView(buildTextView(s.toString()));
        });
    }

    public void concat() {
        container.removeAllViews();
        Single<Student> s15 = RealmService.findStudentBy(15);
        Single<Student> s14 = RealmService.findStudentBy(14);

        s14.concatWith(s15).subscribe(
                s -> container.addView(buildTextView(s.toString())),
                e -> Toast.makeText(RxJavaActivity.this, "没有查询到数据!", Toast.LENGTH_LONG).show());
    }

    public void compose() {
        container.removeAllViews();
        Single<Student> s15 = RealmService.findStudentBy(15);
        Single<Student> s14 = RealmService.findStudentBy(14);

        s15.compose(upstream -> s14).subscribe(s -> container.addView(buildTextView(s.toString())));
    }

    public void queryStudentByAge() {
        container.removeAllViews();
//        RealmService.getRealmService().queryStudentWithObservable(15).subscribe(
//                student-> container.addView(buildTextView(student.toString())),
//                throwable-> Toast.makeText(this,throwable.toString(),Toast.LENGTH_LONG).show());

        RealmService.queryStudentWithMaybe(15).subscribe(
                student -> container.addView(buildTextView(student.toString())),
                error -> Toast.makeText(RxJavaActivity.this, error.toString(), Toast.LENGTH_LONG).show()
                )
                .dispose();
    }


    public void singleQuerySample() {
        container.removeAllViews();
        Single<Student> s15 = RealmService.findStudentBy(15);
        s15.subscribe(
                student -> container.addView(buildTextView(student.toString())),
                error -> Toast.makeText(RxJavaActivity.this, error.toString(), Toast.LENGTH_LONG).show());
    }

    public void CompletebleA(){
        Completable.create( e-> {
            //toDosometing
            e.onComplete();});
    }

    public void queryStudentWithMaybe() {
        container.removeAllViews();
        RealmService.queryStudentWithMaybe(15).subscribe(
                student -> container.addView(buildTextView(student.toString())),
                error -> Toast.makeText(RxJavaActivity.this, error.toString(), Toast.LENGTH_LONG).show())
                .dispose();
    }

    public void map() {
        container.removeAllViews();
        Observable.create(e -> {
                    e.onNext(1);
                    e.onNext(2);
                    e.onNext(3);}
        ).concatMap( upstream-> {
            List<String> source = new ArrayList<>();
            for(int i=0; i<15; i++){
                source.add("0x"+ upstream);
            }
            return Observable.fromIterable(source);
        })
        .subscribe(result-> container.addView(buildTextView(result)));
    }

    //创建 String 发射器
    private Observable<String> getStringObservable() {
        return Observable.create( e->{
            e.onNext("A");
            e.onNext("B");
            e.onNext("C");
        });
    }

    //创建 Integer 发射器
    private Observable<Integer> getIntegerObservable() {
        return Observable.create(e-> {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);
                e.onNext(5);
        });
    }

    private void zip(){
        Observable.zip(
                getStringObservable(),
                getIntegerObservable(),
                (string,integer)->string+integer).subscribe(
                        result-> container.addView(buildTextView(result))
                );
    }

    private void interval(){

        Observable.interval(1, TimeUnit.SECONDS)
                .map(source-> 5-source)
                .observeOn(AndroidSchedulers.mainThread())
                .take(5)
                .subscribe(result-> container.addView(buildTextView(String.valueOf(result))));
    }

    private void repeat(){
        Observable.just(1,2)
                .repeat(2)
                .subscribe(result-> container.addView(buildTextView(String.valueOf(result))));
    }

    private void range(){
        Observable
                .range( 1 , 5 )
                .subscribe(result-> container.addView(buildTextView(String.valueOf(result))));
    }

    private void backPressure(){
        Flowable.interval( 1 , TimeUnit.MILLISECONDS)
                .onBackpressureBuffer( 100) //设置缓冲队列大小为 1000
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result-> container.addView(buildTextView(String.valueOf(result))));
    }
}
