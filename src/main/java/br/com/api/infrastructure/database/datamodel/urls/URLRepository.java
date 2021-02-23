package br.com.api.infrastructure.database.datamodel.urls;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface URLRepository extends JpaRepository<URL, Integer> {
    @Query(value = "SELECT * FROM url " + "WHERE name IN (?1)", nativeQuery = true)
    public List<URL> containsUrls(Set<String> urls);

    @Query(value = "SELECT * FROM url " + "WHERE name = ?1", nativeQuery = true)
    public URL withName(String url);
}
