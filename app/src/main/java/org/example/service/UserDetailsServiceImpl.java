package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.entities.UserInfo;
import org.example.entities.UserRole;
import org.example.eventProducer.UserInfoProducer;
import org.example.models.UserInfoDto;
import org.example.repository.UserInfoRepository;
import org.example.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoProducer userInfoProducer;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       UserInfo user =  userInfoRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfo){
        return userInfoRepository.findByUsername(userInfo.getUsername())
                .orElse(null);
    }

    public Boolean SignUpUser(UserInfoDto userDto){
        if(ValidationUtil.validateUser(userDto)){
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            if(checkIfUserAlreadyExists(userDto) != null){
                return false;
            }
             UserInfo userInfo = userInfoRepository.save(new UserInfo(UUID.randomUUID().toString(),
                    userDto.getUsername(), userDto.getPassword(), new HashSet<UserRole>()));

            userDto.setUserId(userInfo.getUserId());
            //pushEventToQueue
            userInfoProducer.sendEventToKafka(userDto);
            return true;
        }
        return false;

    }
}
