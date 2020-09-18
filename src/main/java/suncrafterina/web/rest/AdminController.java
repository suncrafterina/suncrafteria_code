package suncrafterina.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Status;
import suncrafterina.domain.User;
import suncrafterina.security.AuthoritiesConstants;
import suncrafterina.security.SecurityUtils;
import suncrafterina.service.AmazonClient;
import suncrafterina.service.UserService;
import suncrafterina.service.dto.*;
import suncrafterina.web.rest.errors.CustomException;
import suncrafterina.web.rest.errors.SunCraftStatusCode;

import javax.validation.Valid;
import java.math.BigInteger;
import java.time.Year;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.SUB_ADMIN + "\")")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AmazonClient amazonClient;

//    @Autowired
//    private OrderService orderService;

    @GetMapping("/profile")
    public ResponseEntity<AdminProfileDto> getAdminProfile(){
        Optional<String> optional = SecurityUtils.getCurrentUserLogin();
        if(!optional.isPresent())
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.BAD_CREDENTIALS,null);
        User user = userService.getUserByEmail(optional.get()).get();
        AdminProfileDto adminProfileDto = new AdminProfileDto();
        adminProfileDto.setEmail(user.getEmail());
        adminProfileDto.setFirst_name(user.getFirstName());
        adminProfileDto.setLast_name(user.getLastName());
        adminProfileDto.setImage_url(user.getImageUrl());
        adminProfileDto.setUser_id(user.getId());
        return new ResponseEntity<>(adminProfileDto, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<ResponseStatusDTO> updateAdminProfile(@Valid @ModelAttribute AdminProfileFormDTO adminProfileDto){
        Optional<String> optional = SecurityUtils.getCurrentUserLogin();
        if(!optional.isPresent())
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.BAD_CREDENTIALS,null);
        User user = userService.getUserByEmail(optional.get()).get();
        user.setFirstName(adminProfileDto.getFirst_name().trim());
        user.setLastName(adminProfileDto.getLast_name().trim());
        if(adminProfileDto.getImage_url()!=null) {
            String imageFile = this.amazonClient.uploadFile(adminProfileDto.getImage_url(),"suncraft_admin");
            if(user.getImageUrl()!=null){
                this.amazonClient.deleteFileFromS3Bucket(user.getImageUrl(),"hytx_admin");
            }
            user.setImageUrl(imageFile);
        }
        userService.saveUser(user);
        return new ResponseEntity<>(new ResponseStatusDTO("Profile updated",HttpStatus.OK.value(),System.currentTimeMillis()), HttpStatus.OK);
    }

    // DashBoard Api's

    @GetMapping("/count-list")
    public ResponseEntity<Map<String,Object>> getCountList(){
        Map<String,Object> data = new LinkedHashMap<>();

  //      BigInteger total_customer = userService.getCountOfCustomer();
        data.put("total_customer",0);

    //    BigInteger total_factory_vendor = userService.getCountOfFactoryVendor();
        data.put("total_factory_vendor",0);

      //  BigInteger total_warehouse = userService.getCountOfWarehouseVendor();
        data.put("total_warehouse",0);

        //BigInteger total_shipper = userService.getCountOfShippingVendor();
        data.put("total_shipper",0);

        data.put("total_agent",0);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/sale-graph/{year}")
    public ResponseEntity<SaleGraphDto> getSaleGraph(@PathVariable("year")  @DateTimeFormat(pattern = "yyyy") Year year){
        //SaleGraphDto saleGraphDto=this.orderService.getSaleGraphData(year);
        SaleGraphDto saleGraphDto =new SaleGraphDto();
        List<String> labels = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        List<Long> data =  new ArrayList<>();
        data.add((long) 1);
        data.add((long) 2);
        data.add((long) 3);
        data.add((long) 4);
        data.add((long) 5);
        data.add((long) 6);
        data.add((long) 7);
        data.add((long) 8);
        data.add((long) 9);
        data.add((long) 10);
        data.add((long) 11);
        data.add((long) 12);
        saleGraphDto.setData(data);
        saleGraphDto.setLabels(labels);
        return new ResponseEntity<>(saleGraphDto, HttpStatus.OK);
    }

    @GetMapping("/top-selling-products")
    public ResponseEntity<List<DashboardTopProductsDto>> getTopSellingProducts(){
       // return new ResponseEntity<>(orderService.getTopSellingProducts(),HttpStatus.OK);
        return new ResponseEntity<>(new LinkedList<DashboardTopProductsDto>(),HttpStatus.OK);
    }

    @GetMapping("/top-customers")
    public ResponseEntity<List<DashboardTopCustomersDto>> getTopCustomers(){
       // return new ResponseEntity<>(orderService.getTopCustomer(),HttpStatus.OK);
        return new ResponseEntity<>(new LinkedList<DashboardTopCustomersDto>(),HttpStatus.OK);
    }


}
