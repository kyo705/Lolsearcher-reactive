package com.lolsearcher.reactive.exception;

public class NonAuthorizedSearchException extends RuntimeException{

    private final String ipAddress;

    public NonAuthorizedSearchException(String ipAddress){
        this.ipAddress = ipAddress;
    }

    @Override
    public String getMessage() {
        if(ipAddress==null){
            return "클라이언트 요청에 대한 IP 주소가 없음";
        }else{
            return String.format("접근 권한이 없는 IP : %s 유저가 접근하였음.", ipAddress);
        }
    }
}
