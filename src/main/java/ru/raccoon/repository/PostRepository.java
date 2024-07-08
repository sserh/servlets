package ru.raccoon.repository;

import ru.raccoon.exception.NotFoundException;
import ru.raccoon.model.InternalPost;
import ru.raccoon.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepository {

  private static final ConcurrentHashMap<Long, InternalPost> postCollection = new ConcurrentHashMap<>();
  private static final AtomicLong idValue = new AtomicLong(0);

  public List<Post> all() {
    ArrayList<Post> posts = new ArrayList<>();
    for (InternalPost internalPost : postCollection.values()) {
      if (!internalPost.isRemoved()) {
        posts.add(internalPost.convertToPost());
      }
    }
    return posts;
  }

  public Optional<Post> getById(long id) {
    for (InternalPost internalPost : postCollection.values()) {
      if ((internalPost.getId() == id) && (!internalPost.isRemoved())) {
        return Optional.of(internalPost.convertToPost());
      }
    }
    throw new NotFoundException("Post with id " + id + " is not found");
  }

  public Post save(Post newPost) {

    InternalPost internalPost = new InternalPost();
    internalPost = internalPost.convertToInternalPost(newPost);

    //при Id == 0 добавляем в коллекцию пост с новым несуществующим Id,
    //при Id != 0 меняем контент поста, если пост существует и не помечен удалённым
    List<Long> ids = Collections.list(postCollection.keys());
    long postId = internalPost.getId();
    if (postId == 0) {
      while (ids.contains(idValue.get())) {
        idValue.getAndIncrement();
      }
        internalPost.setId(idValue.get());
    } else {
      if ((ids.contains(postId))) {
        if (!postCollection.get(internalPost.getId()).isRemoved()) {
          for (InternalPost postInCollection : postCollection.values()) {
            if (postInCollection.getId() == postId) {
              postInCollection.setContent(newPost.getContent());
              return internalPost.convertToPost();
            }
          }
        } else {
          throw new NotFoundException("Post with id " + postId + " is not found");
        }
      }
    }
      postCollection.put(internalPost.getId(), internalPost);
      return internalPost.convertToPost();
  }

  public void removeById(long id) {
    for (InternalPost post : postCollection.values()) {
    long postId = post.getId();
      if ((postId == id) && (!post.isRemoved())) {
        post.setRemoved(true);
        return;
      }
    }
    throw new NotFoundException("Post with id " + id + " is not found");
  }
}
