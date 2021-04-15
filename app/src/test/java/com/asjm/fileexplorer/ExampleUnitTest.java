package com.asjm.fileexplorer;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        A a = new A();
        B a = new B();
//        getCons(a);
//        getMethods(a);

    }

    private void getMethods(B a) {
        Method[] methods = a.getClass().getDeclaredMethods();
        for(Method m : methods){
            Log.d(m.getName());
            Log.d(m.getModifiers());
            Log.d(m.getReturnType());
            Log.d(m.getParameterTypes());
        }
    }

    private void getCons(B a) {
        Constructor<?>[] constructors = a.getClass().getConstructors();
        Log.d(constructors.length);
        for (Constructor c : constructors) {
            Log.d(c.getName());

        }
    }
}

class A {
    private String name;
    public int id;

    public String getName() {
        return this.name;
    }

    private int getId() {
        return this.id;
    }

}

class B extends A {

    public Long l;
    private boolean b;

    public B() {

    }

    private B(String name) {

    }

    public void print() {

    }

    private void setL() {

    }

}