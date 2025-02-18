package com.tsg.posts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
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
import com.tsg.posts.service.PostService;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private GenericMapper mapper;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePost_UserExists() {
        Customer customer = new Customer();
        customer.setId(1L);

        Post post = new Post();
        post.setTitle("Sample Title");
        post.setContent("Sample Content");
        post.setStatus(PostStatus.DRAFT);
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Sample Title");
        postDTO.setContent("Sample Content");
        PostDetailsResponse postDetailsResponse = new PostDetailsResponse();
        postDetailsResponse.setTitle("Sample Title");
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(mapper.map(any(Post.class), eq(PostDetailsResponse.class))).thenReturn(postDetailsResponse);
        PostDetailsResponse response = postService.createPost(1L, postDTO);
        System.out.println(response.getTitle());
        assertEquals("Sample Title", response.getTitle());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testCreatePost_UserNotExists() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        PostDTO postDTO = new PostDTO();
        assertThrows(NotFoundException.class, () -> postService.createPost(1L, postDTO));
    }

    @Test
    public void testFindAllWithFiltersPageable() {
        GenericPage page = new GenericPage();
        page.setPageNumber(0);
        page.setPageSize(10);
        page.setSortDirection(org.springframework.data.domain.Sort.Direction.ASC);
        List<PostResponse> postResponses = Collections.emptyList();
        when(postRepository.findPostPage(anyString(), anyInt(), anyInt())).thenReturn(postResponses);
        when(postRepository.findAll()).thenReturn(Collections.emptyList());
        Page<PostResponse> response = postService.findAllWithFiltersPageable(page);
        assertEquals(0, response.getTotalElements());
    }

    @Test
    public void testGetPostById_PostExists() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Sample Title");
        post.setContent("Sample Content");
        PostDetailsResponse postDetailsResponse = new PostDetailsResponse();
        postDetailsResponse.setTitle("Sample Title");
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(mapper.map(any(Post.class), eq(PostDetailsResponse.class))).thenReturn(postDetailsResponse);
        PostDetailsResponse response = postService.getPostById(1L);
        assertEquals("Sample Title", response.getTitle());
    }

    @Test
    public void testGetPostById_PostNotExists() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> postService.getPostById(1L));
    }

    @Test
    public void testUpdatePost_PostExists() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Old Title");
        post.setContent("Old Content");
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("New Title");
        postDTO.setContent("New Content");
        postDTO.setStatus(PostStatus.PUBLISHED);
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        PostDTO response = postService.updatePost(1L, postDTO);
        assertEquals("New Title", response.getTitle());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void testUpdatePost_PostNotExists() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());
        PostDTO postDTO = new PostDTO();
        assertThrows(NotFoundException.class, () -> postService.updatePost(1L, postDTO));
    }

    @Test
    public void testDeletePost_PostExists() {
        Post post = new Post();
        post.setId(1L);
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        postService.deletePost(1L);
        verify(postRepository, times(1)).delete(any(Post.class));
    }

    @Test
    public void testDeletePost_PostNotExists() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> postService.deletePost(1L));
    }
}