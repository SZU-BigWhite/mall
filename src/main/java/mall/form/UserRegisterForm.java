package mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRegisterForm {

    //@NotEmpty用于list
    //@NotNull 对象null
    @NotBlank   //用于String判断空格
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
}
