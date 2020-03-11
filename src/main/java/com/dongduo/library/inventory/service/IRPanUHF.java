package com.dongduo.library.inventory.service;

import com.dongduo.library.inventory.util.EpcCode;

import java.util.Set;

public interface IRPanUHF {
    boolean connect();

    void disconnect();

    Set<EpcCode> getRecordEpc();
}
