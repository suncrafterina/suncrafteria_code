package suncrafterina.web.rest.errors;



import org.apache.commons.lang3.StringUtils;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import java.util.Map;

public class CustomException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private static final String PARAM = "param";

    private String message;
    private String entityName;
    private String errorKey;
    private StatusType sunCraftStatusCode;


    public <T extends StatusType> CustomException(String message, Status status, T sunCraftStatusCode,
                                                  Map<String, Object> parameters) {
        super(ErrorConstants.PARAMETERIZED_TYPE, status.getReasonPhrase(),
            status, null, null, null,
            parameters);
        this.message = message;
        this.sunCraftStatusCode = sunCraftStatusCode;
    }

    public <T extends StatusType> CustomException(Status status, T digitalSafeStatusCode, Map<String, Object> parameters) {
        super(ErrorConstants.PARAMETERIZED_TYPE, status.getReasonPhrase(),
            status, null, null, null,
            parameters);
        this.message = digitalSafeStatusCode.getReasonPhrase();
        this.sunCraftStatusCode = digitalSafeStatusCode;
    }

    public <T extends StatusType> CustomException(String message, String entityName, String errorKey, Status status,
                                                  T docAppStatusCode, Map<String, Object> parameters) {
        if (StringUtils.isEmpty(message)) {
            this.message = docAppStatusCode.getReasonPhrase();
        } else {
            this.message = message;
        }
        this.entityName = entityName;
        this.errorKey = errorKey;
    }


    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    public StatusType getSunCraftStatusCode() {
        return sunCraftStatusCode;
    }

    public void setSunCraftStatusCode(StatusType sunCraftStatusCode) {
        this.sunCraftStatusCode = sunCraftStatusCode;
    }
}
