package suncrafterina.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.violations.Violation;

import java.net.URI;
import java.time.Instant;
import java.util.*;


public class ExceptionResponseEntity implements Problem {

    private static final Set<String> RESERVED_PROPERTIES = new HashSet<>(Arrays.asList(
        "type", "title", "status", "detail", "instance", "cause"
    ));

    private URI type;
    private String title;
    private StatusType status;
    private String detail;
    private URI instance;
    private String message;
    private String path;
    private ThrowableProblem cause;
    private Map<String, Object> parameters;
    private StatusType sunCraftStatusCode;
    private MessageSource messageSource;

    public <T extends StatusType> ExceptionResponseEntity(URI type, String title, StatusType status, String detail,
                                                          URI instance, String message, String path, ThrowableProblem cause,
                                                          T sunCraftStatusCode, Map<String, Object> parameters,
                                                          MessageSource messageSource) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.message = message;
        this.path = path;
        this.cause = cause;
        this.sunCraftStatusCode = sunCraftStatusCode;
        this.parameters = parameters;
        this.messageSource = messageSource;
    }

    public <T extends StatusType> ExceptionResponseEntity(String message, StatusType status, String path,
                                                          T sunCraftStatusCode, Map<String, Object> parameters) {
        this.message = message;
        this.title = status.getReasonPhrase();
        this.status = status;
        this.path = path;
        this.sunCraftStatusCode = sunCraftStatusCode;
        this.parameters = parameters;
    }

    public ThrowableProblem build() {

        ProblemBuilder problemBuilder = Problem.builder()
            .withType(this.type)
            .withTitle(this.title)
            .withStatus(this.status)
            .withDetail(this.detail)
            .withInstance(this.instance)
            .with("message", this.message)
            .with("path", path)
            .with("timestamp", Instant.now())
            .withCause(this.cause);

        if (!CollectionUtils.isEmpty(this.parameters)) {
            this.parameters.entrySet().stream()
                .forEach(entry -> {
                    problemBuilder.with(entry.getKey(), entry.getValue());

                    if (entry.getKey().equalsIgnoreCase("violations")) {
                        List<Violation> violations = (List<Violation>) entry.getValue();
                        if (!CollectionUtils.isEmpty(violations)) {
                            problemBuilder.with("error", SunCraftStatusCode.INVALID_FIELD.getStatusCode())
                                .with("error_message", violations.get(0).getField() + " " + violations.get(0).getMessage());
                        }
                    }
                    // this will work when u have @validated on DTO
                    if (entry.getKey().equalsIgnoreCase("fieldErrors")) {
                        List<FieldErrorVM> fieldErrors = (List<FieldErrorVM>) entry.getValue();
                        if (!CollectionUtils.isEmpty(fieldErrors)) {
                            problemBuilder.with("error", SunCraftStatusCode.INVALID_FIELD.getStatusCode())
                                .with("error_message", fieldErrors.get(0).getField() + " " + fieldErrors.get(0).getMessage());
                        }
                    }
                });
        }

        if (this.sunCraftStatusCode != null) {
            String error_message = null;
            if (this.sunCraftStatusCode.getStatusCode() == SunCraftStatusCode.INVALID_FIELD.getStatusCode() ) {
                error_message = this.message;
            }  else {
                try {
                    error_message = messageSource.getMessage(this.sunCraftStatusCode.getReasonPhrase(),
                        null, LocaleContextHolder.getLocale());
                } catch (Exception e) {
                    error_message = this.sunCraftStatusCode.getReasonPhrase();
                }
                if (StringUtils.isEmpty(error_message)) {
                    error_message = this.sunCraftStatusCode.getReasonPhrase();
                }
            }

            problemBuilder.with("error", this.sunCraftStatusCode.getStatusCode())
                .with("error_message", error_message);
        }

        return problemBuilder.build();
    }
}
