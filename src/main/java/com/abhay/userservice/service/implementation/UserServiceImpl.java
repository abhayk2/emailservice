package com.abhay.userservice.service.implementation;

import com.abhay.userservice.domain.Confirmation;
import com.abhay.userservice.domain.User;
import com.abhay.userservice.exception.EmailExistsException;
import com.abhay.userservice.repository.ConfirmationRepository;
import com.abhay.userservice.repository.UserRepository;
import com.abhay.userservice.service.EmailService;
import com.abhay.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    @Override
    public User saveUser(User user, String emailType) {

        if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailExistsException("Email '" + user.getEmail() + "' already exists.");
        }
        String effectiveEmailType = StringUtils.hasText(emailType) ? emailType : "simpleEmail";
        user.setIsEnabled(false);
        Confirmation confirmation = new Confirmation(user);

        switch (effectiveEmailType) {
            case "simpleEmail":
                emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), confirmation.getToken());
                break;
            case "emailWithAttachment":
                emailService.sendMimeMessageWithAttachment(user.getName(), user.getEmail(), confirmation.getToken());
                break;
            case "emailWithEmbeddedImages":
                emailService.sendMimeMessageWithEmbeddedImages(user.getName(), user.getEmail(), confirmation.getToken());
                break;
            case "HTMLEmail":
                emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());
                break;
            case "htmlEmailWithAttachment":
                emailService.sendHtmlEmailWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
                break;
            default:
                throw new IllegalArgumentException("The provided emailType '" + emailType + "' is not supported.");
        }

        userRepository.save(user);
        confirmationRepository.save(confirmation);

        return user;
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setIsEnabled(true);
        userRepository.save(user);


        return Boolean.TRUE;
    }
}
