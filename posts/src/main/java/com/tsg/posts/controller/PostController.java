package com.tsg.posts.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tsg.commons.exception.NotFoundException;
import com.tsg.commons.models.enums.GenericPage;
import com.tsg.commons.models.enums.ResponseDTO;
import com.tsg.posts.models.request.PostDTO;
import com.tsg.posts.models.response.PostDetailsResponse;
import com.tsg.posts.models.response.PostResponse;
import com.tsg.posts.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/v1")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create-post")
    public ResponseEntity<PostDetailsResponse> createPost(@RequestHeader("user-id") String authHeader,
            @RequestBody  PostDTO postRequest) {
                System.out.println("body: " + postRequest.toString());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createPost(Long.valueOf(authHeader), postRequest));
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<PostResponse>> getAllUsers(
            @RequestParam(value = "page-number") int pageNumber,
            @RequestParam(value = "page-size") int pageSize,
            @RequestParam(value = "sort-direction") String sortDirection) {
        try {
            GenericPage page = new GenericPage();
            page.setPageNumber(pageNumber);
            page.setPageSize(pageSize);
            page.setSortDirection(Sort.Direction.valueOf(sortDirection));
            Page<PostResponse> pageReturn = postService.findAllWithFiltersPageable(page);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(pageReturn);
        } catch (Exception ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    @GetMapping("/get-id")
    public ResponseEntity<PostDetailsResponse> getPostById(
            @RequestParam Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PutMapping("/update-post")
    public ResponseEntity<PostDTO> updatePost(
            @RequestParam Long id, @RequestBody @Valid PostDTO postRequest) {
        return ResponseEntity.ok(postService.updatePost(id, postRequest));
    }

    @DeleteMapping("/delete-post")
    public ResponseEntity<ResponseDTO> deletePost(
            @RequestParam Long id) {
        postService.deletePost(id);
        ResponseDTO response = ResponseDTO.builder().httpStatusCode(HttpStatus.ACCEPTED.value()).timestamp(LocalDateTime.now())
                        .description(Collections.singletonList("Se ha eliminado con exito.")).build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
