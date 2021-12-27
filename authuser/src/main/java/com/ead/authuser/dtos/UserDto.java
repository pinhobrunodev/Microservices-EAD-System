package com.ead.authuser.dtos;

import com.ead.authuser.validation.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    // Custom Views for Each Endpoint
    public interface UserView {
        public static interface RegistrationPost {
        }

        public static interface UserPut {
        }

        public static interface PasswordPut {
        }

        public static interface ImagePut {
        }
    }

    @NotBlank(groups = UserView.RegistrationPost.class,message = "Mandatory field")
    @Size(min = 4,max = 50,groups = UserView.RegistrationPost.class,message = "Username must be between 4 and 50 characters")
    @UsernameConstraint(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String username;

    @NotBlank(groups = UserView.RegistrationPost.class,message = "Mandatory field")
    @JsonView(UserView.RegistrationPost.class)
    @Email(groups = UserView.RegistrationPost.class,message = "Insert a valid E-mail.")
    private String email;

    @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class},message = "Mandatory field")
    @Size(min = 6,max = 20,groups = {UserView.RegistrationPost.class,UserView.PasswordPut.class},message = "Password must be between 6 and 20 characters")
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @ToString.Exclude // Exclude the password of toString() of @Data
    private String password;

    @NotBlank(groups = UserView.PasswordPut.class,message = "Mandatory field")
    @Size(min = 6,max = 20,groups = UserView.PasswordPut.class,message = "Password must be between 6 and 20 characters")
    @JsonView(UserView.PasswordPut.class)
    @ToString.Exclude
    private String oldPassword;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String cpf;

    @NotBlank(groups = UserView.ImagePut.class,message = "Mandatory field")
    @JsonView({UserView.ImagePut.class})
    private String imageUrl;


}
