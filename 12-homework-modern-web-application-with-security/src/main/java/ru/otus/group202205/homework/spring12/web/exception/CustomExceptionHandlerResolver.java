package ru.otus.group202205.homework.spring12.web.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.group202205.homework.spring12.exception.LibraryGeneralException;

@Component
@ControllerAdvice
public class CustomExceptionHandlerResolver {

  @ExceptionHandler(value = BindException.class)
  protected ModelAndView handleBindException(BindException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) {
    ModelMap modelMap = new ModelMap();
    modelMap.addAttribute("entityClass",
        ex
            .getBindingResult()
            .getObjectName());
    modelMap.addAttribute("error",
        ex
            .getAllErrors()
            .toString());
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return new ModelAndView("error-400",
        modelMap);
  }

  @ExceptionHandler(value = LibraryGeneralException.class)
  protected ModelAndView handleLibraryGeneralException(LibraryGeneralException ex, HttpServletRequest request, HttpServletResponse response,
      @Nullable Object handler) {
    ModelMap modelMap = new ModelMap();
    modelMap.addAttribute("error",
        ex.toString());
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return new ModelAndView("error-400",
        modelMap);
  }

  @ExceptionHandler(value = BeansException.class)
  protected ModelAndView handleBeansException(BeansException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) {
    ModelMap modelMap = new ModelMap();
    modelMap.addAttribute("error",
        ex.getMessage());
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return new ModelAndView("error-400",
        modelMap);
  }

}
