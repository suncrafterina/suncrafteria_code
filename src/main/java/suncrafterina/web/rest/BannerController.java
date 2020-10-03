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
import suncrafterina.domain.Banner;
import suncrafterina.domain.Product;
import suncrafterina.security.AuthoritiesConstants;
import suncrafterina.service.AmazonClient;
import suncrafterina.service.HelperService;
import suncrafterina.service.dto.*;
import suncrafterina.service.impl.BannerService;
import suncrafterina.service.impl.ProductService;
import suncrafterina.web.rest.errors.CustomException;
import suncrafterina.web.rest.errors.SunCraftStatusCode;

import javax.validation.Valid;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.SUB_ADMIN + "\")")
public class BannerController {

    @Autowired
    BannerService bannerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private HelperService helperService;

    @Autowired
    MessageSource messageSource;

    private AmazonClient amazonClient;

    @Autowired
    public BannerController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @GetMapping("/banner")
    public ResponseEntity<List<BannerListDTO>> getAllBanner(@RequestParam(required = false) String search, Pageable pageable){
        if(search == null)
            search="_";

        List<BannerListDTO> banners = new LinkedList<>();
        Page<Object[]> page = bannerService.getAllBannerList(search,pageable);
        for(Object[] obj : page){
            BigInteger id = (BigInteger) obj[0];
            String name = (String) obj[1];
            BigInteger type_id = (BigInteger) obj[2];
            String image_file = (String) obj[3];
            String image_file_thumb = (String) obj[4];
            Boolean is_active = (Boolean) obj[5];
            Timestamp created_at = (Timestamp) obj[6];
            banners.add(new BannerListDTO(id,type_id.longValue(),
                name,image_file,image_file_thumb,is_active,
                created_at.toLocalDateTime().toLocalDate()));

        }
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(banners);
    }

    @GetMapping("/banner/{id}")
    public ResponseEntity<BannerDetailDTO> getBannerDetail(@PathVariable Long id){
        Banner banner = bannerService.findBannerById(id);
        if(banner==null){
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.NOT_FOUND,null);
        }
        BannerDetailDTO bannerDetailDTO = null;
            bannerDetailDTO = new BannerDetailDTO(banner.getId(),
                banner.getProduct().getId(),banner.getProduct().getName(),banner.getImage_file(),
                banner.getImage_file_thumb(),banner.getIs_active());
        return new ResponseEntity<>(bannerDetailDTO,HttpStatus.OK);
    }

    @PostMapping("/banner")
    public ResponseEntity<ResponseStatus> addBanner(@Valid @ModelAttribute BannerDTO bannerDTO, Locale locale){

        Banner banner = new Banner();
        Product product = productService.getById(bannerDTO.getType_id());
        if(product==null)
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.NOT_FOUND,null);
        banner.setProduct(product);
        if(bannerDTO.getImage_file()!=null) {
            String image_file = this.amazonClient.uploadFile(bannerDTO.getImage_file(),"suncraft_banner");
            banner.setImage_file(image_file);
            MultipartFile multipartFile=null;
            try{
                multipartFile = helperService.createBannerThumbnail(bannerDTO.getImage_file());
                String thumbnail= this.amazonClient.uploadFile(multipartFile,"suncraft_banner");
                banner.setImage_file_thumb(thumbnail);
            }catch(Exception ex){
                throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.IMAGE_NOT_UPLOADED, null);
            }
        }
        banner.setIs_active(true);
        banner.setCreated_at(LocalDateTime.now());
        banner = bannerService.saveBanner(banner);
        ResponseStatusDTO status = new ResponseStatusDTO("New banner added.", HttpStatus.CREATED.value(),System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status,HttpStatus.CREATED);
        return responseEntity;
    }

    @PutMapping("/banner")
    public ResponseEntity<ResponseStatus> updateBannerDetail(@Valid @ModelAttribute BannerUpdateDTO bannerDTO, Locale locale){
        Banner banner = bannerService.findBannerById(bannerDTO.getId());
        if(banner==null){
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.NOT_FOUND,null);
        }
        Product product = productService.getById(bannerDTO.getType_id());
        if(product.getId() != banner.getProduct().getId())
            banner.setProduct(product);

        if(bannerDTO.getImage_file()!=null) {
            String image_file = this.amazonClient.uploadFile(bannerDTO.getImage_file(),"suncraft_banner");
            if(banner.getImage_file()!=null){
                this.amazonClient.deleteFileFromS3Bucket(banner.getImage_file(),"suncraft_banner");
            }
            banner.setImage_file(image_file);
            MultipartFile multipartFile=null;
            try{
                multipartFile = helperService.createBannerThumbnail(bannerDTO.getImage_file());
                String thumbnail= this.amazonClient.uploadFile(multipartFile,"suncraft_banner");
                if(banner.getImage_file_thumb()!=null){
                    this.amazonClient.deleteFileFromS3Bucket(banner.getImage_file_thumb(),"suncraft_banner");
                }
                banner.setImage_file_thumb(thumbnail);
            }catch(Exception ex){
                throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.IMAGE_NOT_UPLOADED, null);
            }
        }
        banner.setUpdated_at(LocalDateTime.now());
        bannerService.saveBanner(banner);
        ResponseStatusDTO status = new ResponseStatusDTO("Banner updated.", HttpStatus.CREATED.value(),System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status,HttpStatus.CREATED);
        return responseEntity;
    }

    @GetMapping("/search/{search}")
    public ResponseEntity<List<SearchDTO>> getSearchResult(@PathVariable String search){
        List<SearchDTO> searchDTOs = new LinkedList<>();
        if(search.length()>2){
            searchDTOs = productService.searchByProductName(search);
        }
        return new ResponseEntity<>(searchDTOs,HttpStatus.OK);
    }

    @DeleteMapping("/banner/{id}")
    public ResponseEntity<ResponseStatus> deleteBanner(@PathVariable Long id,Locale locale){
        Banner banner = bannerService.findBannerById(id);
        if(banner==null){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        bannerService.deleteBanner(banner);
        ResponseStatusDTO status = new ResponseStatusDTO("Banner deleted.", HttpStatus.CREATED.value(),System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status,HttpStatus.CREATED);
        return responseEntity;
    }

    @PatchMapping("/banner/change-status/{id}")
    public ResponseEntity<ResponseStatus> updateBannerStatus(@PathVariable Long id){
        Banner banner = bannerService.findBannerById(id);
        if(banner==null){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        banner.setIs_active(!banner.getIs_active());
        bannerService.saveBanner(banner);
        ResponseStatusDTO status1 = new ResponseStatusDTO("Banner status updated.", HttpStatus.CREATED.value(),System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status1,HttpStatus.CREATED);
        return responseEntity;
    }
}
