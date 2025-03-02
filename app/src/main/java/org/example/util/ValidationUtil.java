package org.example.util;

import org.example.models.UserInfoDto;

public class ValidationUtil {

    public static boolean validateUser(UserInfoDto userDto){
        //check email and password
        //"^(?![_\\.])[a-zA-Z0-9._]{3,15}(?<![_\\.])$"
        if(userDto.getUsername().length() < 5){
            return false;
        }
        //String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
//        if(!userDto.getEmail().contains("@")){
//            return false;
//        }
       // String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        //!userDto.getPassword().matches(PASSWORD_PATTERN)
        if(userDto.getPassword().length() < 5){
            return false;
        }
        return true;
    }
}
