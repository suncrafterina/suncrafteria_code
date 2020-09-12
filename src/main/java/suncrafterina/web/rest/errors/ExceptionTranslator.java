package suncrafterina.web.rest.errors;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.*;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;
import suncrafterina.security.UserNotActivatedException;
import suncrafterina.service.UserService;
import suncrafterina.web.rest.util.HeaderUtil;
import suncrafterina.web.rest.vm.LoginVM;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807)
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {

    /**
     * Post-process the Problem payload to add the message key for the front-end if needed
     */
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return entity;
        }

        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem
            || problem instanceof DefaultProblem || problem instanceof CustomException)) {
            return entity;
        }

        String path = request.getNativeRequest(HttpServletRequest.class).getRequestURI();
        ExceptionResponseEntity exResponse;

        if (problem instanceof CustomException) {
            exResponse = new ExceptionResponseEntity(problem.getType(), problem.getStatus().getReasonPhrase(),
                problem.getStatus(), problem.getDetail(), problem.getInstance(),
                ((CustomException) problem).getMessage(), path, ((CustomException) problem).getCause(),
                ((CustomException) problem).getSunCraftStatusCode(), problem.getParameters(), messageSource);

        } else if (problem instanceof ConstraintViolationProblem) {

            URI type = Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType();

            Map<String, Object> map = new HashMap<>();
            map.put("violations", ((ConstraintViolationProblem) problem).getViolations());

            exResponse = new ExceptionResponseEntity(type, problem.getTitle(), problem.getStatus(), problem.getDetail(),
                problem.getInstance(), ErrorConstants.ERR_VALIDATION, path,
                ((ConstraintViolationProblem) problem).getCause(), null, map, messageSource);

        } else {

            URI type = Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType();

            String message = null;
            if (!problem.getParameters().containsKey("message") && problem.getStatus() != null) {
                message = "error.http." + problem.getStatus().getStatusCode();
            }
            exResponse = new ExceptionResponseEntity(type, problem.getTitle(), problem.getStatus(), problem.getDetail(),
                problem.getInstance(), message, path, ((DefaultProblem) problem).getCause(), null,
                problem.getParameters(), messageSource);

        }
        return new ResponseEntity<>(exResponse.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {

        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
            .map(f -> new FieldErrorVM(f.getObjectName(), f.getField(), f.getCode(), f.getDefaultMessage()))
            .collect(Collectors.toList());

        ProblemBuilder problemBuilder = Problem.builder()
            .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
            .withTitle("Method argument not valid")
            .withStatus(defaultConstraintViolationStatus())
            .with("message", ErrorConstants.ERR_VALIDATION)
            .with("fieldErrors", fieldErrors);

        if (result.getTarget() instanceof LoginVM) {
            Set<String> fields = fieldErrors.stream().map(FieldErrorVM::getField).collect(Collectors.toSet());
            if (fields.size() == 2) {
                updateErrorCodes(problemBuilder, SunCraftStatusCode.USERNAME_PASSWORD_INVALID);
            } else {
                for (String itr : fields) {
                    if ("username".equals(itr)) {
                        updateErrorCodes(problemBuilder, SunCraftStatusCode.USERNAME_INVALID);
                    }
                    if ("password".equals(itr)) {
                        updateErrorCodes(problemBuilder, SunCraftStatusCode.PASSWORD_INVALID);
                        //updateErrorCodes(problemBuilder, DigitalSafeStatusCode.BAD_CREDENTIALS);
                    }
                }
            }
        }

        return create(ex, problemBuilder.build(), request);
    }

    private ResponseEntity<Problem> handleCustomException(URI type, String title, StatusType statusType, String message) {
        Problem problem = Problem.builder()
            .withType(type)
            .withTitle(title)
            .withStatus(statusType)
            .with("message", message)
            .build();
        return new ResponseEntity<Problem>(problem, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElementException ex, NativeWebRequest request) {
        ProblemBuilder problemBuilder = Problem.builder()
            .withType(ErrorConstants.ENTITY_NOT_FOUND_TYPE)
            .withTitle("Entity not found")
            .withStatus(Status.NOT_FOUND)
            .with("message", ErrorConstants.ENTITY_NOT_FOUND_TYPE);

        return create(ex, problemBuilder.build(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
        return create(ex, request, HeaderUtil.createFailureAlert(ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {

        Problem problem = Problem.builder()
            .withStatus(Status.CONFLICT)
            .with("message", ErrorConstants.ERR_CONCURRENCY_FAILURE)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleAuthentication(AuthenticationException ex, NativeWebRequest request) {

        SunCraftStatusCode code = null;
        Map<String, Object> params = null;
        String returnMessage = null;
        if (ex.getCause() instanceof UserNotActivatedException || ex instanceof UserNotActivatedException) {
            if (ex.getMessage().equals(SunCraftStatusCode.EMAIL_PHONE_NOT_VERIFIED.getReasonPhrase())) {
                code = SunCraftStatusCode.EMAIL_PHONE_NOT_VERIFIED;
            } else if (ex.getMessage().equals(SunCraftStatusCode.EMAIL_NOT_VERIFIED.getReasonPhrase())) {
                code = SunCraftStatusCode.EMAIL_NOT_VERIFIED;
            } else if (ex.getMessage().equals(SunCraftStatusCode.PHONE_NOT_VERIFIED.getReasonPhrase())) {
                code = SunCraftStatusCode.PHONE_NOT_VERIFIED;
            }
        } else if (ex.getCause() instanceof CustomException) {
            params = ((CustomException) ex.getCause()).getParameters();

        } else {
            code = SunCraftStatusCode.BAD_CREDENTIALS;
        }


        if (returnMessage == null) {
            returnMessage = "error.http." + Status.UNAUTHORIZED;
        }
        ExceptionResponseEntity exceptionResponseEntity = new ExceptionResponseEntity
            (ErrorConstants.DEFAULT_TYPE, Status.UNAUTHORIZED.getReasonPhrase(),
                Status.UNAUTHORIZED, ex.getMessage(), null,returnMessage ,
                request.getNativeRequest(HttpServletRequest.class).getRequestURI(), null, code,
                params, messageSource);

        return new ResponseEntity<>(exceptionResponseEntity.build(), null, HttpStatus.BAD_REQUEST);
    }

    private ProblemBuilder updateErrorCodes(ProblemBuilder problemBuilder, SunCraftStatusCode statusCode) {

        String errorMessage = messageSource.getMessage(statusCode.getReasonPhrase(),
            null, LocaleContextHolder.getLocale());
        return problemBuilder.with("error", statusCode).with("error_message", errorMessage);
        //   return problemBuilder.with("errorCode", statusCode).with("errorMessage", statusCode.getReasonPhrase());
    }
}
