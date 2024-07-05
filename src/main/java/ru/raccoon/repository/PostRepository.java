package ru.raccoon.repository;

import org.springframework.stereotype.Repository;
import ru.raccoon.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
// Stub
public class PostRepository {

  private static final ConcurrentHashMap<Long, Post> postCollection = new ConcurrentHashMap<>();
  private static final AtomicLong idValue = new AtomicLong(0);

  public List<Post> all() {
    return new ArrayList<>(postCollection.values());
  }

  public Optional<Post> getById(long id) {
    for (Post post : postCollection.values()) {
      if (post.getId() == id) {
        return Optional.of(post);
      }
    }
    return Optional.empty();
  }

  public Post save(Post newPost) {
    //при Id == 0 добавляем в коллекцию пост с новым несуществующим Id,
    //при Id != 0 добавляем пост с указанным Id в коллекцию, если же Id уже существует, то меняем контент поста с указанным Id в коллекции
    List<Long> ids = Collections.list(postCollection.keys());
    long postId = newPost.getId();
    if (postId == 0) {
      while (ids.contains(idValue.get())) {
        idValue.getAndIncrement();
      }
        newPost.setId(idValue.get());
    } else {
      if (ids.contains(postId)) {
        for (Post postInCollection : postCollection.values()) {
          if (postInCollection.getId() == postId) {
            postInCollection.setContent(newPost.getContent());
            return newPost;
          }
        }
      }
    }
      postCollection.put(newPost.getId(), newPost);
      return newPost;
  }

  public void removeById(long id) {
    for (Post post : postCollection.values()) {
    long postId = post.getId();
        if (postId == id) {
          postCollection.remove(post.getId());
        }
    }
  }
}
