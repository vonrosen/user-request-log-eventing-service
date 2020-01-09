package org.hunter.userrequestlogeventingservice.repository;

import java.util.UUID;

import org.hunter.userrequestlogeventingservice.model.UserRequestLog;
import org.springframework.data.repository.CrudRepository;

public interface UserRequestLogRepository extends CrudRepository<UserRequestLog, UUID> { }
