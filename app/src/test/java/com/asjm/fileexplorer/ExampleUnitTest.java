package com.asjm.fileexplorer;

import android.os.SystemClock;
import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExampleUnitTest {
    String TAG = "ExampleUnitTest";

    @Test
    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
//        System.out.println(new Child());
        System.out.println(new Sub());
    }

    public abstract class Father {
        public Father() {
            System.out.println(this);
        }
    }

    public class Child extends Father {
        public Child() {
            System.out.println(this);
        }

    }

    public class Sub extends Child {
        public Sub() {
            System.out.println(this);
        }
    }
}