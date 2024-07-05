package ru.raccoon.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.raccoon.controller.PostController;
import ru.raccoon.repository.PostRepository;
import ru.raccoon.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private static final String CONST_PATH_PART = "/api/posts";
  private static final String CONST_PATH_ID = "/\\d+";

  @Override
  public void init() {
    final var context = new AnnotationConfigApplicationContext("ru.raccoon");
    controller = context.getBean("postController", PostController.class);

  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals("GET") && path.equals(CONST_PATH_PART)) {
        controller.all(resp);
        return;
      }
      if (method.equals("GET") && path.matches(CONST_PATH_PART + CONST_PATH_ID)) {
        // easy way
        final var id = getIdFromPath(path);
        controller.getById(id, resp);
        return;
      }
      if (method.equals("POST") && path.equals(CONST_PATH_PART)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals("DELETE") && path.matches(CONST_PATH_PART + CONST_PATH_ID)) {
        // easy way
        final var id = getIdFromPath(path);
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  long getIdFromPath(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
  }
}

