package com.lolsearcher.reactive.constant.enumeration;

import lombok.Getter;

@Getter
public enum CertificationStatus {


    EXPIRED(703);

    private final int statusCode;

    CertificationStatus(int code){
        this.statusCode = code;
    }
}