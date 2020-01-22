package org.hunter.userrequestlogeventingservice.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hunter.userrequestlogeventingservice.repository.UserRequestLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hunter.model.UserRequestLogView;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/userrequestloghistory")
public class UserRequestLogEventingServiceController {

    @Autowired
    private UserRequestLogRepository userRequestLogRepository;

    @GetMapping(path = "/{id}", produces = "application/json")
    public List<UserRequestLogView> getUserHistory(@PathVariable UUID id) {
        try {
			return userRequestLogRepository
					.findByUserId(id).stream().map(log -> new UserRequestLogView(log.getUserId(),
							log.getMaxPaymentAmount().divide(BigDecimal.valueOf(100)), log.getCreated(), log.getUpdated()))
					.collect(Collectors.toList());
        }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

}
