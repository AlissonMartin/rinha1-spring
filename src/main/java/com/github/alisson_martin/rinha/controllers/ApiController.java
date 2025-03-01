package com.github.alisson_martin.rinha.controllers;

import com.github.alisson_martin.rinha.dtos.CreateDTO;
import com.github.alisson_martin.rinha.models.User;
import com.github.alisson_martin.rinha.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class ApiController {

  @Autowired
  UserService userService;

  @PostMapping("")
  public ResponseEntity create(@Validated @RequestBody CreateDTO body) {
    if (body.apelido() == null || body.apelido().isBlank() || body.apelido().length() > 32 || body.nome() == null || body.nome().isBlank() || body.nome().length() > 100 || nascimentoIsInvalid(body.nascimento())) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }
    String uid = userService.create(body);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.LOCATION, "/pessoas/" + uid);
    return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
  }

  @GetMapping("")
  public ResponseEntity list(@RequestParam String t) {
    List<User> users = userService.list(t);

    return ResponseEntity.ok(users);
  }

  @GetMapping("/{uid}")
  public ResponseEntity getById(@PathVariable String uid) {
    User user = userService.getById(uid);

    return ResponseEntity.ok(user);
  }

  private boolean nascimentoIsInvalid(String nascimento) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    sdf.setLenient(false);
    try {
      sdf.parse(nascimento);
      return false;
    } catch (ParseException e) {
      return true;
    }
  }
}
