package org.aitesting.microservices.authentication.repository;

import org.aitesting.microservices.authentication.model.UserOrganization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgUserRepository extends CrudRepository<UserOrganization, String> {
    public UserOrganization findByUsername(String username);
}
