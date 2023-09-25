package africa.semicolon.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
//@EnableMethodSecurity
public class DemoController {

    @GetMapping
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello from this side");
    }

//    @PostMapping("/hello-2")
//    @PreAuthorize("hasRole('LIBRARIAN')")
//    public String hello2(@RequestBody User user){
//        return "Working...";
//    }
}
