package ru.raccoon.model;

public class InternalPost {
    private long id;
    private String content;
    private boolean removed;

    public InternalPost() {
    }

    public InternalPost(long id, String content) {
        this.id = id;
        this.content = content;
        this.removed = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public Post convertToPost() {
        Post post = new Post();
        post.setId(this.getId());
        post.setContent(this.getContent());
        return post;
    }

    public InternalPost convertToInternalPost(Post post) {
        InternalPost internalPost = new InternalPost();
        internalPost.setId(post.getId());
        internalPost.setContent(post.getContent());
        internalPost.setRemoved(false);
        return internalPost;
    }
}
