package org.hunter.userrequestlogeventingservice.repository;

import java.util.List;
import java.util.UUID;

import org.hunter.userrequestlogeventingservice.model.UserRequestLog;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hunter.model.UserRequestLogView;

public interface UserRequestLogRepository extends CrudRepository<UserRequestLog, UUID> {

    @Query(" select user_id, max_payment_value_cents / 100 as max_payment_amount, created, updated from user_request_log where user_id = :userId "
            + "order by created desc limit 10")
    List<UserRequestLogView> findByUserId(@Param("userId") UUID userId);

}
