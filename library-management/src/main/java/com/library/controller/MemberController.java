package com.library.controller;

import com.library.entity.Member;
import com.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllMembers() {
        List<Map<String, Object>> members = memberRepository.findAll().stream()
                .map(this::toMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(members);
    }

    private Map<String, Object> toMap(Member member) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", member.getId());
        map.put("username", member.getUsername());
        map.put("fullName", member.getFullName());
        map.put("email", member.getEmail());
        map.put("maxBorrowLimit", member.getMaxBorrowLimit());
        map.put("currentBorrowCount", member.getCurrentBorrowCount());
        return map;
    }
}
