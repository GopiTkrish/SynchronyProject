package com.synchrony.synchrony.service;

import com.synchrony.synchrony.cache.CacheService;
import com.synchrony.synchrony.model.DataRecord;
import com.synchrony.synchrony.repository.DataRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class DataService {

    @Autowired
    private DataRecordRepository repository;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ExecutorService executorService;

    public DataRecord saveData(String data) {
        DataRecord record = new DataRecord();
        record.setData(data);
        record.setCreatedAt(LocalDateTime.now());
        DataRecord savedRecord = repository.save(record);
        cacheService.cacheData("record_" + savedRecord.getId(), savedRecord);
        return savedRecord;
    }

    public DataRecord fetchData(Long id) {
        String cacheKey = "record_" + id;
        Object cachedData = cacheService.getCachedData(cacheKey);
        if (cachedData != null) {
            return (DataRecord) cachedData;
        }

        //Making call to Actual db
        DataRecord record = repository.findById(id).orElseThrow(() -> new RuntimeException("Data not found"));
        cacheService.cacheData(cacheKey, record);
        return record;
    }

    public void processInParallel(List<Long> ids) {
        List<Future<DataRecord>> futures = ids.stream()
                .map(id -> executorService.submit(() -> fetchData(id)))
                .collect(Collectors.toList());

        futures.forEach(future -> {
            try {
                System.out.println("Processed: " + future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
