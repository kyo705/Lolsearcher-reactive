package com.lolsearcher.reactive.exception.exception;

public class NonAuthorizedSearchException extends NonAuthorizedException {

    private final String ipAddress;

    public NonAuthorizedSearchException(String ipAddress){
        super("해당 IP 주소로는 접근 불가");
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
