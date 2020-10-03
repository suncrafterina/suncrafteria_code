package suncrafterina.web.rest;

import io.github.jhipster.web.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.zalando.problem.Status;
import suncrafterina.domain.Category;
import suncrafterina.domain.Product;
import suncrafterina.domain.User;
import suncrafterina.enums.CategoryLevelEnum;
import suncrafterina.security.AuthoritiesConstants;
import suncrafterina.security.SecurityUtils;
import suncrafterina.service.HelperService;
import suncrafterina.service.UserService;
import suncrafterina.service.dto.*;
import suncrafterina.service.impl.CategoryService;
import suncrafterina.service.impl.ProductService;
import suncrafterina.web.rest.errors.CustomException;
import suncrafterina.web.rest.errors.SunCraftStatusCode;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.SUB_ADMIN + "\")")
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private HelperService helperService;

    @GetMapping("/category/list")
    public ResponseEntity<List<CategoryListDTO>> getCategoryList() {
        List<Object[]> categoryList = categoryService.getCategoriesLevelThree();
        List<CategoryListDTO> categoryDtos = new ArrayList<>();

        for (Object[] category : categoryList) {
            BigInteger id = (BigInteger) category[0];
            String title = (String) category[1];
            CategoryListDTO categoryDto = new CategoryListDTO(id.longValue(), title);
            categoryDtos.add(categoryDto);
        }
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

    @PostMapping("/product")
    public ResponseEntity<ResponseStatusDTO> addProduct(@Valid @RequestBody ProductDTO productDTO){
        String login = SecurityUtils.getCurrentUserLogin().get();
        Optional<User> user = userService.getUserByEmail(login);
        if (!user.isPresent())
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.NOT_FOUND,null);
        Category category = categoryService.getById(productDTO.getCategory_id());
        if(category.getCategory_level() != CategoryLevelEnum.LEVEL_TWO){
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.INVALID_FIELD,null);
        }
        Product product = new Product();
        product.setName(productDTO.getName().trim());
        product.setDescription(productDTO.getDescription().trim());
        if(productDTO.getSku()!=null)
            product.setSku(productDTO.getSku().trim());
        product.setManufacturer(productDTO.getManufacturer().trim());
        product.setPrice(productDTO.getPrice());
        product.setDimension(productDTO.getDimension().trim());
        product.setLength_unit(productDTO.getLength_unit());
        product.setWeight(productDTO.getWeight().trim());
        product.setWeight_unit(productDTO.getWeight_unit());
        product.setCategory(category);
        product.setUser(user.get());
        product.setCreated_at(LocalDateTime.now());
        product.setUpdated_at(LocalDateTime.now());
        productService.save(product);
        ResponseStatusDTO status = new ResponseStatusDTO("Product added successfully.", HttpStatus.OK.value(), System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/all-products")
    public ResponseEntity<List<ProductListDTO>> getProducts(@RequestParam(required = false) String search, Pageable pageable, Locale locale) {
        List<ProductListDTO> productDtos = new ArrayList<>();

        Page<Object[]> objects = productService.getAllUserProduct(search, pageable);
        for (Object[] object : objects) {
            BigInteger id = (BigInteger) object[0];
            String name = (String) object[1];
            String image_file = (String) object[2];
            String image_file_thumb = (String) object[11];
            String sku = (String) object[3];
            String vendor = (String) object[4];
            String manufacturer = (String) object[5];
            Timestamp created_at = (Timestamp) object[6];
            LocalDate added_on = created_at.toLocalDateTime().toLocalDate();
            BigDecimal price = (BigDecimal) object[7];
            Boolean vendor_show_status = (Boolean) object[8];
            Double rating = (Double) object[9];
            Boolean admin_show_status = (Boolean) object[10];
            Double round_rating = null;
            Double format_price = null;

            if (price != null) {
                format_price = price.doubleValue();
            }
            if (rating != null) {
                round_rating = helperService.round(rating, 2);
            }else
                round_rating=0.0;
            Boolean is_sponsored = (Boolean) object[12];
            Boolean is_new_arrival = (Boolean) object[13];
            ProductListDTO productDto = new ProductListDTO(id.longValue(), image_file, image_file_thumb, name, sku, vendor, manufacturer, added_on, format_price,vendor_show_status, round_rating,admin_show_status,is_sponsored,is_new_arrival);
            productDtos.add(productDto);
        }

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), objects);
        return ResponseEntity.ok().headers(headers).body(productDtos);
    }

    @GetMapping("/products")
    public ResponseEntity<List<AdminProductListDTO>> getAdminProducts(@RequestParam(required = false) String search, Pageable pageable, Locale locale) {
        List<AdminProductListDTO> productDtos = new ArrayList<>();

        String login = SecurityUtils.getCurrentUserLogin().get();
        User user = userService.getUserByEmail(login).get();
        Page<Object[]> objects = productService.getAllProduct(user.getId(), search, pageable);
        for (Object[] object : objects) {
            BigInteger id = (BigInteger) object[0];
            String name = (String) object[1];
            String image_file = (String) object[2];
            String image_file_thumb = (String) object[10];
            String sku = (String) object[3];
            String category = (String) object[4];
            String manufacturer = (String) object[5];
            Timestamp created_at = (Timestamp) object[6];
            LocalDate added_on = created_at.toLocalDateTime().toLocalDate();
            BigDecimal price = (BigDecimal) object[7];
            Boolean vendor_show_status = (Boolean) object[8];
            Double rating = (Double) object[9];
            Double round_rating = null;
            Double format_price = null;

            if (price != null) {
                format_price = price.doubleValue();
            }
            if (rating != null) {
                round_rating = helperService.round(rating, 2);
            }else
                round_rating=0.0;

            AdminProductListDTO productDto = new AdminProductListDTO(id.longValue(), image_file, image_file_thumb, name, sku, category, manufacturer, added_on, format_price,vendor_show_status, round_rating);
            productDtos.add(productDto);
        }

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), objects);
        return ResponseEntity.ok().headers(headers).body(productDtos);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProductDetail(@PathVariable Long id){
        String login = SecurityUtils.getCurrentUserLogin().get();
        User user = userService.getUserByEmail(login).get();
        Product product = productService.getById(id);
        if(product == null){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        if(!product.getUser().getEmail().equalsIgnoreCase(user.getEmail())){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        ProductDTO productDTO = new ProductDTO(product.getId(),product.getName(),product.getDescription(),product.getSku(),
                                    product.getManufacturer(),product.getPrice(),product.getDimension(),product.getLength_unit(),
                                    product.getWeight(),product.getWeight_unit(),product.getCategory().getId());
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @PutMapping("/product")
    public ResponseEntity<ResponseStatusDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO){
        if(productDTO.getId()==null)
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        String login = SecurityUtils.getCurrentUserLogin().get();
        User user = userService.getUserByEmail(login).get();
        Product product = productService.getById(productDTO.getId());
        if(product == null){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        if(!product.getUser().getEmail().equalsIgnoreCase(user.getEmail())){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        product.setName(productDTO.getName().trim());
        product.setDescription(productDTO.getDescription().trim());
        if(productDTO.getSku()!=null)
            product.setSku(productDTO.getSku().trim());
        product.setManufacturer(productDTO.getManufacturer().trim());
        product.setPrice(productDTO.getPrice());
        product.setDimension(productDTO.getDimension().trim());
        product.setLength_unit(productDTO.getLength_unit());
        product.setWeight(productDTO.getWeight().trim());
        product.setWeight_unit(productDTO.getWeight_unit());
        if(product.getCategory().getId() != productDTO.getId()){
            Category category = categoryService.getById(productDTO.getCategory_id());
            if(category.getCategory_level() != CategoryLevelEnum.LEVEL_TWO){
                throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.INVALID_FIELD,null);
            }
            product.setCategory(category);
        }
        product.setUpdated_at(LocalDateTime.now());
        productService.save(product);
        ResponseStatusDTO status = new ResponseStatusDTO("Product updated successfully.", HttpStatus.OK.value(), System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status, HttpStatus.OK);
        return responseEntity;
    }

    @PatchMapping("/product/vendor-status/{id}")
    public ResponseEntity<ResponseStatusDTO> changeVendorStatus(@PathVariable Long id){
        String login = SecurityUtils.getCurrentUserLogin().get();
        User user = userService.getUserByEmail(login).get();
        Product product = productService.getById(id);
        if(product == null){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        if(!product.getUser().getEmail().equalsIgnoreCase(user.getEmail())){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        product.setVendor_show_status(!product.getVendor_show_status());
        productService.save(product);
        ResponseStatusDTO status = new ResponseStatusDTO("Product status changed.", HttpStatus.OK.value(), System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status, HttpStatus.OK);
        return responseEntity;
    }

    @PatchMapping("/product/admin-status/{id}")
    public ResponseEntity<ResponseStatusDTO> changeAdminStatus(@PathVariable Long id){
        String login = SecurityUtils.getCurrentUserLogin().get();
        User user = userService.getUserByEmail(login).get();
        Product product = productService.getById(id);
        if(product == null){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        product.setAdmin_show_status(!product.getAdmin_show_status());
        productService.save(product);
        ResponseStatusDTO status = new ResponseStatusDTO("Product status changed.", HttpStatus.OK.value(), System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status, HttpStatus.OK);
        return responseEntity;
    }

    @PatchMapping("/product/sponsored/{id}")
    public ResponseEntity<ResponseStatus> sponsoredStatus(@PathVariable Long id){
        String login = SecurityUtils.getCurrentUserLogin().get();
        User user = userService.getUserByEmail(login).get();
        Product product = productService.getById(id);
        if(product == null){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        product.setIs_sponsored(!product.getIs_sponsored());
        productService.save(product);
        ResponseStatusDTO status = new ResponseStatusDTO("Product sponsored status changed.", HttpStatus.OK.value(), System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status, HttpStatus.OK);
        return responseEntity;
    }

    @PatchMapping("/product/new-arrival/{id}")
    public ResponseEntity<ResponseStatus> newArrivalStatus(@PathVariable Long id){
        String login = SecurityUtils.getCurrentUserLogin().get();
        User user = userService.getUserByEmail(login).get();
        Product product = productService.getById(id);
        if(product == null){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        product.setIs_new_arrival(!product.getIs_new_arrival());
        productService.save(product);
        ResponseStatusDTO status = new ResponseStatusDTO("Product new arrival status changed.", HttpStatus.OK.value(), System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status, HttpStatus.OK);
        return responseEntity;
    }

}
