package com.nins.interfaces;

import java.util.Random;

public class MyBean2 {

    public int process() {
        return new Random().nextInt();
    }

    public void shutdown() {
        System.out.println("22222222222MyBean shutdown");
    }

}
