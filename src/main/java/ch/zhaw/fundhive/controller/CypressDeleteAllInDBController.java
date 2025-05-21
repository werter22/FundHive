package ch.zhaw.fundhive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.fundhive.service.helpers.CypressDeleteAllInDBService;
import ch.zhaw.fundhive.service.helpers.UserService;

@RestController
@RequestMapping("/api")
public class CypressDeleteAllInDBController {

    @Autowired
    private UserService userService;

    @Autowired
    private CypressDeleteAllInDBService service;

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllInDB() {
        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        service.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("DELETED");
    }

}
