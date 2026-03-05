package org.example.projecttraining.Controller;

import org.apache.coyote.Response;
import org.example.projecttraining.Model.Concert;
import org.example.projecttraining.Model.User;
import org.example.projecttraining.Repository.ConcertRepository;
import org.example.projecttraining.Repository.UserRepository;
import org.example.projecttraining.Security.TokenGenerator;
import org.example.projecttraining.Service.concertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserAPIController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private concertService concertService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User Registered Successfully");
    }
        @PostMapping("/login")
                public ResponseEntity<?> login(@RequestBody User user) {
            String token = tokenGenerator.generateToken(user.getEmail(), user.getPassword());
            User role=userRepository.findByEmail(user.getEmail());
            if (token != null) {
                return ResponseEntity.ok(Map.of("token", token ,"role", role.getRole()));
            }
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        @GetMapping("/user")
        public  ResponseEntity<?> alluser(){
            return  ResponseEntity.ok(Map.of("user",userRepository.findAll()));
        }

        @PostMapping("/add_concert")
        public ResponseEntity<?> concertCreation(@RequestBody Concert concert){
            concertRepository.save(concert);
            return ResponseEntity.ok("Concert Added Successfully - "+concert.getConcertName());
        }

        @GetMapping("/concerts")
        public ResponseEntity<?> viewConcert(){
        return ResponseEntity.ok(Map.of("concert", concertRepository.findAll()));
        }

        @PostMapping("/editConcert/{id}")
        public ResponseEntity<?> editConcert(@RequestBody Concert concert, @PathVariable Integer id){
        return concertService.updateConcert(id, concert)
                .map(updated -> ResponseEntity.ok(Map.of("message", "Updated the concert Successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Concert with ID "+id+" not found")));
        }
    }