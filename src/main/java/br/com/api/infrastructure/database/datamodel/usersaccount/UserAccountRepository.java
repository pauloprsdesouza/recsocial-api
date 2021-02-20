package br.com.api.infrastructure.database.datamodel.usersaccount;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

    @Query(value = "SELECT * FROM user_account WHERE email = ?1", nativeQuery = true)
    public Optional<UserAccount> findByEmail(String email);

    @Query(value = "SELECT * FROM user_account WHERE token = ?1", nativeQuery = true)
    public UserAccount findByToken(String token);
}
