package com.nins.interfaces;

import java.util.Random;

public class MyBean {

    public int process() {
        return new Random().nextInt();
    }

    public void shutdown() {
        System.out.println("MyBean shutdown");
    }

}
