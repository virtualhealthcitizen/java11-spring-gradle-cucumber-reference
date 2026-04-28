package org.squidmin.cucumber.skeleton.dto.bigquery;

enum Code {
    CODE_UNSPECIFIED,
    REQUESTED,
    LONG_RUNNING,
    LARGE_RESULTS,
    OTHER
}

public class JobCreationReason {

    private Code code;

}
