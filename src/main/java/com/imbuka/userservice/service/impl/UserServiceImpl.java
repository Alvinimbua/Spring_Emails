package com.imbuka.userservice.service.impl;

import com.imbuka.userservice.domain.Confirmation;
import com.imbuka.userservice.domain.User;
import com.imbuka.userservice.repository.ConfirmationRepository;
import com.imbuka.userservice.repository.UserRepository;
import com.imbuka.userservice.service.EmailService;
import com.imbuka.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;

    private final EmailService emailService;

    @Override
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        //disable the user
        user.setEnabled(false);
        userRepository.save(user);

        //create a new confirmation for the user
        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

        /* TODO Send email to user with Tokens */
        //emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), confirmation.getToken());
        //emailService.sendMimeMessageWithAttachments(user.getName(), user.getEmail(), confirmation.getToken());
        //emailService.sendMimeMessageWithAttachmentsFiles(user.getName(), user.getEmail(), confirmation.getToken());
       // emailService.sendHtmlEmail(user.getName(),user.getEmail(), confirmation.getToken());
        emailService.sendHtmlEmailwithEmbeddedFiles(user.getName(),user.getEmail(), confirmation.getToken());
        return user;
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        //enable the user
        user.setEnabled(true);
        userRepository.save(user);
        return Boolean.TRUE;
    }
}
