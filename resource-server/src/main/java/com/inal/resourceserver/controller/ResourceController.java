package com.inal.resourceserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inal.resourceserver.domain.entity.Notes;
import com.inal.resourceserver.domain.repository.NotesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private static final int AUTHORIZATION_BEARER_LENGTH = 7;
    private final NotesRepository notesRepository;

    public ResourceController(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Notes>> get(final HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = authorizationHeader.substring(AUTHORIZATION_BEARER_LENGTH);
        Jwt jwt = JwtHelper.decode(token);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> claimsMap = objectMapper.readValue(jwt.getClaims(), new TypeReference<Map<String, Object>>() {
            });
            Long memberId = Long.valueOf(claimsMap.get("memberId").toString());
            List<Notes> notesList = notesRepository.findByMember_Id(memberId);
            return ResponseEntity.ok(notesList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("cannot.get.jwt.claims");
    }

}