package example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import example.domain.Company;
import example.service.company.CompanyRepository;

@RestController
@RequestMapping(value="/company")
//@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @RequestMapping(method=RequestMethod.GET)
    public List<Company> getAll() {
      return null;
    }    
    
    @RequestMapping(method=RequestMethod.GET, value="/{companyId}")
    public Company getCompany(@PathVariable int companyId) {
        Company company = companyRepository.findByCompanyId(companyId);
        return company;
    }
}
