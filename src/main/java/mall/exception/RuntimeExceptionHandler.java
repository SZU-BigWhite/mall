package mall.exception;

import mall.enums.ResponseEnum;
import mall.vo.ResponseVo;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RuntimeExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseVo handle(RuntimeException e){
        return ResponseVo.error(ResponseEnum.ERROR,e.getMessage());
    }
    //TODO 重写异常返回值
    //登录异常
    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo rserLoginException(){
        return ResponseVo.error(ResponseEnum.NEED_LOGIN);
    }
    //参数异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo notValidException(MethodArgumentNotValidException e){
        FieldError fieldError = e.getBindingResult().getFieldError();
        return ResponseVo.error(ResponseEnum.PARAM_ERROR,fieldError.getField()+" "+fieldError.getDefaultMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseVo missingServletRequestParameterException(MissingServletRequestParameterException e){
        return ResponseVo.error(ResponseEnum.PARAM_ERROR,e.getParameterName()+" "+e.getMessage());
    }
}
