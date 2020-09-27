package suncrafterina.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zalando.problem.Status;
import suncrafterina.domain.User;
import suncrafterina.enums.Currency;
import suncrafterina.repository.UserRepository;
import suncrafterina.security.jwt.JWTFilter;
import suncrafterina.security.jwt.TokenProvider;
import suncrafterina.service.UserService;
import suncrafterina.service.dto.LoginProfileDTO;
import suncrafterina.web.rest.errors.CustomException;
import suncrafterina.web.rest.errors.SunCraftStatusCode;
import suncrafterina.web.rest.vm.LoginVM;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginProfileDTO> authorize(@Valid @RequestBody LoginVM loginVM) {
        User user = userService.checkUsernamePassword(loginVM);
        if(user == null)
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.USERNAME_PASSWORD_INVALID,null);
        else{
            if(!user.getActivated())
                throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.EMAIL_NOT_VERIFIED,null);
            if (!passwordEncoder.matches(loginVM.getPassword(),user.getPassword())){
                throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.USERNAME_PASSWORD_INVALID,null);
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        LoginProfileDTO loginProfileDTO = new LoginProfileDTO();
        loginProfileDTO.setUser_id(user.getId());
        loginProfileDTO.setEmail(user.getEmail());
        loginProfileDTO.setFirst_name(user.getFirstName());
        loginProfileDTO.setLang(user.getLangKey());
        loginProfileDTO.setLast_name(user.getLastName());
        loginProfileDTO.setImage_file(user.getImageUrl());
        loginProfileDTO.setProfile_status(false);
        if(user.getCurrency().equalsIgnoreCase("USD"))
            loginProfileDTO.setCurrency(Currency.USD);
        else if(user.getCurrency().equalsIgnoreCase("INR"))
            loginProfileDTO.setCurrency(Currency.INR);
        else
            loginProfileDTO.setCurrency(Currency.EURO);
        String role = userService.getRole(user);
        if( role.equalsIgnoreCase("ROLE_SUB_ADMIN")){
            role = "admin";
        }else if (role.equalsIgnoreCase("ROLE_FACTORY_VENDOR")) {
            role = "factory";
        }else if(role.equalsIgnoreCase("ROLE_CUSTOMER")){
            role = "customer";
        }else{
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.USERNAME_PASSWORD_INVALID,null);
        }
        loginProfileDTO.setToken(jwt);
        loginProfileDTO.setRole(role);
        return new ResponseEntity<>(loginProfileDTO,HttpStatus.OK);

        //return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }
    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
