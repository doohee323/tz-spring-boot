package hello.service;

import hello.model.Company;

import org.springframework.data.repository.Repository;

public interface CompanyRepository extends Repository<Company, Integer>{
    
    Company findByCompanyId(int companyId);
}