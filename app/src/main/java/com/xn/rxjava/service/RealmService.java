package com.xn.rxjava.service;

import com.xn.rxjava.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by xn068074 on 2018/7/24.
 */

public class RealmService {

    public Single<List<Student>> findAllStudent() {
        return Single.fromCallable(() -> {
            final List<Student> students = new ArrayList<>();
            Realm realm = Realm.getDefaultInstance();
            try {
                realm.executeTransaction(r -> {
                    RealmResults<Student> rs = realm.where(Student.class).findAll();
                    students.addAll(realm.copyFromRealm(rs));
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
            return students;
        });
    }

    public Single<Student> findStudentBy(String name) {
        return Single.fromCallable(() -> {
            Realm realm = null;
            Student student = null;
            try {
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                student = realm.where(Student.class).equalTo("name", name).findFirst();
                realm.commitTransaction();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
            return student;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteStudentBy(String name){
        return Completable.fromAction(()->{
            Realm realm = Realm.getDefaultInstance();
            try {
               realm.executeTransaction(r->{
                   RealmResults<Student> result = r.where(Student.class).equalTo("name",name).findAll();
                   result.deleteFromRealm(0);
               });
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteAllStudent(){
        return Completable.fromAction(()->{
            Realm realm = Realm.getDefaultInstance();
            try {
               realm.executeTransaction( r-> r.deleteAll());
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }

    public Completable updateStudentBy(int index,String name){
        return Completable.fromAction(()->{
            Realm realm = Realm.getDefaultInstance();
            try {
                realm.beginTransaction();
                RealmResults<Student> rs = realm.where(Student.class).findAll();
                List<Student> students = realm.copyFromRealm(rs);
                for(int i = 0; i < students.size(); i++){
                    if(i == index){
                       Student  toUpdate = students.get(i);
                       toUpdate.setName(name);
                       realm.copyToRealmOrUpdate(toUpdate);
                    }
                }
                realm.commitTransaction();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public Single<Student> findStudentBy(int age) {
        return Single.fromCallable(() -> {
            Realm realm = Realm.getDefaultInstance();
            Student student = null;
            try {
                realm.beginTransaction();
                student = realm.where(Student.class).equalTo("age", age).findFirst();
                realm.commitTransaction();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(student == null){
                throw new Exception("没有查到数据!");
            }
            return student;
        });
    }

    public Completable addStudent(Student student) {
        return Completable.fromAction(() -> {
            Realm realm = Realm.getDefaultInstance();
            try {
                realm.executeTransaction(r -> {
                    Student s = realm.createObject(Student.class, UUID.randomUUID().toString());
                    s.setName(student.getName());
                    s.setAddress(student.getAddress());
                    s.setAge(student.getAge());
                    realm.copyFromRealm(s);
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });


//        return Completable.create( e -> {
//            Realm realm = Realm.getDefaultInstance();
//            try {
//                realm.executeTransaction(r -> {
//                    Student s = realm.createObject(Student.class, UUID.randomUUID().toString());
//                    s.setName(student.getName());
//                    s.setAddress(student.getAddress());
//                    s.setAge(student.getAge());
//                    realm.copyFromRealm(s);
//                });
//                //结束业务流程，发送通知完成了;
//                e.onComplete();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                //发生异常，发送一个异常通知;
//                e.onError(ex);
//            } finally {
//                if (realm != null) {
//                    realm.close();
//                }
//            }
//        }).delay(1,TimeUnit.SECONDS);

    }

    public Observable queryStudentWithObservable(int age){
        return Observable.create( (ObservableOnSubscribe<Student>) e -> {
            Realm realm = Realm.getDefaultInstance();
            Student student = null;
            try {
                realm.beginTransaction();
                student = realm.where(Student.class).equalTo("age", age).findFirst();
                realm.commitTransaction();
            } catch (Exception ex) {
                ex.printStackTrace();
            }finally {
                if(student != null){
                    e.onNext(student);
                }else{
                    e.onError(new Throwable("没有查到数据!"));
                }
            }
        });
    }

    public Flowable<Student> queryStudentWithFlowable(int age){
        return Flowable.create( e -> {
            Realm realm = Realm.getDefaultInstance();
            Student student = null;
            try {
                realm.beginTransaction();
                student = realm.where(Student.class).equalTo("age", age).findFirst();
                realm.commitTransaction();
            } catch (Exception ex) {
                ex.printStackTrace();
            }finally {
                if(student != null){
                    e.onNext(student);
                }else{
                    e.onError(new Throwable("没有查到数据!"));
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    public Maybe queryStudentWithMaybe(int age){
        return Maybe.create( e -> {
            Realm realm = Realm.getDefaultInstance();
            Student student = null;
            try {
                realm.beginTransaction();
                student = realm.where(Student.class).equalTo("age", age).findFirst();
                realm.commitTransaction();
            } catch (Exception ex) {
                ex.printStackTrace();
            }finally {
                if(student != null){
                    //如果查询到结果发送数据;
                    e.onSuccess(student);
                }else{
                    //如果没有查询到数据，发送异常消息;
                    //e.onError(new Throwable("没有查到数据!"));
                }
                //事件完成发送完成通知;
                e.onComplete();
            }
        });
    }
}
