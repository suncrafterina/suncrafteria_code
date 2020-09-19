package suncrafterina.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidCategoryClass.class})
@Documented
public @interface ValidCategory {
    String message() default "Invalid form data";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
