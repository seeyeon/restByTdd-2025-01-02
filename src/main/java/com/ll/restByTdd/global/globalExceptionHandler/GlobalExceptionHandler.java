package com.ll.restByTdd.global.globalExceptionHandler;

import com.ll.restByTdd.global.app.AppConfig;
import com.ll.restByTdd.global.exceptions.ServiceException;
import com.ll.restByTdd.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<RsData<Void>> handle(NoSuchElementException ex) {

        if(AppConfig.isNotProd()) ex.printStackTrace(); //오류 발생 경위 실행창에 찍힘

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new RsData<>(
                        "404-1",
                        "해당 데이터가 존재하지 않습니다."
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RsData<Void>> handle(MethodArgumentNotValidException ex) {

        ex.printStackTrace();

        String msg = ex.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .filter(error -> error instanceof FieldError)
                    .map(error -> (FieldError) error)
                    .map(error -> error.getField() + "-" + error.getCode() + "-" + error.getDefaultMessage())
                    .sorted(Comparator.comparing(String::toString))
                    .collect(Collectors.joining("\n"));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new RsData<>(
                        "400-1",
                        msg
                ));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<RsData<Void>> handle(ServiceException ex) {

        if(AppConfig.isNotProd()) ex.printStackTrace();

        RsData<Void> rsData = ex.getRsData();

        return ResponseEntity
                .status(rsData.getStatusCode())
                .body(rsData);
    }
}