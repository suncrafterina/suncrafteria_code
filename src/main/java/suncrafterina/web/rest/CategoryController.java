package suncrafterina.web.rest;

import io.github.jhipster.web.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.zalando.problem.Status;
import suncrafterina.domain.Category;
import suncrafterina.enums.CategoryLevelEnum;
import suncrafterina.security.AuthoritiesConstants;
import suncrafterina.service.AmazonClient;
import suncrafterina.service.HelperService;
import suncrafterina.service.dto.CategoryDto;
import suncrafterina.service.dto.CategoryFormDto;
import suncrafterina.service.dto.CategoryLevelTwoDto;
import suncrafterina.service.dto.ResponseStatusDTO;
import suncrafterina.service.impl.CategoryService;
import suncrafterina.web.rest.errors.CustomException;
import suncrafterina.web.rest.errors.SunCraftStatusCode;

import javax.validation.Valid;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.SUB_ADMIN + "\")")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HelperService helperService;

    private AmazonClient amazonClient;

    @Autowired
    MessageSource messageSource;

    @Autowired
    CategoryController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }



    @GetMapping("/category-level-one")
    public ResponseEntity<List<CategoryDto>> getAllCategories(@RequestParam(required = false) String search, Pageable pageable) {

        Page<Object[]> page= categoryService.getCategoriesLevelOne(search,pageable);
        List<CategoryDto> categoryDtoList=new ArrayList<>();
        for (Object[] category : page) {
            BigInteger id= (BigInteger) category[0];
            String title=(String) category[1];
            BigInteger parent_id= (BigInteger) category[2];
            CategoryLevelEnum category_level=  CategoryLevelEnum.values()[(Short) category[3]];
            String image_file=(String) category[4];
            BigInteger subcategory_count= (BigInteger) category[5];
            String image_file_thumb = (String) category[6];
            String icon_file = (String) category[7];
            String slug = (String) category[8];
            Long subcategory=null;
            if(subcategory_count!=null) {
                subcategory = subcategory_count.longValue();
            }

            CategoryDto categoryDto=new CategoryDto(id.longValue(),title,parent_id.longValue(),
                category_level,subcategory,image_file,image_file_thumb,
                icon_file,slug);
            categoryDtoList.add(categoryDto);
        }
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(categoryDtoList);
    }


    @PostMapping("/category")
    public ResponseEntity<ResponseStatusDTO> saveCategory(@Valid @ModelAttribute CategoryFormDto categoryFormDto) {
        Category category = new Category();
        category.setTitle(categoryFormDto.getTitle().trim());
        if(categoryFormDto.getImage_file()!=null) {
            String image_file = this.amazonClient.uploadFile(categoryFormDto.getImage_file(),"suncraft_category");
            category.setImage_file(image_file);
            MultipartFile multipartFile=null;
            try{
                multipartFile = helperService.createThumbnail(categoryFormDto.getImage_file());
                String thumbnail= this.amazonClient.uploadFile(multipartFile,"suncraft_category");
                category.setImage_file_thumb(thumbnail);
            }catch(Exception ex){
                throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.IMAGE_NOT_UPLOADED, null);
            }
        }
        if(categoryFormDto.getIcon_file()!=null){
            String icon_file = this.amazonClient.uploadFile(categoryFormDto.getIcon_file(),"suncraft_category");
            category.setIcon_file(icon_file);
        }
        String slug = helperService.getSlug(categoryFormDto.getTitle().trim());
        category.setSlug(slug);
        category.setParent_id(categoryFormDto.getParent_id());
        category.setCategory_level(categoryFormDto.getCategory_level());
        category.setCreated_at(LocalDateTime.now());
        category = categoryService.save(category);
        ResponseStatusDTO status = new ResponseStatusDTO("Category added.", HttpStatus.CREATED.value(),System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<>(status,HttpStatus.CREATED);
        return responseEntity;
    }



    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getFAQsById(@PathVariable("id") Long id)
    {
        Category category = this.categoryService.getById(id);
        if(category == null)
            //throw new DataNotFoundException("Catgory id = "+id+" is not found!");
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.CATEGORY_NOT_FOUND,null);

        ResponseEntity<Category> responseEntity = new ResponseEntity<Category>(category,HttpStatus.OK);

        return responseEntity;
    }


    @PutMapping("/category/{id}")
    public ResponseEntity<ResponseStatusDTO> updateCategory(@PathVariable(value = "id") Long id,@Valid @ModelAttribute CategoryFormDto categoryFormDto) {
        Category category=categoryService.getById(id);
        if(category == null)
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.CATEGORY_NOT_FOUND,null);

        if(categoryFormDto.getImage_file()!=null) {
            String image_file = this.amazonClient.uploadFile(categoryFormDto.getImage_file(),"suncraft_category");
            if(category.getImage_file()!=null){
                this.amazonClient.deleteFileFromS3Bucket(category.getImage_file(),"suncraft_category");
            }
            MultipartFile multipartFile=null;
            String thumbnail=null;
            try{
                multipartFile = helperService.createThumbnail(categoryFormDto.getImage_file());
                thumbnail= this.amazonClient.uploadFile(multipartFile,"suncraft_category");
            }catch(Exception ex){
                throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.IMAGE_NOT_UPLOADED, null);
            }
            if(category.getImage_file_thumb()!=null){
                this.amazonClient.deleteFileFromS3Bucket(category.getImage_file_thumb(),"suncraft_category");
            }
            category.setImage_file(image_file);
            category.setImage_file_thumb(thumbnail);
        }

        if(categoryFormDto.getIcon_file()!=null){
            String icon_file = this.amazonClient.uploadFile(categoryFormDto.getIcon_file(),"suncraft_category");
            if(category.getIcon_file()!=null){
                this.amazonClient.deleteFileFromS3Bucket(category.getIcon_file(),"suncraft_category");
            }
            category.setIcon_file(icon_file);
        }
        String slug = helperService.getSlug(categoryFormDto.getTitle().trim());
        category.setSlug(slug);
        category.setTitle(categoryFormDto.getTitle().trim());
        category.setParent_id(categoryFormDto.getParent_id());
        category.setCategory_level(categoryFormDto.getCategory_level());
        category.setUpdated_at(LocalDateTime.now());
        categoryService.save(category);
        ResponseStatusDTO status = new ResponseStatusDTO("Category Updated",HttpStatus.OK.value(),System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<>(status,HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/category-level-two/{category_id}")
    public ResponseEntity<List<CategoryLevelTwoDto>> getAllCategoriesLevelThree(@PathVariable(value = "category_id") Long category_id, @RequestParam(required = false) String search, Pageable pageable) {
        Category category = categoryService.getById(category_id);
        if(category == null)
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.CATEGORY_NOT_FOUND,null);
        Page<Object[]> page = categoryService.getCategoriesLevelTwo(category_id,search,pageable);
        List<CategoryLevelTwoDto> categoryLevelThreeDtoList=new ArrayList<>();
        for (Object[] category1 : page) {
            BigInteger id= (BigInteger) category1[0];
            String title=(String) category1[1];
            BigInteger parent_id= (BigInteger) category1[2];
            CategoryLevelEnum category_level= CategoryLevelEnum.values()[(Short) category1[3]];
            String image_file=(String) category1[4];
            BigInteger product_count= (BigInteger) category1[5];
            String image_file_thumb = (String) category1[6];
            String icon_file = (String) category1[7];
            String slug = (String) category1[8];
            Long product_count_long;
            if(product_count==null){
                product_count_long = 0L;
            }
            else
                product_count_long=product_count.longValue();
            CategoryLevelTwoDto categoryLevelThreeDto=new CategoryLevelTwoDto(id.longValue(),title,parent_id.longValue(),category_level,product_count_long,image_file,image_file_thumb,icon_file,slug);
            categoryLevelThreeDtoList.add(categoryLevelThreeDto);
        }
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(categoryLevelThreeDtoList);
    }


}
