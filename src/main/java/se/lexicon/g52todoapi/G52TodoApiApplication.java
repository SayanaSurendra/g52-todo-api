package se.lexicon.g52todoapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import se.lexicon.g52todoapi.domain.dto.RoleDTOForm;
import se.lexicon.g52todoapi.domain.dto.UserDTOForm;
import se.lexicon.g52todoapi.service.RoleService;
import se.lexicon.g52todoapi.service.UserService;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@SpringBootApplication
public class G52TodoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(G52TodoApiApplication.class, args);
    }


    @Bean
    @Profile("!test")
    public CommandLineRunner runner (RoleService roleService, UserService userService) {
        return (args)-> {

            roleService.getAll().forEach(System.out::println);

            Set<RoleDTOForm> roleDTOForms=new HashSet<>();
            RoleDTOForm roleDTOForm=new RoleDTOForm();
            roleDTOForm.setName("ADMIN");
            roleDTOForms.add(roleDTOForm);

            UserDTOForm userDTOForm = new UserDTOForm("test@gmail.com","1234",roleDTOForms);
            userService.register(userDTOForm);
        } ;
    }

}
