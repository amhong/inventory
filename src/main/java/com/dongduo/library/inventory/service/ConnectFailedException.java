package com.dongduo.library.inventory.service;

public class ConnectFailedException extends Exception {
    public ConnectFailedException() {
        super("设备连接失败。");
    }
}
