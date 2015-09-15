package example.service.company;

import org.springframework.data.repository.Repository;

import example.domain.Company;

public interface CompanyRepository extends Repository<Company, Integer>{
    
    Company findByCompanyId(int companyId);
}