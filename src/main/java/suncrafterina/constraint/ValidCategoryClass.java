package suncrafterina.constraint;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import suncrafterina.enums.CategoryLevelEnum;
import suncrafterina.service.dto.CategoryFormDto;
import suncrafterina.service.impl.CategoryService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidCategoryClass implements ConstraintValidator<ValidCategory, CategoryFormDto> {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    MessageSource messageSource;

    @Override
    public void initialize(ValidCategory constraintAnnotation) {

    }

    @Override
    public boolean isValid(CategoryFormDto value, ConstraintValidatorContext context) {

        Long id=value.getId();
        Long parentId=value.getParent_id();
        String title=value.getTitle();
        CategoryLevelEnum categoryLevel=value.getCategory_level();

        if((parentId!=null) && (title!=null) && (categoryLevel!=null)){

            //validate title
            if(id!=null){
                if(this.categoryService.getByIdAndTitle(id,title)!=null){
                    context.disableDefaultConstraintViolation();
                    String msg= messageSource.getMessage("title.already.exist", null, LocaleContextHolder.getLocale());
                    context.buildConstraintViolationWithTemplate(msg).addPropertyNode("title").addConstraintViolation();
                    return  false;
                }
            }else{
                if(this.categoryService.getByTitle(title)!=null){
                    context.disableDefaultConstraintViolation();
                    String msg= messageSource.getMessage("title.already.exist", null, LocaleContextHolder.getLocale());
                    context.buildConstraintViolationWithTemplate(msg).addPropertyNode("title").addConstraintViolation();
                    return  false;
                }
            }

            if(parentId != 0) {
                if (this.categoryService.getById(parentId) == null) {
                    context.disableDefaultConstraintViolation();
                    String msg= messageSource.getMessage("parent.not.exist", null, LocaleContextHolder.getLocale());
                    context.buildConstraintViolationWithTemplate(msg).addPropertyNode("parent_id").addConstraintViolation();
                    return  false;

                }
            }

        }
        return true;
    }
}
