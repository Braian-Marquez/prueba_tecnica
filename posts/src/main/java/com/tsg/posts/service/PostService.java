package com.tsg.posts.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.tsg.commons.exception.NotFoundException;
import com.tsg.commons.models.entity.Customer;
import com.tsg.commons.models.entity.Post;
import com.tsg.commons.models.enums.GenericPage;
import com.tsg.commons.models.enums.PostStatus;
import com.tsg.posts.models.mapper.GenericMapper;
import com.tsg.posts.models.repository.CustomerRepository;
import com.tsg.posts.models.repository.PostRepository;
import com.tsg.posts.models.request.PostDTO;
import com.tsg.posts.models.response.PostDetailsResponse;
import com.tsg.posts.models.response.PostResponse;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CustomerRepository customerRepository;
    private final GenericMapper mapper;
    
    public PostDetailsResponse createPost(Long id, PostDTO postRequest) {
         Optional<Customer> customer = customerRepository.findById(id);
        if (customer == null ) {
            throw new NotFoundException("El usuario no existe.");
		}
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setStatus(PostStatus.DRAFT);
        postRepository.save(post);
        customer.get().getPosts().add(post);
        customerRepository.save(customer.get());
        return mapper.map(post, PostDetailsResponse.class);
    }

    public Page<PostResponse> findAllWithFiltersPageable(GenericPage page) {
        int offset = page.getPageNumber() * page.getPageSize();
		long totalElements = postRepository.findAll().size();
		List<PostResponse> response = postRepository.findPostPage(page.getSortDirection().toString(), page.getPageSize(), offset);
		return new PageImpl<>(response, PageRequest.of(page.getPageNumber(), page.getPageSize(), page.getSortDirection(), page.getSortBy()),
				totalElements);
	}

    public PostDetailsResponse getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("El post no existe."));
        return mapper.map(post, PostDetailsResponse.class);
    }

    public PostDTO updatePost(Long id, PostDTO postRequest) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("El post no existe."));
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setStatus(postRequest.getStatus());
        postRepository.save(post);
        return postRequest;
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("El post no existe."));
        postRepository.delete(post);
    }
    
}
