package com.xn.rxjava.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.xn.rxjava.entity.Student;
import com.xn.rxjava.service.RealmService;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by xn068074 on 2018/8/24.
 */

public class RealmServiceModel extends BaseViewModel {

    private final RealmService mRealmService;
    private final MutableLiveData<List<Student>> studentsLiveData = new MutableLiveData<>();

    public RealmServiceModel(RealmService mRealmService) {
        this.mRealmService = mRealmService;
    }

    public MutableLiveData<List<Student>> students(){
        return this.studentsLiveData;
    }

    public void fetchAllStudent(){
        progress.postValue(true);
        mRealmService.findAllStudent()
                .delay(2, TimeUnit.SECONDS)
                .subscribe(students -> {
                     progress.postValue(false);
                     studentsLiveData.postValue(students);
                });
    }

    public void addStudent(){
        this.progress.postValue(true);
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

        this.mRealmService.addStudent(a).subscribe();
        this.mRealmService.addStudent(b).subscribe();
        this.mRealmService.addStudent(c)
                .delay(1,TimeUnit.SECONDS)
                .subscribe(()->this.progress.postValue(false));

    }

    public void removeStudents(){
        this.progress.postValue(true);
        this.mRealmService.deleteAllStudent()
                .delay(1,TimeUnit.SECONDS)
                .subscribe(()->this.progress.postValue(false));

    }
}
