package com.example.tenderdemo.error;

public class TenderServiceException extends RuntimeException {

    private ErrorReport errorReport;

    public TenderServiceException(ErrorReport errorReport) {
        this.errorReport = errorReport;
    }

    public ErrorReport getErrorReport() {
        return errorReport;
    }
}
