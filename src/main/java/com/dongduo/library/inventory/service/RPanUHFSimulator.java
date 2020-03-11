package com.dongduo.library.inventory.service;

import com.dongduo.library.inventory.util.EpcCode;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
//@ConditionalOnProperty(name = "inventory.useSimulatedDevice", havingValue = "true")
public class RPanUHFSimulator implements IRPanUHF {
    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public void disconnect() {
    }

    @Override
    public Set<EpcCode> getRecordEpc() {
        return Stream.iterate(1, n -> n+1)
                .limit(1000)
                .map(n -> new EpcCode(false, String.valueOf(n)))
                .collect(Collectors.toSet());
    }
}
