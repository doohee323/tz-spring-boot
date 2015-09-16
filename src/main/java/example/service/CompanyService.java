package example.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import example.domain.Company;
import example.repository.CompanyRepository;

@Component("companyService")
@Transactional
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Company getCompany(Integer company_id) {
        return this.companyRepository.findByCompanyId(company_id);
    }
}