package com.abhay.userservice.service.implementation;

import com.abhay.userservice.domain.Confirmation;
import com.abhay.userservice.domain.User;
import com.abhay.userservice.repository.ConfirmationRepository;
import com.abhay.userservice.repository.UserRepository;
import com.abhay.userservice.service.EmailService;
import com.abhay.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;
    /**
     * @param user
     * @return
     */
    @Override
    public User saveUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exists");
        }
            user.setIsEnabled(false);
            userRepository.save(user);
            Confirmation confirmation = new Confirmation(user);
            confirmationRepository.save(confirmation);
            //Sending Email
//            emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), confirmation.getToken());
//              emailService.sendMimeMessageWithAttachment(user.getName(), user.getEmail(), confirmation.getToken());
//              emailService.sendMimeMessageWithEmbeddedImages(user.getName(), user.getEmail(), confirmation.getToken());
//              emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());
              emailService.sendHtmlEmailWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
        return user;
    }

    /**
     * @param token
     * @return
     */
    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setIsEnabled(true);
        userRepository.save(user);


        return Boolean.TRUE;
    }
}
