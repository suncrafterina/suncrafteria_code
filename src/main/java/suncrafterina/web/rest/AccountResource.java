package suncrafterina.web.rest;

import org.zalando.problem.Status;
import springfox.documentation.annotations.ApiIgnore;
import suncrafterina.domain.User;
import suncrafterina.repository.UserRepository;
import suncrafterina.security.SecurityUtils;
import suncrafterina.service.MailService;
import suncrafterina.service.ResetPassword;
import suncrafterina.service.UserService;
import suncrafterina.service.dto.*;
import suncrafterina.web.rest.errors.*;
import suncrafterina.web.rest.vm.KeyAndPasswordVM;
import suncrafterina.web.rest.vm.ManagedUserVM;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiIgnore
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
   /*     if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }

    */
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        mailService.sendActivationEmail(user);
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    @ApiIgnore
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    @ApiIgnore
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    @ApiIgnore
    public UserDTO getAccount() {
        return userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    @ApiIgnore
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
            userDTO.getLangKey(), userDTO.getImageUrl());
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    @ApiIgnore
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    @ApiIgnore
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail '{}'", mail);
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    @ApiIgnore
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user =
            userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    @PostMapping("/register-user")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Object> registerUserAccount(@Valid @RequestBody RegisterUserDto registerUserDto) {
        User user = userService.registerNewUser(registerUserDto);
        mailService.sendActivationEmail(user);
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("email",user.getEmail());
        return data;
    }

    /**
     *{@code POST  /verify-code} : verify the email of registered user.
     * @param verificationCode verification code
     * @throws CustomException VerificationCodeNotMatchedException {@code 400(Bad Request)} if the verification code not matched
     */
    @PostMapping("/verify-code")
    public  Map<String,String> verifyCode(@Valid @RequestBody VerificationCode verificationCode){
        System.out.println("================================");
        Optional<User> user = userService.verification(verificationCode.getVerification_code());
        System.out.println(user);
        if(!user.isPresent())
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.VERIFICATION_CODE_NOT_MATCHED,null);
        Map<String,String> data = new LinkedHashMap<>();
        data.put("message","Email Verified");
        return data;
    }

    /**
     * {@code POST  /resend-verification} : resend the verification code.
     * @param emailDto email of the registered user.
     *@return email & verification code
     */
    @PostMapping("/resend-verification")
    public Map<String,Object> resendVerificationCode(@Valid @RequestBody EmailDto emailDto){
        Optional<User> user = userService.resendVerificationCode(emailDto.getEmail());
        Map<String,Object> data = new LinkedHashMap<>();
        if(user.isPresent()){
            mailService.sendActivationEmail(user.get());
            data.put("email",user.get().getEmail());
            data.put("message","6-digit verification code is resent on your Registered Email Address");
            return data;
        }
        else
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.EMAIL_NOT_FOUND,null);
    }

    @PostMapping("/forgot-password")
    public Map<String,String> forgotPassword(@Valid @RequestBody EmailDto emailDto){
        Optional<User> user = userService.requestPasswordReset(emailDto.getEmail().trim());
        if(user.isPresent()){
            mailService.sendPasswordResetMail(user.get());
            Map<String,String> data = new LinkedHashMap<>();
            data.put("email",emailDto.getEmail());
            data.put("message","6-digit OTP is sent on your Registered Email Address");
            return data;
        }else {
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.EMAIL_NOT_FOUND,null);
        }
    }

    @PostMapping("/resend-code")
    public Map<String,String> resendCode(@Valid @RequestBody EmailDto emailDto){
        Optional<User> user = userService.requestPasswordReset(emailDto.getEmail().trim());
        if(user.isPresent()){
            mailService.sendPasswordResetMail(user.get());
            Map<String,String> data = new LinkedHashMap<>();
            data.put("email",emailDto.getEmail());
            data.put("message","6-digit OTP is resent on your Registered Email Address");
            return data;
        }else {
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.EMAIL_NOT_FOUND,null);
        }
    }

    @PostMapping("/reset-password")
    public Map<String,String> resetCode(@Valid @RequestBody ResetPassword resetPassword) {
        Optional<User> user = userService.resetPassword(resetPassword.getNew_password(),resetPassword.getOtp());
        if (!user.isPresent()) {
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.INCORRECT_OTP,null);
        }
        Map<String,String> data = new LinkedHashMap<>();
        data.put("message","Password Reset Successfully");
        return data;
    }


}
