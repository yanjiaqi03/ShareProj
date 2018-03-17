package com.example.jacky.shareproj.rxjava;

import com.example.jacky.shareproj.DebugUtils;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * RxJavaDemo
 *
 * @author yanjiaqi
 * @date 2018/3/17-下午4:41
 * @desc 请查阅 http://gank.io/post/560e15be2dca930e00da1083
 */

public class RxJavaDemo {
    public static void main(String[] args) {
//        Observable observable = generateObservableA();
//        Observable observable = generateObservableB();
//        Observable observable = generateObservableC();
//        Observer observer = generateObserverA();
//        Observer observer = generateObserverB();
//        observable.subscribe(observer);
//        testMap();
//        testFlagMap();
        testCompose();
    }

    private static void testMap() {
        Observable<String> observable = Observable.from(new String[]{"1", "22", "333"});
        observable.map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                return s.length();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                DebugUtils.log("rxjava", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                DebugUtils.log("rxjava", "length: " + integer);
            }
        });
    }

    private static void testFlagMap() {
        Person[] persons = new Person[]{
                new Person("100", "200", "300"),
                new Person("400", "500"),
                new Person("600", "700", "800", "900")
        };

        Observable<Person> observable = Observable.from(persons);
        observable.flatMap(new Func1<Person, Observable<String>>() {
            @Override
            public Observable<String> call(Person person) {
                return Observable.from(person.mFlags);
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                DebugUtils.log("rxjava", "flag: " + s);
            }
        });
    }

    private static void testCompose() {
        Observable<String> observable = Observable.from(new String[]{"1", "22", "333", "44", "5", "6666"});
        observable.compose(new Observable.Transformer<String, String>() {
            @Override
            public Observable<String> call(Observable<String> stringObservable) {
                return stringObservable.map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.length();
                    }
                }).observeOn(Schedulers.io())
                        .map(new Func1<Integer, String>() {
                            @Override
                            public String call(Integer integer) {
                                return "len->" + integer;
                            }
                        });
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                DebugUtils.log("rxjava", s);
            }
        });
    }

    private static Observable generateObservableA() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Yan");
                subscriber.onNext("Nan");
                subscriber.onCompleted();
            }
        });
    }

    private static Observable generateObservableB() {
        // 将会依次调用：
        // onNext("1");
        // onNext("2");
        // onNext("3");
        // onCompleted();
        return Observable.just("1", "2", "3");
    }

    private static Observable generateObservableC() {
        // 将会依次调用：
        // onNext("1");
        // onNext("2");
        // onNext("3");
        // onCompleted();
        return Observable.from(new String[]{"1", "2", "3"});
    }

    private static Observer generateObserverA() {
        return new Observer<String>() {
            @Override
            public void onCompleted() {
                DebugUtils.log("rxjava", "completed");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                DebugUtils.log("rxjava", "onNext: " + s);
            }
        };
    }

    private static Subscriber generateObserverB() {
        return new Subscriber<String>() {
            @Override
            public void onStart() {
                DebugUtils.log("rxjava", "onStart");
            }

            @Override
            public void onCompleted() {
                DebugUtils.log("rxjava", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                DebugUtils.log("rxjava", "onNext: " + s);
            }
        };
    }

    static class Person {
        String[] mFlags;

        Person(String... flag) {
            mFlags = flag;
        }
    }
}
