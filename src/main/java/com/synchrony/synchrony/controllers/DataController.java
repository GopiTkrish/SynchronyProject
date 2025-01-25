package com.synchrony.synchrony.controllers;

import com.synchrony.synchrony.model.DataRecord;
import com.synchrony.synchrony.model.DataRequest;
import com.synchrony.synchrony.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DataService dataService;


//    {
//        "data": "Sample Data"
//    }
    @PostMapping
    public ResponseEntity<DataRecord> saveData(@RequestBody DataRequest request) {
        DataRecord record = dataService.saveData(request.getData());
        return ResponseEntity.ok(record);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataRecord> getData(@PathVariable Long id) {
        DataRecord record = dataService.fetchData(id);
        return ResponseEntity.ok(record);
    }

//    [1, 2, 3, 4, 5]
    @PostMapping("/process")
    public ResponseEntity<String> processData(@RequestBody List<Long> ids) {
        dataService.processInParallel(ids);
        return ResponseEntity.ok("Processing initiated.");
    }
}

