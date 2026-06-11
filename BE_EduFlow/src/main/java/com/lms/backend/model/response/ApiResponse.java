package com.lms.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class ApiResponse<T> {
//
    private String status;
    private String message;
    private T payload;
    private Map<String, String> error;
    private Map<String, Object> metadata;

    public void ok() {
        this.status = "SUCCESS";
    }

    public void ok(T data) {
        this.status = "SUCCESS";
        this.payload = data;
    }

    public void ok(String message, T data) {
        this.status = "SUCCESS";
        this.message = message;
        this.payload = data;
    }

    public void ok(HashMap<String, Object> metadata) {
        this.status = "SUCCESS";
        this.metadata = metadata;
    }

    public void ok(T data, HashMap<String, Object> metadata) {
        this.status = "SUCCESS";
        this.payload = data;
        this.metadata = metadata;
    }

    public void error(String message) {
        this.status = "ERROR";
        this.message = message;
    }

    public void error(String message, Map<String, String> error) {
        this.status = "ERROR";
        this.message = message;
        this.error = error;
    }

    // Phương thức để thiết lập thông tin phân trang vào metadata
    public void setPaginationMetadata(long totalElements, int totalPages, int currentPage, int pageSize) {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        metadata.put("totalElements", totalElements);
        metadata.put("totalPages", totalPages);
        metadata.put("currentPage", currentPage);
        metadata.put("pageSize", pageSize);
    }

    

}


