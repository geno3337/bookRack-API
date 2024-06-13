package com.example.bookrack.response.common;

import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ApiResponse {

    protected Map<String,String> headers= new HashMap<>();

    protected List<?> data;

    protected Object recordInfo;

    protected Integer totalPages;

    protected Integer currentPage;

    protected Integer recordLimit;

    protected Long totalRecords;

    protected Object userDetails;

    public ApiResponse(String status,String message,Integer statusCode){
        this.headers.put("status",status);
        this.headers.put("statusCode",statusCode.toString());
        this.headers.put("message",message);
    }
}
