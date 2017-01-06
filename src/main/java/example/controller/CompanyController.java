package example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import example.domain.Company;
import example.repository.CompanyRepository;

@RestController
@RequestMapping(value = "/rest")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Company> getAll() {
        return null;
    }

    // http://localhost:8088/rest/company/{"companyId":"1","user":"user1","password":"password1"}
    @RequestMapping(method = RequestMethod.GET, value = "/company/{parms}")
    public Company getCompany(@PathVariable String parms) {
        JsonObject obj = (JsonObject) new JsonParser().parse(parms);
        int companyId = Integer.parseInt(obj.get("companyId").getAsString());
        Company company = companyRepository.findByCompanyId(companyId);
        if(company == null) {
        	company = new Company();
        }
        return company;
    }

    @RequestMapping(value = "/hello/{user}/{password}", method = RequestMethod.GET)
    public ResponseEntity<HelloweenResponse> hello(Principal principal) {
        
        return new ResponseEntity<HelloweenResponse>(new HelloweenResponse("Hello, " + principal.getName()
                + "!"), HttpStatus.OK);
    }

    public static class HelloweenResponse {
        private String message;

        public HelloweenResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
