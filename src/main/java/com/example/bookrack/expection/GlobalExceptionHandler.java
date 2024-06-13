package com.example.bookrack.expection;

import com.example.bookrack.response.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public ResponseEntity<ApiResponse> handleRecordNotFoundException(RecordNotFoundException ex){
        ApiResponse response = new ApiResponse("failed", ex.getMessage(), 400);
        return new  ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
