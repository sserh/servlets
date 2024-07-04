package ru.raccoon.repository;

import ru.raccoon.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {

  final ConcurrentLinkedQueue<Post> postCollection = new ConcurrentLinkedQueue<>();
  final ConcurrentLinkedQueue<Long> idCollection = new ConcurrentLinkedQueue<>();
  static AtomicLong idValue = new AtomicLong(0);

  public List<Post> all() {
      return new ArrayList<>(postCollection);
  }

  public Optional<Post> getById(long id) {
    for (Post post : postCollection) {
      if (post.getId() == id) {
        return Optional.of(post);
      }
    }
    return Optional.empty();
  }

  public Post save(Post post) {
    long postId = post.getId();
    if (postId == 0) {
      while (idCollection.contains(idValue.get())) {
        idValue.getAndIncrement();
      }
        post.setId(idValue.get());
    } else {
      if (idCollection.contains(idValue.get())) {
        removeById(postId);
      }
    }
      postCollection.add(post);
      idCollection.add(postId);
      return post;
  }

  public void removeById(long id) {
    for (Post post : postCollection) {
    long postId = post.getId();
        if (postId == id) {
          postCollection.remove(post);
          idCollection.remove(postId);
        }
    }
  }
}
