package suncrafterina.web.rest.errors;

import org.zalando.problem.StatusType;

public enum SunCraftStatusCode implements StatusType {

    USERNAME_PASSWORD_INVALID(10010, "username.password.failed.validation"),
    USER_LOGGED_INTO_ANOTHER_DEVICE(10011,"user.loggedin.into.another.device"),
    INVALID_FIELD(10012, "invalid.field"),
    USERNAME_INVALID(10013,"invalid.user.name"),
    BAD_CREDENTIALS(10014,"bad.credentials"),
    EMAIL_PHONE_NOT_VERIFIED(10015,"email.phone.not.verified"),
    EMAIL_NOT_VERIFIED(10016,"email.not.verified"),
    PHONE_NOT_VERIFIED(10017,"phone.not.verified"),
    INVALID_KEY(10018,"invalid.key"),
    PASSWORD_INVALID(10019,"invalid.password"),
    EMAIL_INVALID(10020,"invalid.email"),
    EMAIL_ALREADY_EXIST(10021,"email.already.exist"),
    FORGOT_PASSWORD_LINK_EXPIRE(10022,"forgot.link.expire"),
    TWILIO_ERROR(10023,"twilio.error"),
    PHONE_VERIFICATION_FAILED(10024,"phone.verification.failed"),
    OTP_EXPIRED(10025,"otp.expired"),
    CURRENT_PASSWORD_NOT_MATCHED(10026,"password.not.matched"),
    VERIFICATION_CODE_NOT_MATCHED(10027, "verification.code.not.matched"),
    EMAIL_NOT_FOUND(10028, "email.not.found"),
    VERIFICATION_CODE_EXPIRED(10029, "verification.code.expired"),
    USER_NOT_ACTIVATED(10030,"user.not.activate"),
    INCORRECT_OTP(10031, "incorrect.otp"),
    IMAGE_NOT_UPLOADED(10032,"product.image.not.uploaded"),
    CATEGORY_NOT_FOUND(10033,"invalid.category"),
    ALREADY_EXISTS(10034,"already.exist"),
    NOT_FOUND(10035,"not.found");

    private final int code;
    private final String reason;

    SunCraftStatusCode(int statusCode, String reasonPhrase) {
        this.code = statusCode;
        this.reason = reasonPhrase;
    }

    public static SunCraftStatusCode getSunCraftStatusCode(int statusCode) {
        for (SunCraftStatusCode sunCraftStatusCode : SunCraftStatusCode.values()) {
            if (sunCraftStatusCode.getStatusCode() == statusCode) {
                return sunCraftStatusCode;
            }
        }
        return null;
    }

    @Override
    public int getStatusCode() {
        return this.code;
    }

    @Override
    public String getReasonPhrase() {
        return this.reason;
    }
}
