package suncrafterina.web.rest.errors;

import java.io.Serializable;

public class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String objectName;

    private final String field;

    private final String code;

    private final String message;

    public FieldErrorVM(String dto, String field, String code, String message) {
        this.objectName = dto;
        this.field = field;
        this.code = code;
        this.message = message;

    }

    public String getObjectName() {
        return objectName;
    }

    public String getField() {
        return field;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
