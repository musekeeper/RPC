package com.example.provider;

import org.junit.jupiter.api.Test;



class ProviderApplicationTests {

    @Test
    void contextLoads() {

    }

    public static void main(String[] args) {
        Time t = new Time(1, 2);
        System.out.println(t.of(1, 2));
    }

}
record Time(int x, int y) {
    static int of(int x, int y) {
        return x-y;
    }
}
