package suncrafterina.constraint;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageFileValidator implements ConstraintValidator<ValidImage,MultipartFile> {

	@Override
	public void initialize(ValidImage constraintAnnotation) {}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {

		boolean result = true;
		if(value!=null) {

			String contentType = value.getContentType();
			//System.out.println(contentType);

			if (!isSupportedContentType(contentType)) {

				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("Only PNG or JPG or JPEG image are allowed.").addConstraintViolation();

				result = false;
			}
		}

		return result;
	}

	private boolean isSupportedContentType(String contentType) {

		return contentType.equals("image/png")|| contentType.equals("image/jpg")|| contentType.equals("image/jpeg");
	}
}
