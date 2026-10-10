package az.ingress.e_commerce.dto;

import az.ingress.e_commerce.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = " Email cannot be blank ")
    @Email(message = " Invalid email format ")
    private String email;

    @NotBlank(message = " Password cannot be blank ")
    private String password;

    @NotNull(message = " Role cannot be null ")
    private Role role;
}
