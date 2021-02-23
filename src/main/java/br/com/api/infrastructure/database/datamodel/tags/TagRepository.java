package br.com.api.infrastructure.database.datamodel.tags;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query(value = "SELECT * FROM tag WHERE name IN (?1)", nativeQuery = true)
    public List<Tag> containsTags(Set<String> tags);

    @Query(value = "SELECT * FROM tag WHERE name = ?1", nativeQuery = true)
    public Tag withName(String tag);
}
