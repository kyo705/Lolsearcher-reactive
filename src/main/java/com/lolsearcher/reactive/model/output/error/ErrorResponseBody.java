package com.lolsearcher.reactive.model.output.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponseBody {

    private int errorStatusCode;
    private String errorMessage;

}
