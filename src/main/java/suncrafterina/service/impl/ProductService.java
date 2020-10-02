package suncrafterina.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zalando.problem.Status;
import suncrafterina.domain.Product;
import suncrafterina.repository.CategoryRepository;
import suncrafterina.repository.ProductRepository;
import suncrafterina.service.HelperService;
import suncrafterina.service.UserService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

//    @Autowired
 //   private ProductUserFavouriteRepository productUserFavouriteRepository;

    @Autowired
    private CategoryRepository categoryRepository;

  //  @Autowired
  //  private ProductImageRepository productImageRepository;

    @Autowired
    private HelperService helperService;


    public Product save(Product product){
        return productRepository.save(product);
    }

    public Product getById(Long id){
        Optional<Product> product= productRepository.findById(id);
        if(product.isPresent()){
            return product.get();
        }
        return null;
    }

    public Product getByTitleAndUserId(String title,Long userId){
        Optional<Product> product= productRepository.findByTitle(title,userId);
        if(product.isPresent()){
            return product.get();
        }
        return null;
    }

    public Product getByTitleUserIdAndProductId(String title,Long userId,Long productId){
        Optional<Product> product= productRepository.findByTitleAndId(title,userId,productId);
        if(product.isPresent()){
            return product.get();
        }
        return null;
    }

    public Product getBySkuUserIdAndProductId(String sku,Long userId,Long productId){
        Optional<Product> product= productRepository.findBySkuAndId(sku,userId,productId);
        if(product.isPresent()){
            return product.get();
        }
        return null;
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Page<Object[]> getAllUserProduct(String search, Pageable pageable){
        return productRepository.findAllUserProduct(search,pageable);
    }

    public Page<Object[]> getAllProduct(Long userId, String search, Pageable pageable){
        return productRepository.findAllProduct(userId,search,pageable);
    }
/*
    public Double findRating(Long productId){
        List<Object[]> ratingObject = productRepository.findRating(productId);
        Double rating = 0.0D;
        if(ratingObject.size() > 0) {
            Object[] ratingDetails = ratingObject.get(0);
            BigInteger sumRating = (BigInteger) ratingDetails[1];
            BigInteger totalCount = (BigInteger) ratingDetails[2];
            rating=(Double) ratingDetails[3];
        }
        return round(rating,2);
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public BigInteger getCountOfProducts(Long user_id){
        return productRepository.getCountOfProducts(user_id);
    }

    // Currency exchange then add throw UnavailableExchangeRateException
    public ProductDetailDto getProductDetail(Long productId, Currency currency) {

        ProductDetailDto productDetailDto=new ProductDetailDto();
        Optional<Product> productOptional=productRepository.findById(productId);
        if(!productOptional.isPresent()){
            throw new CustomException(Status.BAD_REQUEST, HytxStatusCode.PRODUCT_NOT_FOUND, null);
        }
        Product product=productOptional.get();

        Optional<FactoryVendor> factoryVendorOptional=factoryVendorRepository.findByUserId(product.getUser().getId());
        FactoryVendor factoryVendor=factoryVendorOptional.get();

        String login = SecurityUtils.getCurrentUserLogin().get();
        User user = userService.getUserByLogin(login);
        Long customerId=null;
        Boolean is_fav=false;

        if(user!=null){
            customerId= user.getId();
        }

        if(customerId!=null) {
            Optional<ProductUserFavourite> productUserFavouriteOptional = productUserFavouriteRepository.findByProductAndUserID(productId, customerId);
            if(productUserFavouriteOptional.isPresent()){
                is_fav=true;
            }
        }

        BigDecimal exchange_rate= BigDecimal.valueOf(1.00);

        //if(!currency.equals(Currency.USD)) {
         //   exchange_rate = fxRateService.getFxConversion(Currency.USD.toString(), currency.toString(), LocalDate.now());
        //}

        productDetailDto.setId(productId);
        productDetailDto.setFactory_vendor(factoryVendor);
        productDetailDto.setName(product.getName());
        productDetailDto.setSlug(product.getSlug());
        productDetailDto.setDescription(product.getDescription());
        productDetailDto.setImage_file(product.getImage_file());
        productDetailDto.setImage_file_thumb(product.getImage_file_thumb());
        productDetailDto.setVideo_file(product.getVideo_file());
        productDetailDto.setMeta_tag_title(product.getMeta_tag_title());
        productDetailDto.setMeta_description(product.getMeta_description());
        productDetailDto.setMeta_tag_keyword(product.getMeta_tag_keyword());
        productDetailDto.setSku(product.getSku());
        productDetailDto.setManufacturer(product.getManufacturer());
        productDetailDto.setModel(product.getModel());
        BigDecimal price=BigDecimal.valueOf(product.getPrice());
        //if(!currency.equals(Currency.USD)){
        //    price= price.multiply(exchange_rate).setScale(2,BigDecimal.ROUND_HALF_DOWN);
       // }
        productDetailDto.setPrice(price);
        productDetailDto.setDimension(product.getDimension());
        productDetailDto.setLength_unit(product.getLength_unit());
        productDetailDto.setWeight(product.getWeight());
        productDetailDto.setWeight_unit(product.getWeight_unit());
        Optional<Category> category=categoryRepository.findById(product.getId());

        productDetailDto.setCategory(category.get());
        productDetailDto.setAdded_on(product.getCreated_at().toLocalDate());
        productDetailDto.setVendor_show_status(product.getVendor_show_status());
        productDetailDto.setAdmin_show_status(product.getAdmin_show_status());

        Set<AttributeValue> attributeValues=product.getAttribute_values();
        List<ProductOptionValue> productOptionValues = productOptionValueRepository.findAllByProductId(productId);
        List<ProductImage> productImages = productImageRepository.findAllByProductId(productId);
        List<Object[]> ratingObject=productRepository.findRating(productId);
        if(ratingObject.size() > 0) {
            Object[] ratingDetails = ratingObject.get(0);
            BigInteger sumRating = (BigInteger) ratingDetails[1];
            BigInteger totalCount = (BigInteger) ratingDetails[2];
            Double rating=(Double) ratingDetails[3];
            productDetailDto.setRating(helperService.round(rating, 2));
            productDetailDto.setCount_rating(totalCount.longValue());
        }


        Set<ProductAttributeDto> attributeDtos=new HashSet<>();
        Set<ProductOptionDto> optionDtos=new HashSet<>();

        Map<Long, List<AttributeValue>> productAttributeValueMap =
            attributeValues.stream().collect(Collectors.groupingBy(w -> w.getAttribute_id()));

        Map<Long, List<ProductOptionValue>> productOptionValueMap =
            productOptionValues.stream().collect(Collectors.groupingBy(w -> w.getOption_value().getOption_id()));


        for (Long key : productAttributeValueMap.keySet()) {
            List<AttributeValue> attributeValues1 = productAttributeValueMap.get(key);
            ProductAttributeDto productAttributeDto = new ProductAttributeDto();
            productAttributeDto.setId(key);
            Set<ProductAttributeValueDto> attributeValueDtos = new HashSet<>();
            for (AttributeValue attributeValue : attributeValues1) {
                Attribute attribute = attributeRepository.findOneById(attributeValue.getAttribute_id()).get();
                productAttributeDto.setTitle(attribute.getTitle());
                productAttributeDto.setSlug(attribute.getSlug());
                ProductAttributeValueDto productAttributeValueDto=new ProductAttributeValueDto(attributeValue.getId(),attributeValue.getValue());
                attributeValueDtos.add(productAttributeValueDto);
            }
            productAttributeDto.setValues(attributeValueDtos);
            attributeDtos.add(productAttributeDto);
        }

        for (Long key : productOptionValueMap.keySet()) {
            List<ProductOptionValue> productOptionValues1 = productOptionValueMap.get(key);
            ProductOptionDto productOptionDto = new ProductOptionDto();
            productOptionDto.setId(key);
            Set<ProductOptionValueDto> optionValueDtos = new HashSet<>();
            for (ProductOptionValue productOptionValue : productOptionValues1) {
                Option option = optionRepository.findOneById(productOptionValue.getOption_value().getOption_id()).get();
                productOptionDto.setTitle(option.getTitle());
                BigDecimal additional_cost=BigDecimal.valueOf(productOptionValue.getAdditional_cost());
                if(!currency.equals(Currency.USD)){
                    additional_cost= additional_cost.multiply(exchange_rate).setScale(2,BigDecimal.ROUND_HALF_DOWN);
                }
                ProductOptionValueDto productOptionValueDto=new ProductOptionValueDto(productOptionValue.getOption_value().getId(),productOptionValue.getOption_value().getValue(),additional_cost);
                optionValueDtos.add(productOptionValueDto);
            }
            productOptionDto.setValues(optionValueDtos);
            optionDtos.add(productOptionDto);
        }

        productDetailDto.setAttributes(attributeDtos);
        productDetailDto.setOptions(optionDtos);
        productDetailDto.setTags(product.getTags());
        productDetailDto.setAdditional_images(productImages);
        productDetailDto.setIs_favourite(is_fav);

        return productDetailDto;
    }


  */
}
