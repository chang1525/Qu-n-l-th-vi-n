package com.library.controller;

import com.library.entity.Member;
import com.library.entity.Role;
import com.library.entity.User;
import com.library.repository.MemberRepository;
import com.library.repository.UserRepository;
import com.library.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


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

    @PostMapping
    public ResponseEntity<String> addMember(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Tên đăng nhập đã tồn tại!\"}");
        }

        Member member = new Member();
        member.setUsername(request.getUsername());
        // Sử dụng mật khẩu mặc định nếu không truyền lên
        String rawPassword = request.getPassword() != null && !request.getPassword().isEmpty() ? request.getPassword() : "123456";
        member.setPassword(passwordEncoder.encode(rawPassword));
        member.setFullName(request.getFullName());
        member.setEmail(request.getEmail());
        member.setRole(Role.MEMBER);
        member.setMaxBorrowLimit(5);
        member.setCurrentBorrowCount(0);

        memberRepository.save(member);

        return ResponseEntity.ok("{\"message\": \"Thêm độc giả thành công!\"}");
    }
}
